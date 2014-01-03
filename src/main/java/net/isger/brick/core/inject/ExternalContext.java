package net.isger.brick.core.inject;

import java.lang.reflect.Member;

public class ExternalContext<T> {

    public ExternalContext(Member member, Key<T> key, Container container) {
        // TODO Auto-generated constructor stub
    }

    public static <T> ExternalContext<T> newInstance(Member member, Key<T> key,
            Container container) {
        return new ExternalContext<T>(member, key, container);
    }
}
