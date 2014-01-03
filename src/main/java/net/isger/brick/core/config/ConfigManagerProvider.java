package net.isger.brick.core.config;

import net.isger.brick.core.inject.ContainerBuilder;
import net.isger.brick.core.inject.ContainerProvider;
import net.isger.brick.core.inject.Scope;

public class ConfigManagerProvider implements ContainerProvider {

    private Class<? extends ConfigManager> clazz;

    private String name;

    public ConfigManagerProvider(Class<? extends ConfigManager> clazz,
            String name) {
        this.clazz = clazz;
        this.name = name;
    }

    public boolean isReload() {
        return false;
    }

    public void register(ContainerBuilder builder) {
        builder.factory(ConfigManager.class, name, clazz, Scope.SINGLETON);
    }

}
