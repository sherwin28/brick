package net.isger.brick;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.isger.brick.core.Console;
import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.stub.StubCommand;
import net.isger.brick.test.bean.TestBean;

public class BrickTest extends TestCase {

    public BrickTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(BrickTest.class);
    }

    public void testBrick() {
        PluginCommand pcmd = new PluginCommand();
        pcmd.setDomain("test");
        pcmd.setDirect("test");
        pcmd.setTarget("test");
        pcmd.setParameter("test", "this is test.");
        Console.execute(pcmd);
        StubCommand scmd = StubCommand.cast(pcmd);
        scmd.setTable(new TestBean());
        scmd.setOperate(StubCommand.OPERATE_SEARCH);
        System.out.println(Console.execute(scmd));
        assertTrue(true);
    }

}
