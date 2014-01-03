package net.isger.brick.core.inject;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.BrickException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 容器构建器
 * 
 * @author issing
 * 
 */
public final class ContainerBuilder {

    private static final Logger LOG;

    private final Map<Key<?>, InternalFactory<?>> facs;

    private boolean duplicates;

    static {
        LOG = LoggerFactory.getLogger(ContainerBuilder.class);
    }

    public ContainerBuilder() {
        this.facs = new HashMap<Key<?>, InternalFactory<?>>();
        this.duplicates = false;
    }

    public <T> ContainerBuilder factory(Class<T> type) {
        return factory(type, Container.DEFAULT_NAME, type);
    }

    public <T> ContainerBuilder factory(Class<T> type, String name) {
        return factory(type, name, type);
    }

    public <T> ContainerBuilder factory(Class<T> type,
            Class<? extends T> implementation) {
        return factory(type, Container.DEFAULT_NAME, implementation);
    }

    public <T> ContainerBuilder factory(Class<T> type, String name,
            Class<? extends T> implementation) {
        Scoped scoped = implementation.getAnnotation(Scoped.class);
        return factory(type, name, implementation,
                scoped == null ? Scope.DEFAULT : scoped.value());
    }

    public <T> ContainerBuilder factory(Class<T> type, Scope scope) {
        return factory(type, Container.DEFAULT_NAME, type, scope);
    }

    public <T> ContainerBuilder factory(Class<T> type, String name, Scope scope) {
        return factory(type, name, type, scope);
    }

    public <T> ContainerBuilder factory(Class<T> type,
            Class<? extends T> implementation, Scope scope) {
        return factory(type, Container.DEFAULT_NAME, implementation, scope);
    }

    public <T> ContainerBuilder factory(final Class<T> type, final String name,
            final Class<? extends T> implementation, Scope scope) {
        return factory(Key.newInstance(type, name), new InternalFactory<T>() {
            public T create(InternalContext context) {
                return context.container.inject(implementation);
            }
        }, scope);
    }

    public ContainerBuilder constant(String name, String value) {
        return constant(String.class, name, value);
    }

    public <T> ContainerBuilder constant(final Class<T> type,
            final String name, final T value) {
        return factory(Key.newInstance(type, name), new InternalFactory<T>() {
            public T create(InternalContext ignored) {
                return value;
            }
        }, Scope.DEFAULT);
    }

    private <T> ContainerBuilder factory(final Key<T> key,
            InternalFactory<? extends T> factory, Scope scope) {
        if (facs.containsKey(key)) {
            if (!duplicates) {
                throw new BrickException("Dependency mapping for " + key
                        + " already exists");
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn("Dependency mapping for {} already exists", key);
            }
        }
        facs.put(key, scope.factory(key.getType(), key.getName(), factory));
        return this;
    }

    public void setDuplicates(boolean duplicates) {
        this.duplicates = duplicates;
    }

    public Container create() {
        return new InternalContainer(new HashMap<Key<?>, InternalFactory<?>>(
                facs));
    }

}
