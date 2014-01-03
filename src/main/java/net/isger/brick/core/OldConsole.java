package net.isger.brick.core;

import net.isger.brick.BrickException;
import net.isger.brick.core.inject.Container;
import net.isger.brick.core.inject.ContainerProviders;

/**
 * 控制台
 * 
 * @author issing
 * 
 */
public class OldConsole implements Console {

    private static final Object LOCKED = new Object();

    private static OldConsole CONSOLE;

    private Modules modules;

    private transient boolean startup;

    protected OldConsole() {
        modules = new Modules();
        startup = false;
    }

    /**
     * 获取活动控制台
     * 
     * @return
     */
    public static OldConsole getActionConsole() {
        synchronized (LOCKED) {
            if (CONSOLE == null) {
                // CONSOLE = Config.setup();
            }
        }
        return CONSOLE;
    }

    /**
     * 执行操作指令
     * 
     * @param cmd
     * @return
     */
    public static Object execute(Command cmd) {
        OldConsole console = getActionConsole();
        synchronized (console) {
            // 自动加载默认砖头
            if (!console.isStartup()) {
                Config.load(Config.CONF_FILE_APP);
            }
        }
        // Preparers.prepare(cmd);
        String name = Commands.getModuleName(cmd);
        if (name == null) {
            console.operate();
        } else {
            Module module = console.modules.get(name);
            if (module == null) {
                throw new BrickException("Not found the module by " + name);
            }
            Context.getActionContext().set(Context.MODULE, module);
            module.execute();
        }
        // Preparers.cleanup(cmd);
        return cmd.getResult();
    }

    /**
     * 是否启动
     * 
     * @return
     */
    public boolean isStartup() {
        return startup;
    }

    /**
     * 启动
     * 
     */
    public synchronized void startup() {
        if (startup) {
            return;
        }
        modules.initial();
        startup = true;
    }

    /**
     * 获取模块
     * 
     * @return
     */
    public Modules getModules() {
        return modules;
    }

    /**
     * 核心操作
     * 
     */
    protected void operate() {
        throw new BrickException("Not implemented the console");
    }

    /**
     * 关闭
     * 
     */
    public synchronized void shutdown() {
        if (!startup) {
            return;
        }
        modules.destroy();
        startup = false;
    }

    public Container getContainer() {
        return null;
    }

    public void reload(ContainerProviders providers) {

    }

    public <T> T getInstance(Class<T> clazz, String name) {
        return null;
    }

    @Override
    public void initial() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
