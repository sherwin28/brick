package net.isger.brick.core.inject;

class InternalContext {

    final InternalContainer container;

    private ExternalContext<?> externalContext;

    private Strategy strategy;

    InternalContext(InternalContainer container) {
        this.container = container;
    }

    Strategy getStrategy(Class<?> type, String name) {
        if (strategy == null) {
            strategy = (Strategy) container.strategies.get(type, name);
            if (strategy == null) {
                throw new IllegalStateException(
                        "Scope strategy not set. Please call Container.setStrategy().");
            }
        }
        return strategy;
    }

    @SuppressWarnings("unchecked")
    <T> ExternalContext<T> getExternalContext() {
        return (ExternalContext<T>) externalContext;
    }

    void setExternalContext(ExternalContext<?> externalContext) {
        this.externalContext = externalContext;
    }

}
