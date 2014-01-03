package net.isger.brick.core.inject;

import net.isger.brick.BrickConstants;
import net.isger.brick.core.config.ConfigManager;
import net.isger.brick.core.config.ConfigManagerFactory;
import net.isger.brick.core.config.ConfigManagerFactoryProvider;
import net.isger.brick.core.config.ConfigManagerProvider;
import net.isger.brick.core.config.DefaultConfigManager;
import net.isger.brick.core.config.DefaultConfigManagerFactory;

public class BootstrapBuilder {

    private BootstrapBuilder() {
    }

    public static ContainerBuilder getBuilder(ContainerProviders providers) {
        ContainerBuilder builder = new ContainerBuilder();
        boolean cmfRegisted = false;
        // 注册自定义配置管理器
        for (ContainerProvider provider : providers) {
            if (provider instanceof ConfigManagerProvider) {
                provider.register(builder);
            } else if (provider instanceof ConfigManagerFactoryProvider) {
                provider.register(builder);
                cmfRegisted = true;
            }
        }
        // 注册系统默认配置管理器
        builder.factory(ConfigManager.class, Container.SYSTEM,
                DefaultConfigManager.class, Scope.SINGLETON);
        if (!cmfRegisted) {
            builder.factory(ConfigManagerFactory.class,
                    DefaultConfigManagerFactory.class, Scope.SINGLETON);
        }
        builder.constant(BrickConstants.KEY_ENABLED_RELOAD,
                Boolean.FALSE.toString());
        return builder;
    }

}
