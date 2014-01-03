package net.isger.brick.test.bean;

import net.isger.brick.util.anno.Alias;

@Alias("t_test")
public class TestBean {

    private String id;

    private String name;

    public TestBean() {
    }

    public TestBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
