package net.isger.brick.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令
 * 
 * @author issing
 * 
 */
public class DefaultCommand implements Command {

    private static final long serialVersionUID = 5645749653780853461L;

    private String moduleName;

    private boolean bypass;

    private String domain;

    private String operate;

    private Map<String, Object> headers;

    private Map<String, Object> parameters;

    private Map<String, Object> footers;

    private Object result;

    public DefaultCommand() {
        headers = new HashMap<String, Object>();
        parameters = new HashMap<String, Object>();
        footers = new HashMap<String, Object>();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Object getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, Object value) {
        headers.put(name, value);
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }

    public void setParameter(String name, Object value) {
        parameters.put(name, value);
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, Object> params) {
        this.parameters.putAll(params);
    }

    public Object getFooter(String name) {
        return footers.get(name);
    }

    public void setFooter(String name, Object value) {
        footers.put(name, value);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
