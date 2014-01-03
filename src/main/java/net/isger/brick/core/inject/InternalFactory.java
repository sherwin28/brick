package net.isger.brick.core.inject;

/**
 * 内部工厂
 * 
 * @author issing
 * @param <T>
 */
interface InternalFactory<T> {

    /**
     * 创建实例
     * 
     * @param context
     * @return
     */
    public T create(InternalContext context);

}
