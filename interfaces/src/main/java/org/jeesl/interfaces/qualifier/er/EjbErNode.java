package org.jeesl.interfaces.qualifier.er;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface EjbErNode
{
   String name() default "";
   String category() default "";
   int level() default 0;
   String subset() default "";
}