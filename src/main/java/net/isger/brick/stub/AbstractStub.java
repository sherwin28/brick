package net.isger.brick.stub;

import java.util.HashMap;
import java.util.Map;

public class AbstractStub implements Stub {

    private Map<String, Object> parameters;

    protected AbstractStub() {
        parameters = new HashMap<String, Object>();
    }

    protected Object getParameter(String name) {
        return parameters.get(name);
    }

    public void initial() {
    }

    public void insert() {
    }

    public void delete() {
    }

    public void update() {
    }

    public void search() {
    }

    public void destroy() {
    }

}
