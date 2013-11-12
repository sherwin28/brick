package net.isger.brick.plugin;

import net.isger.brick.BrickConstants;
import net.isger.brick.core.Command;
import net.isger.brick.core.CommandShell;
import net.isger.brick.core.Commands;
import net.isger.brick.core.Context;

public class PluginCommand extends CommandShell {

    private static final long serialVersionUID = 8646247875555696968L;

    public static final String OPERATE_SERVICE = "service";

    public static final String OPERATE_PERSIST = "persist";

    public static final String HEADER_DIRECT = "plugin-direct";

    public static final String HEADER_TARGET = "plugin-target";

    static {
        Commands.register(BrickConstants.MODULE_PLUGIN, PluginCommand.class);
    }

    public PluginCommand() {
    }

    public PluginCommand(Command cmd) {
        super(cmd);
    }

    public static PluginCommand getAction() {
        Command cmd = Context.getActionCommand();
        return cmd instanceof PluginCommand ? (PluginCommand) cmd
                : new PluginCommand(cmd);
    }

    public String getDirect() {
        return getDirect(this);
    }

    public void setDirect(String direct) {
        setDirect(this, direct);
    }

    public static String getActionDirect() {
        return getDirect(Context.getActionCommand());
    }

    public static void setActionDirect(String direct) {
        setDirect(Context.getActionCommand(), direct);
    }

    public static String getDirect(Command cmd) {
        return (String) cmd.getHeader(HEADER_DIRECT);
    }

    private static void setDirect(Command cmd, String direct) {
        cmd.setHeader(HEADER_DIRECT, direct);
    }

    public String getTarget() {
        return getTarget(this);
    }

    public void setTarget(String target) {
        setTarget(this, target);
    }

    public static String getActionTarget() {
        return getTarget(Context.getActionCommand());
    }

    public static void setActionTarget(String target) {
        setTarget(Context.getActionCommand(), target);
    }

    public static String getTarget(Command cmd) {
        return (String) cmd.getHeader(HEADER_TARGET);
    }

    private static void setTarget(Command cmd, String target) {
        cmd.setHeader(HEADER_TARGET, target);
    }

}
