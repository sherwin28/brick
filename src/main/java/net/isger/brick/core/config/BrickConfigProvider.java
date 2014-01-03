package net.isger.brick.core.config;

import net.isger.brick.core.Console;
import net.isger.brick.core.DefaultConsole;
import net.isger.brick.core.Modules;
import net.isger.brick.core.inject.ContainerBuilder;
import net.isger.brick.core.inject.ContainerProvider;
import net.isger.brick.core.inject.Scope;
import net.isger.brick.core.preparer.Preparers;

public class BrickConfigProvider implements ContainerProvider {

    public boolean isReload() {
        return false;
    }

    public void register(ContainerBuilder builder) {
        builder.factory(Console.class, DefaultConsole.class, Scope.SINGLETON)
                .factory(Preparers.class, Scope.SINGLETON)
                .factory(Modules.class, Scope.SINGLETON);
    }

}
