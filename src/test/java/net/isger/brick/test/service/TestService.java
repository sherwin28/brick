package net.isger.brick.test.service;

import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.plugin.service.BaseService;

public class TestService extends BaseService {

    public void test() {
        PluginCommand cmd = PluginCommand.getAction();

        System.out.println("TestService: " + cmd.getParameter("test"));

        toPersist();
        System.out.println(cmd.getResult());

        cmd.setOperate("Chain");
        cmd.setDirect("chain");
        toPersist();
        System.out.println(cmd.getResult());
    }

}
