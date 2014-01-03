package net.isger.brick.core.inject;

import java.util.Map;

import net.isger.brick.util.Reflects;
import net.isger.brick.util.reflect.BoundField;

class InternalContainer implements Container {

    final Map<Key<?>, InternalFactory<?>> factories;

    final ScopeStrategies strategies;

    private ThreadLocal<Object[]> context;

    InternalContainer(Map<Key<?>, InternalFactory<?>> factories) {
        this.factories = factories;
        this.strategies = new ScopeStrategies();
        this.context = new ThreadLocal<Object[]>() {
            protected Object[] initialValue() {
                return new Object[1];
            }
        };
    }

    public void initial() {
    }

    public void setStrategy(Class<?> type, Strategy strategy) {
        strategies.add(type, DEFAULT_NAME, strategy);
    }

    public void setStrategy(Class<?> type, String name, Strategy strategy) {
        strategies.add(type, name, strategy);
    }

    <T> T inject(Class<? extends T> implementation) {
        T instance = Reflects.newInstance(implementation);
        inject(instance);
        return instance;
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
        ExternalContext<?> previous = context.getExternalContext();
        Key<T> key = Key.newInstance(type, name);
        context.setExternalContext(ExternalContext.newInstance(null, key, this));
        try {
            InternalFactory<? extends T> factory = (InternalFactory<? extends T>) factories
                    .get(key);
            if (factory != null) {
                return factory.create(context);
            } else {
                return null;
            }
        } finally {
            context.setExternalContext(previous);
        }
    }

    public void destroy() {

    }

    private interface ContextualCallable<T> {

        public T call(InternalContext context);

    }

}
