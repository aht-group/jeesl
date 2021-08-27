package net.sf.ahtutils.report.revert.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface ImportStrategyInfo {
    
    public String name();
    public String description();
    public int steps();
}
