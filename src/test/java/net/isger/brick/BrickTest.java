package net.isger.brick;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.isger.brick.core.Console;
import net.isger.brick.core.ConsoleManager;
import net.isger.brick.core.Context;
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
        // 初始环境
        ConsoleManager manager = new ConsoleManager();
        Console console = manager.getConsole();
        Context context = new Context();
        Context.setActionContext(context);

        // 调用插件
        PluginCommand pcmd = new PluginCommand();
        pcmd.setDomain("test");
        pcmd.setDirect("test");
        pcmd.setTarget("test");
        pcmd.setParameter("test", "this is test.");
        console.execute(pcmd);

        // 调用存根
        StubCommand scmd = StubCommand.cast(pcmd);
        scmd.setTable(new TestBean("test", "this is test."));
        scmd.setOperate(StubCommand.OPERATE_SEARCH);
        Context.setActionCommand(scmd);
        console.execute();

        // 显示结果
        Object[] result = (Object[]) scmd.getResult();
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
