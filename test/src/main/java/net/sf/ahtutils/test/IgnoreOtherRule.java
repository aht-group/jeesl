package net.sf.ahtutils.test;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

public class IgnoreOtherRule implements InvocationInterceptor
{
    private String applyMethod;
    
    public IgnoreOtherRule(String applyMethod)
    {
        this.applyMethod = applyMethod;
    }
    
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable
    {
        if (applyMethod.equals(extensionContext.getTestMethod().get().getName()))
        {
            invocation.proceed();
        }
    }
    
    // register via @RegisterExtension public IgnoreOtherRule test = new IgnoreOtherRule("withoutCode");
}