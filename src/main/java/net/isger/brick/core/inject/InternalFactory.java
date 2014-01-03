package net.isger.brick.core.inject;

interface InternalFactory<T> {

    public T create(InternalContext context);

}
