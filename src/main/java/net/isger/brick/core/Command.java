package net.isger.brick.core;

import java.io.Serializable;
import java.util.Map;

/**
 * 命令
 * 
 * @author issing
 * 
 */
public interface Command extends Serializable {

    public boolean isBypass();

    public String getModuleName();

    public String getDomain();

    public String getOperate();

    public Object getHeader(String name);

    public Object getParameter(String name);

    public Map<String, Object> getParameters();

    public Object getFooter(String name);

    public Object getResult();

    public void setBypass(boolean bypass);

    public void setModuleName(String moduleName);

    public void setDomain(String domain);

    public void setOperate(String operate);

    public void setHeader(String name, Object value);

    public void setParameter(String name, Object value);

    public void setParameters(Map<String, Object> params);

    public void setFooter(String name, Object value);

    public void setResult(Object result);

}
