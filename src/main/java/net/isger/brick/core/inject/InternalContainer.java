package net.isger.brick.core.inject;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.BrickConstants;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.reflect.BoundField;

/**
 * 内部容器
 * 
 * @author issing
 * 
 */
class InternalContainer implements Container {

    final Map<Key<?>, InternalFactory<?>> facs;

    final ScopeStrategies stgs;

    private ThreadLocal<Object[]> context;

    InternalContainer(Map<Key<?>, InternalFactory<?>> factories) {
        this.facs = new HashMap<Key<?>, InternalFactory<?>>(factories);
        this.stgs = new ScopeStrategies();
        this.context = new ThreadLocal<Object[]>() {
            protected Object[] initialValue() {
                return new Object[1];
            }
        };
    }

    public void initial() {
        final Container container = this;
        this.facs.put(Key.newInstance(Container.class,
                BrickConstants.KEY_BRICK_CONTAINER),
                new InternalFactory<Container>() {
                    public Container create(InternalContext context) {
                        return container;
                    }
                });
    }

    public void setStrategy(Class<?> type, Strategy strategy) {
        stgs.add(type, DEFAULT_NAME, strategy);
    }

    public void setStrategy(Class<?> type, String name, Strategy strategy) {
        stgs.add(type, name, strategy);
    }

    public void inject(final Object instance) {
        call(new ContextualCallable<Void>() {
            public Void call(InternalContext context) {
                inject(instance, context);
                return null;
            }
        });
    }

    private void inject(Object instance, InternalContext context) {
        Class<?> type = instance.getClass();
        for (BoundField field : Reflects.getBoundFields(type)) {
            field.setValue(instance,
                    getInstance(field.getField().getType(), field.getName()));
        }
    }

    public <T> T getInstance(final Class<T> type) {
        return call(new ContextualCallable<T>() {
            public T call(InternalContext context) {
                return getInstance(type, context);
            }
        });
    }

    public <T> T getInstance(final Class<T> type, final String name) {
        return call(new ContextualCallable<T>() {
            public T call(InternalContext context) {
                return getInstance(type, name, context);
            }
        });
    }

    /**
     * 上下文传递
     * 
     * @param callable
     * @return
     */
    private <T> T call(ContextualCallable<T> callable) {
        Object[] reference = context.get();
        if (reference[0] == null) {
            reference[0] = new InternalContext(this);
            try {
                return callable.call((InternalContext) reference[0]);
            } finally {
                reference[0] = null;
                context.remove();
            }
        } else {
            return callable.call((InternalContext) reference[0]);
        }
    }

    private <T> T getInstance(Class<T> type, InternalContext context) {
        return getInstance(type, DEFAULT_NAME, context);
    }

    @SuppressWarnings("unchecked")
    private <T> T getInstance(Class<T> type, String name,
            InternalContext context) {
        InternalFactory<? extends T> factory = (InternalFactory<? extends T>) facs
                .get(Key.newInstance(type, name));
        return factory == null ? null : factory.create(context);
    }

    public void destroy() {

    }

    private interface ContextualCallable<T> {

        public T call(InternalContext context);

    }

}
