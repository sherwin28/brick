package net.isger.brick.core.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 作用域注解
 * 
 * @author issing
 * 
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface Scoped {

    public Scope value();

}
