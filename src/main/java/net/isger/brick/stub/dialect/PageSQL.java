package net.isger.brick.stub.dialect;

public interface PageSQL {

    public String getWrapSQL();

    public Object[] getWrapValues();

    public String getCountSQL();

}
