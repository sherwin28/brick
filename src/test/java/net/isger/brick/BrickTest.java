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
        TestBean bean = new TestBean();
        bean.setId("test");
        bean.setName("hello");
        scmd.setTable(bean);
        // scmd.setOperate(StubCommand.OPERATE_INSERT);
        // Console.execute(scmd);
        scmd.setOperate(StubCommand.OPERATE_SEARCH);
        Object[] result = (Object[]) Console.execute(scmd);
        System.out.println("======================================");
        for (Object name : (Object[]) result[0]) {
            System.out.print(name + "\t\t");
        }
        System.out.println();
        System.out.println("--------------------------------------");
        for (Object[] row : (Object[][]) result[1]) {
            for (Object cell : row) {
                System.out.print(cell + "\t\t");
            }
            System.out.println();
        }
        assertTrue(true);
    }

}
