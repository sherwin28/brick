package net.isger.brick.core;

import java.util.Map;

public class CommandShell implements Command {

    private static final long serialVersionUID = 598214239863897067L;

    private Command cmd;

    protected CommandShell() {
        this(null);
    }

    protected CommandShell(Command cmd) {
        this.cmd = cmd == null ? new BaseCommand() : cmd;
    }

    public String getModuleName() {
        return cmd.getModuleName();
    }

    public void setModuleName(String moduleName) {
        cmd.setModuleName(moduleName);
    }

    public boolean isBypass() {
        return cmd.isBypass();
    }

    public void setBypass(boolean bypass) {
        cmd.setBypass(bypass);
    }

    public String getDomain() {
        return cmd.getDomain();
    }

    public void setDomain(String domain) {
        cmd.setDomain(domain);
    }

    public String getOperate() {
        return cmd.getOperate();
    }

    public void setOperate(String operate) {
        cmd.setOperate(operate);
    }

    public Object getHeader(String name) {
        return cmd.getHeader(name);
    }

    public void setHeader(String name, Object value) {
        cmd.setHeader(name, value);
    }

    public Object getParameter(String name) {
        return cmd.getParameter(name);
    }

    public void setParameter(String name, Object value) {
        cmd.setParameter(name, value);
    }

    public Map<String, Object> getParameters() {
        return cmd.getParameters();
    }

    public void setParameters(Map<String, Object> params) {
        cmd.setParameters(params);
    }

    public Object getFooter(String name) {
        return cmd.getFooter(name);
    }

    public void setFooter(String name, Object value) {
        cmd.setFooter(name, value);
    }

    public Object getResult() {
        return cmd.getResult();
    }

    public void setResult(Object result) {
        cmd.setResult(result);
    }

}
