package net.isger.brick.core;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commands extends Director {

    private static final String KEY_COMMANDS = "brick.core.commands";

    private static final String COMMAND_PATH = "net/isger/brick/core/command";

    private static final Logger LOG;

    private static final Commands COMMANDS;

    /** 所用命令类型 */
    private Map<Class<?>, String> cmdTypes;

    static {
        LOG = LoggerFactory.getLogger(Commands.class);
        COMMANDS = new Commands();
    }

    private Commands() {
        cmdTypes = new HashMap<Class<?>, String>();
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_COMMANDS, COMMAND_PATH);
    }

    public static Commands getCommands() {
        return canonicalize(COMMANDS);
    }

    public static void register(String moduleName, Class<?> cmdType) {
        getCommands().add(moduleName, cmdType);
    }

    /**
     * 添加命令类型
     * 
     * @param moduleName
     * @param cmdType
     */
    public void add(String moduleName, Class<?> cmdType) {
        if (!Command.class.isAssignableFrom(cmdType)) {
            throw new BrickException("Invalid command " + cmdType);
        }
        String oldModuleName = cmdTypes.put(cmdType, moduleName);
        if (oldModuleName == null) {
            LOG.info("Binded the command to module [{}] with {}", moduleName,
                    cmdType);
        } else {
            LOG.info("Unbinded the command from module [{}] with {}",
                    oldModuleName, cmdType);
            LOG.info("Binded the command to module [{}] with {}", moduleName,
                    cmdType);
        }
    }

    /**
     * 获取模块名称
     * 
     * @return
     */
    public static String getModuleName() {
        return getModuleName(Context.getActionCommand());
    }

    public static String getModuleName(Command cmd) {
        if (cmd == null) {
            throw new BrickException("The command can not a null value");
        }
        String moduleName = cmd.getModuleName();
        Class<?> cmdType = cmd.getClass();
        if (cmdType == Command.class) {
            return moduleName;
        }
        return moduleName == null ? getModuleName(cmdType) : moduleName;
    }

    /**
     * 获取模块名
     * 
     * @param cmdType
     * @return
     */
    public static String getModuleName(Class<?> cmdType) {
        if (!Command.class.isAssignableFrom(cmdType)
                || Command.class == cmdType) {
            return null;
        }
        Commands cmds = getCommands();
        String moduleName = cmds.cmdTypes.get(cmdType);
        if (moduleName != null) {
            return moduleName;
        }
        return getModuleName(cmdType.getSuperclass());
    }

}
