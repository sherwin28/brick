package net.isger.brick.core;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.isger.brick.BrickConstants;
import net.isger.brick.core.config.BrickConfigProvider;
import net.isger.brick.core.inject.BootstrapBuilder;
import net.isger.brick.core.inject.Container;
import net.isger.brick.core.inject.ContainerBuilder;
import net.isger.brick.core.inject.ContainerProvider;
import net.isger.brick.core.inject.ContainerProviders;
import net.isger.brick.core.inject.Strategy;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.reflect.BoundField;

/**
 * 控制台管理器
 * 
 * @author issing
 * 
 */
public class ConsoleManager {

    private Lock lock;

    private String name;

    private boolean isReload;

    private boolean isChanged;

    private ContainerProviders providers;

    private Console console;

    public ConsoleManager() {
        this(BrickConstants.BRICK);
    }

    public ConsoleManager(String name) {
        this.lock = new ReentrantLock();
        this.name = name;
        this.isReload = false;
        this.isChanged = false;
        this.providers = new ContainerProviders();
    }

    /**
     * 获取控制台
     * 
     * @return
     */
    public final synchronized Console getConsole() {
        if (console == null) {
            // 创建容器并初始化
            final Container container = createContainer(getContainerProviders());
            container.setStrategy(Console.class, new Strategy() {
                public <T> T find(Class<T> type, String name,
                        Callable<? extends T> factory) throws Exception {
                    T result = factory.call();
                    BoundField field = Reflects.getBoundField(
                            result.getClass(),
                            BrickConstants.KEY_BRICK_CONTAINER);
                    if (field != null) {
                        field.setValue(result, container);
                    }
                    return result;
                }
            });
            container.initial();
            // 获取控制台实例并初始化
            console = container.getInstance(Console.class);
            console.initial();
        } else {
            // 根据条件重新加载配置
            reload();
        }
        return console;
    }

    /**
     * 获取提供器
     * 
     * @return
     */
    public ContainerProviders getContainerProviders() {
        lock.lock();
        try {
            if (providers.size() == 0) {
                providers.add(new BrickConfigProvider());
            }
            return providers;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 重加载配置
     * 
     */
    public final synchronized void reload() {
        if (isReload || isChanged) {
            ContainerProviders providers = getContainerProviders();
            if (providers.isReload()) {
                // 注销控制台及容器
                Container container = console.getContainer();
                console.destroy();
                container.destroy();
                // 创建容器并初始化
                container = createContainer(providers);
                container.initial();
                // 获取控制台实例并初始化
                console = container.getInstance(Console.class);
                console.initial();
                // 设置重新加载标识
                isReload = Boolean.parseBoolean(container.getInstance(
                        String.class, BrickConstants.KEY_ENABLED_RELOAD));
            }
            isChanged = false;
        }
    }

    /**
     * 创建容器
     * 
     * @param providers
     * @return
     */
    protected Container createContainer(ContainerProviders providers) {
        Container bootstrap = createBootstrap(providers);
        bootstrap.initial();
        ContainerBuilder builder = createContainerBuilder();
        Iterator<ContainerProvider> iter = providers.iterator();
        ContainerProvider provider;
        while (iter.hasNext()) {
            provider = iter.next();
            bootstrap.inject(provider);
            provider.register(builder);
        }
        builder.constant(BrickConstants.KEY_BRICK_NAME, name);
        bootstrap.destroy();
        return builder.create();
    }

    /**
     * 创建容器构建器
     * 
     * @return
     */
    protected ContainerBuilder createContainerBuilder() {
        return new ContainerBuilder();
    }

    /**
     * 创建引导容器
     * 
     * @param providers
     * @return
     */
    protected Container createBootstrap(ContainerProviders providers) {
        return BootstrapBuilder.getBuilder(providers).create();
    }

}
