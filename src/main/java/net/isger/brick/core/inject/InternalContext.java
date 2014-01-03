package net.isger.brick.core.inject;

/**
 * 内部上下文
 * 
 * @author issing
 * 
 */
class InternalContext {

    final InternalContainer container;

    private Strategy strategy;

    InternalContext(InternalContainer container) {
        this.container = container;
    }

    public Strategy getStrategy(Class<?> type, String name) {
        if (strategy == null) {
            strategy = (Strategy) container.stgs.get(type, name);
            if (strategy == null) {
                throw new IllegalStateException(
                        "Scope strategy not set. Please call Container.setStrategy().");
            }
        }
        return strategy;
    }

}
