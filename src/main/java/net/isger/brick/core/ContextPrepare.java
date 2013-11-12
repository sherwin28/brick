package net.isger.brick.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文预处理
 * 
 * @author issing
 * 
 */
public class ContextPrepare implements Prepare {

    public void prepare(Command cmd) {
        Context context;
        Context oldContext = Context.getActionContext();
        if (oldContext != null) {
            context = new Context(new HashMap<String, Object>(
                    oldContext.getContextMap()));
        } else {
            context = createContext(cmd);
        }
        Context.setContext(context);
    }

    private Context createContext(Command cmd) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(Context.COMMAND, cmd);
        return new Context(context);
    }

    public void cleanup(Command cmd) {
        Context.setContext(null);
    }

}
