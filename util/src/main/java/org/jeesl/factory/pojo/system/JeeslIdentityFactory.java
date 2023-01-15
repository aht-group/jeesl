package org.jeesl.factory.pojo.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIdentityFactory <I extends JeeslIdentity<R,V,U,A,CTX,USER>,
								   R extends JeeslSecurityRole<?,?,?,V,U,A,USER>,
								   V extends JeeslSecurityView<?,?,?,R,U,A>,
								   U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
								   A extends JeeslSecurityAction<?,?,R,V,U,?>,
								   CTX extends JeeslSecurityContext<?,?>,
								   USER extends JeeslUser<R>>
{

	final static Logger logger = LoggerFactory.getLogger(JeeslIdentityFactory.class);
	
	private boolean debugOnInfo; public boolean isDebugOnInfo() {return debugOnInfo;} public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	
	private final SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity;
	private JeeslLogger jogger; public JeeslLogger getJogger() {return jogger;} public void setJogger(JeeslLogger jogger) {this.jogger = jogger;}

	final Class<I>  cIdentity;

	public JeeslIdentityFactory(SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity,final Class<I> cIdentity)
	{
		this.fbSecurity=fbSecurity;
		this.cIdentity=cIdentity;
		debugOnInfo = false;
	} 

	public static <I extends JeeslIdentity<R,V,U,A,CTX,USER>,
	   			   R extends JeeslSecurityRole<?,?,?,V,U,A,USER>,
	   			   V extends JeeslSecurityView<?,?,?,R,U,A>,
	   			   U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
	   			   A extends JeeslSecurityAction<?,?,R,V,U,?>,
	   			   CTX extends JeeslSecurityContext<?,?>,
	   			   USER extends JeeslUser<R>>
		JeeslIdentityFactory<I,R,V,U,A,CTX,USER> factory(SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity,final Class<I> cIdentity)
	{
		return new JeeslIdentityFactory<>(fbSecurity,cIdentity);
	}
	
	public I build(CTX context)
	{	
		if(context==null) {logger.warn("Context is null, you will get into some trouble with Menu features ...");}
		
		I identity = null;
		try
		{
			identity = cIdentity.newInstance();
			identity.setContext(context);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return identity;
	}

	public I build(JeeslSecurityFacade<?,?,?,R,V,U,A,?,?,?,USER> fSecurity, JeeslSecurityBean<?,?,?,R,V,U,A,?,?,?,?,USER> bSecurity, USER user, CTX context)
	{		
		I identity = null;
		try
		{
			identity = this.build(context);
			if(identity==null) {throw new InstantiationException("Object can not be build");}
			identity.setUser(user);
			
			List<R> roles = fSecurity.allRolesForUser(user);
			for(R r : roles){identity.allowRole(r);}
			if(jogger!=null) {jogger.milestone(fbSecurity.getClassRole().getSimpleName(), null, roles.size());}
			
			if(bSecurity==null) {processViews(fSecurity,user,identity);}
			else {processViews(bSecurity,roles,identity);}
			
			if(bSecurity==null) {processActions(fSecurity,user,identity);}
			else {processActions(bSecurity,roles,identity);}
			if(jogger!=null) {jogger.milestone(fbSecurity.getClassAction().getSimpleName(), null, identity.sizeAllowedActions());}
			
		}
		catch (InstantiationException e) {e.printStackTrace();}
		
		return identity;
	}
	
	private void processViews(JeeslSecurityFacade<?,?,?,R,V,U,A,?,?,?,USER> fSecurity, USER user, I identity)
	{
		List<V> views = fSecurity.allViewsForUser(user);
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),JeeslSecurityFacade.class.getSimpleName(),views.size());}
		for(V v : views){identity.allowView(v);}
	}
	private void processViews(JeeslSecurityBean<?,?,?,R,V,U,A,?,?,?,?,USER> bSecurity, List<R> roles, I identity)
	{	
		Map<Long,V> map = new HashMap<Long,V>();
		for(R role : roles)
		{
			for(V rView : bSecurity.fViews(role))
			{
				map.put(rView.getId(), rView);
			}
			for(U u : bSecurity.fUsecases(role))
			{
				for(V uView : bSecurity.fViews(u))
				{
					map.put(uView.getId(), uView);
				}
			}
		}
		List<V> views = new ArrayList<V>(map.values());
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),JeeslSecurityBean.class.getSimpleName(),views.size());}
		
		for(V v : views){identity.allowView(v);}
	}
	
	private void processActions(JeeslSecurityFacade<?,?,?,R,V,U,A,?,?,?,USER> fSecurity, USER user, I identity)
	{
		for(A a : fSecurity.allActionsForUser(user)){identity.allowAction(a);}
	}
	private void processActions(JeeslSecurityBean<?,?,?,R,V,U,A,?,?,?,?,USER> bSecurity, List<R> roles, I identity)
	{
		Map<Long,A> actions = new HashMap<Long,A>();
		for(R r : roles)
		{
			for(A rAction : bSecurity.fActions(r))
			{
				actions.put(rAction.getId(), rAction);
			}
			for(U u : bSecurity.fUsecases(r))
			{
				for(A uAction : bSecurity.fActions(u))
				{
					actions.put(uAction.getId(), uAction);
				}
			}
		}
		for(A a : actions.values()){identity.allowAction(a);}
	}
}