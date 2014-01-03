package net.isger.brick.core.inject;

import java.util.concurrent.Callable;

public interface Strategy {

    public <T> T find(Class<T> type, String name, Callable<? extends T> callable)
            throws Exception;

}
