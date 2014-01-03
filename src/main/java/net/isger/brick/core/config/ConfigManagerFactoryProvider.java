package net.isger.brick.core.config;

import net.isger.brick.core.inject.ContainerBuilder;
import net.isger.brick.core.inject.ContainerProvider;
import net.isger.brick.core.inject.Scope;

public class ConfigManagerFactoryProvider implements ContainerProvider {

    private Class<? extends ConfigManagerFactory> clazz;

    public ConfigManagerFactoryProvider(
            Class<? extends ConfigManagerFactory> clazz) {
        this.clazz = clazz;
    }

    public boolean isReload() {
        return false;
    }

    public void register(ContainerBuilder builder) {
        builder.factory(ConfigManagerFactory.class, clazz.getSimpleName(),
                clazz, Scope.SINGLETON);
    }

}
