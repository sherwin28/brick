package net.isger.brick.core;

import net.isger.brick.util.Loader;

public abstract class AbstractModule implements Module {

    protected Loader loader;

    /** TODO 注入存在缺陷 */
    protected Operaters operaters;

    protected AbstractModule() {
        operaters = new Operaters(this);
    }

    public void load(Object res) {
        this.loader.load(res);
    }

    public Operaters getOperaters() {
        return this.operaters;
    }

    public void initial() {
    }

    public final void execute() {
        Command cmd = Context.getActionCommand();
        String domain = cmd.getDomain(); // 获取域
        if (domain == null) {
            String operate = cmd.getOperate(); // 获取操作名称
            Operater operater = operaters.get(operate);
            if (operater == null) {
                operate();
            } else {
                operater.operate();
            }
        } else {
            this.call();
        }
    }

    protected void operate() {
    }

    protected void call() {
    }

    public void destroy() {
    }

}
