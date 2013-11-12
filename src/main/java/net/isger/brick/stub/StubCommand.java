package net.isger.brick.stub;

import net.isger.brick.BrickConstants;
import net.isger.brick.core.Command;
import net.isger.brick.core.CommandShell;
import net.isger.brick.core.Commands;
import net.isger.brick.core.Context;

public class StubCommand extends CommandShell {

    private static final long serialVersionUID = 8646247875555696968L;

    public static final String OPERATE_INSERT = "insert";

    public static final String OPERATE_DELETE = "delete";

    public static final String OPERATE_UPDATE = "update";

    public static final String OPERATE_SEARCH = "search";

    public static final String PARAM_TABLE = "table";

    public static final String PARAM_CONDITION = "condition";

    static {
        Commands.register(BrickConstants.MODULE_STUB, StubCommand.class);
    }

    public StubCommand() {
    }

    public StubCommand(Command cmd) {
        super(cmd);
    }

    public static StubCommand getAction() {
        return cast(Context.getActionCommand());
    }

    public static StubCommand cast(Command cmd) {
        return cmd instanceof StubCommand ? (StubCommand) cmd
                : new StubCommand(cmd);
    }

    public Object getTable() {
        return getTable(this);
    }

    public void setTable(Object table) {
        setTable(this, table);
    }

    public static Object getActionTable() {
        return getTable(Context.getActionCommand());
    }

    public static void setActionTable(Object table) {
        setTable(Context.getActionCommand(), table);
    }

    public static Object getTable(Command cmd) {
        return cmd.getParameter(PARAM_TABLE);
    }

    private static void setTable(Command cmd, Object table) {
        cmd.setParameter(PARAM_TABLE, table);
    }

    public Object getCondition() {
        return getCondition(this);
    }

    public void setCondition(Object condition) {
        setCondition(this, condition);
    }

    public static Object getActionCondition() {
        return getCondition(Context.getActionCommand());
    }

    public static void setActionCondition(Object condition) {
        setCondition(Context.getActionCommand(), condition);
    }

    public static Object getCondition(Command cmd) {
        return cmd.getParameter(PARAM_CONDITION);
    }

    private static void setCondition(Command cmd, Object condition) {
        cmd.setParameter(PARAM_CONDITION, condition);
    }

}
