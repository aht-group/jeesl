package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityContextFactory <CTX extends JeeslSecurityContext<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityContextFactory.class);
	
    private final Class<CTX> cCtx;
    
    public EjbSecurityContextFactory(final Class<CTX> cCtx)
    {
        this.cCtx = cCtx;
    } 
    
    public CTX build()
    {
    	CTX ejb = null;
    	
    	try
    	{
			ejb = cCtx.getDeclaredConstructor().newInstance();
			ejb.setPosition(1);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public void ejb2nnb(CTX ctx, NullNumberBinder nnb)
	{
    	nnb.longToA(ctx.getContextId());
	}
	public void nnb2ejb(CTX ctx, NullNumberBinder nnb)
	{
		ctx.setContextId(nnb.aToLong());
	}
}