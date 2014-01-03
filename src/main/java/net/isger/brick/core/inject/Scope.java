package net.isger.brick.core.inject;

import java.util.concurrent.Callable;

/**
 * 作用域
 * 
 * @author issing
 * 
 */
public enum Scope {

    DEFAULT {
        protected <T> InternalFactory<? extends T> factory(Class<T> type,
                String name, InternalFactory<? extends T> factory) {
            return factory;
        }
    },

    SINGLETON {
        protected <T> InternalFactory<? extends T> factory(Class<T> type,
                String name, final InternalFactory<? extends T> factory) {
            return new InternalFactory<T>() {
                T instance;

                public T create(InternalContext context) {
                    synchronized (context.container) {
                        if (instance == null) {
                            instance = factory.create(context);
                        }
                        return instance;
                    }
                }
            };
        }
    },

    THREAD {
        protected <T> InternalFactory<? extends T> factory(Class<T> type,
                String name, final InternalFactory<? extends T> factory) {
            return new InternalFactory<T>() {
                private final ThreadLocal<T> threadLocal = new ThreadLocal<T>();

                public T create(final InternalContext context) {
                    T instance = threadLocal.get();
                    if (instance == null) {
                        instance = factory.create(context);
                        threadLocal.set(instance);
                    }
                    return instance;
                }
            };
        }
    },

    STRATEGY {
        protected <T> InternalFactory<? extends T> factory(final Class<T> type,
                final String name, final InternalFactory<? extends T> factory) {
            return new InternalFactory<T>() {
                public T create(InternalContext context) {
                    Strategy strategy = context.getStrategy(type, name);
                    try {
                        return strategy.find(type, name,
                                toCallable(context, factory));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    };

    protected <T> Callable<? extends T> toCallable(
            final InternalContext context,
            final InternalFactory<? extends T> factory) {
        return new Callable<T>() {
            public T call() throws Exception {
                return factory.create(context);
            }
        };
    }

    protected abstract <T> InternalFactory<? extends T> factory(Class<T> type,
            String name, InternalFactory<? extends T> factory);

}
