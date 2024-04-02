package org.jeesl.factory.pojo.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIdentityFactory <I extends JeeslIdentity<R,V,U,A,CTX,USER>,
								   R extends JeeslSecurityRole<?,?,?,V,U,A>,
								   V extends JeeslSecurityView<?,?,?,R,U,A>,
								   U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
								   A extends JeeslSecurityAction<?,?,R,V,U,?>,
								   CTX extends JeeslSecurityContext<?,?>,
								   USER extends JeeslUser<R>>
								implements Serializable 
{

	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIdentityFactory.class);
	protected JeeslLogger jogger; public JeeslIdentityFactory<I,R,V,U,A,CTX,USER> jogger(JeeslLogger jogger) {this.jogger = jogger; return this;}
	
	protected JeeslSecurityFacade<?,R,V,U,A,CTX,?,USER> fSecurity;
	protected JeeslSecurityBean<R,V,U,A,?,?,?,USER> bSecurity;
	
	private final SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity;
	
	public JeeslIdentityFactory<I,R,V,U,A,CTX,USER> securityFacade(JeeslSecurityFacade<?,R,V,U,A,CTX,?,USER> fSecurity) {this.fSecurity = fSecurity; return this;}
	public JeeslIdentityFactory<I,R,V,U,A,CTX,USER> securityBean(JeeslSecurityBean<R,V,U,A,?,?,?,USER> bSecurity) {this.bSecurity = bSecurity; return this;}
	
	final Class<I>  cIdentity;
	protected boolean debugOnInfo; public boolean isDebugOnInfo() {return debugOnInfo;} public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public static <I extends JeeslIdentity<R,V,U,A,CTX,USER>,
				   R extends JeeslSecurityRole<?,?,?,V,U,A>,
				   V extends JeeslSecurityView<?,?,?,R,U,A>,
				   U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
				   A extends JeeslSecurityAction<?,?,R,V,U,?>,
				   CTX extends JeeslSecurityContext<?,?>,
				   USER extends JeeslUser<R>>
		JeeslIdentityFactory<I,R,V,U,A,CTX,USER> instance(SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity, final Class<I> cIdentity)
	{
		return new JeeslIdentityFactory<>(fbSecurity,cIdentity);
	}
	
	public JeeslIdentityFactory(SecurityFactoryBuilder<?,?,?,R,V,U,A,?,?,?,?,?,?,?,?,USER> fbSecurity, final Class<I> cIdentity)
	{
		this.fbSecurity=fbSecurity;
		this.cIdentity=cIdentity;
		debugOnInfo = false;
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

	public I build(USER user, CTX context)
	{		
		I identity = null;
		try
		{
			identity = this.build(context);
			if(identity==null) {throw new InstantiationException("Object can not be build");}
			identity.setUser(user);
			
			List<R> roles = fSecurity.allRolesForUser(user);
			
			for(R r : roles) {identity.allowRole(r);}
			if(Objects.nonNull(jogger)) {jogger.milestone(fbSecurity.getClassRole().getSimpleName(), null, roles.size());}
			
			if(Objects.nonNull(bSecurity)) {processViewsByBean(bSecurity,roles,identity);}
			else if (Objects.nonNull(fSecurity)) {processViewsByFacade(user,identity);}
			
			if(Objects.nonNull(bSecurity)) {processActionsByBean(bSecurity,roles,identity);}
			else if (Objects.nonNull(fSecurity)) {processActionByFacade(user,identity);}
			
			if(Objects.nonNull(jogger)) {jogger.milestone(fbSecurity.getClassAction().getSimpleName(), null, identity.sizeAllowedActions());}
			
		}
		catch (InstantiationException e) {e.printStackTrace();}
		
		return identity;
	}
	
	private void processViewsByFacade(USER user, I identity)
	{
		List<V> views = fSecurity.allViewsForUser(user);
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),JeeslSecurityFacade.class.getSimpleName(),views.size());}
		for(V v : views) {identity.allowView(v);}
	}
	private void processViewsByBean(JeeslSecurityBean<R,V,U,A,?,?,?,USER> bSecurity, List<R> roles, I identity)
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
		if(Objects.nonNull(jogger)) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),JeeslSecurityBean.class.getSimpleName(),views.size());}
		
		for(V v : views) {identity.allowView(v);}
	}
	
	private void processActionByFacade(USER user, I identity)
	{
		for(A a : fSecurity.allActionsForUser(user)){identity.allowAction(a);}
	}
	private void processActionsByBean(JeeslSecurityBean<R,V,U,A,?,?,?,USER> bSecurity, List<R> roles, I identity)
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