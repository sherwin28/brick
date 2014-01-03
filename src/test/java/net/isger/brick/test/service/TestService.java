package net.isger.brick.test.service;

import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.plugin.service.BaseService;

public class TestService extends BaseService {

    public void test() {
        PluginCommand cmd = PluginCommand.getAction();

        System.out.println("TestService.test(): " + cmd.getParameter("test"));

        toPersist();
        System.out.println("TestService.test(): " + cmd.getResult());

        cmd.setDirect("chain");
        toPersist();
        System.out.println("TestService.test(): " + cmd.getResult());

        cmd.setTarget("chain");
        toPersist();
        System.out.println("TestService.test(): " + cmd.getResult());
    }

}
