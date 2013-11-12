package net.isger.brick.core;

import net.isger.brick.BrickException;

/**
 * 控制台
 * 
 * @author issing
 * 
 */
public class Console {

    private static final Object LOCKED = new Object();

    private static Console CONSOLE;

    private Modules modules;

    private transient boolean startup;

    protected Console() {
        modules = new Modules();
        startup = false;
    }

    /**
     * 获取活动控制台
     * 
     * @return
     */
    public static Console getActionConsole() {
        synchronized (LOCKED) {
            if (CONSOLE == null) {
                CONSOLE = Config.setup();
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
        Console console = getActionConsole();
        synchronized (console) {
            // 自动加载默认砖头
            if (!console.isStartup()) {
                Config.load(Config.CONF_FILE_APP);
            }
        }
        Prepares.prepare(cmd);
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
        Prepares.cleanup(cmd);
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

}
