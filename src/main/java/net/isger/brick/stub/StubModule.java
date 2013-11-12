package net.isger.brick.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickException;
import net.isger.brick.core.AbstractModule;
import net.isger.brick.core.Command;
import net.isger.brick.core.Context;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.SimpleLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 存根模块
 * 
 * @author issing
 * 
 */
public class StubModule extends AbstractModule {

    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(StubModule.class);
    }

    private Map<String, Stub> stubs;

    public StubModule() {
        this.stubs = new HashMap<String, Stub>();
        this.loader = new SimpleLoader(SqlStub.class) {
            @SuppressWarnings("unchecked")
            protected Object load(Map<String, Object> res) {
                List<Object> result = new ArrayList<Object>();
                Stub stub;
                String name;
                Object config;
                for (Entry<String, Object> entry : res.entrySet()) {
                    name = entry.getKey();
                    config = entry.getValue();
                    if (!(config instanceof Map)) {
                        LOG.warn("Skipped the stub invalid config {}", name);
                        continue;
                    }
                    stub = (Stub) super.load((Map<String, Object>) config);
                    add(entry.getKey(), stub);
                    result.add(stub);
                }
                return result;
            }

            protected Object create(Class<?> clazz, Map<String, Object> res) {
                if (!Stub.class.isAssignableFrom(clazz)) {
                    throw new BrickException("Invalid stub class name "
                            + clazz.getName());
                }
                return Reflects.newInstance(clazz, res);
            }

            protected Object create(Object res) {
                throw new BrickException("Unexpected load the stub by " + res);
            }

        };
    }

    public void add(String name, Stub stub) {
        Stub oldStub = stubs.put(name, stub);
        if (oldStub == null) {
            LOG.info("Binded the stub [{}] with {}", name, stub);
        } else {
            LOG.info("Multiple binded the stub [{}] with {}", name, stub);
        }
    }

    public void initial() {
        for (Stub stub : stubs.values()) {
            stub.initial();
        }
    }

    protected void call() {
        call(Context.getActionCommand());
    }

    public void call(Command cmd) {
        Context context = Context.getActionContext();
        String domain = cmd.getDomain(); // 获取域
        Stub stub = stubs.get(domain);
        if (stub == null) {
            throw new BrickException("Not found the stub by " + domain);
        }
        context.set(Context.STUB, stub);
        String operate = cmd.getOperate();
        if (operate == null || operate.equals("search")) {
            stub.search();
        } else if (operate.equals("update")) {
            stub.update();
        } else if (operate.equals("delete")) {
            stub.delete();
        } else if (operate.equals("insert")) {
            stub.insert();
        } else {
            throw new BrickException("Unexpected the stub operation " + operate);
        }
    }

    public void destroy() {
        for (Stub stub : stubs.values()) {
            stub.destroy();
        }
        stubs.clear();
    }

}
