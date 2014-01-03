package net.isger.brick.test.persist;

import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.plugin.persist.BasePersist;

public class TestPersist extends BasePersist {

    public void test() {
        PluginCommand cmd = PluginCommand.getAction();

        System.out.println("TestPersist.test(): " + cmd.getParameter("test"));

        cmd.setResult("TestPersist.test.result");
    }
}
