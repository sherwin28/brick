package net.isger.brick.plugin.persist;

import net.isger.brick.plugin.DynamicTarget;
import net.isger.brick.util.anno.Ignore;

@Ignore
public class BasePersist extends DynamicTarget implements Persist {

    protected String stub;

    protected BasePersist() {
    }

    public void persist() {
        invoke();
    }

}
