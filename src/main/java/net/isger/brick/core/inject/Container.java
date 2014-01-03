package net.isger.brick.core.inject;

/**
 * 容器
 * 
 * @author issing
 * 
 */
public interface Container {

    public static final String DEFAULT_NAME = "default";

    public static final String SYSTEM = "system";

    public void initial();

    public void setStrategy(Class<?> type, Strategy strategy);

    public void setStrategy(Class<?> type, String name, Strategy strategy);

    public void inject(Object instance);

    public <T> T getInstance(Class<T> type);

    public <T> T getInstance(Class<T> type, String name);

    public void destroy();

}
