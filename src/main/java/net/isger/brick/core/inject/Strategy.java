package net.isger.brick.core.inject;

import java.util.concurrent.Callable;

/**
 * 注入策略
 * 
 * @author issing
 * 
 */
public interface Strategy {

    /**
     * 查找实例
     * 
     * @param type
     * @param name
     * @param callable
     * @return
     * @throws Exception
     */
    public <T> T find(Class<T> type, String name, Callable<? extends T> callable)
            throws Exception;

}
