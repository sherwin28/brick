package net.isger.brick.test.persist;

import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.plugin.persist.BasePersist;

public class ChainPersist extends BasePersist {

    public void test() {
        PluginCommand cmd = PluginCommand.getAction();

        System.out.println("TestChainPersist.test(): "
                + cmd.getParameter("test"));

        cmd.setResult("ChainPersist.test.result");
    }

    public void chain() {
        PluginCommand cmd = PluginCommand.getAction();

        System.out.println("TestChainPersist.chain(): "
                + cmd.getParameter("test"));

        cmd.setResult("ChainPersist.chain.result");
    }

}
