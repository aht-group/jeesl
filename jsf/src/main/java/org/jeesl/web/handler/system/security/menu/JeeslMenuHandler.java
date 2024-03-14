package org.jeesl.web.handler.system.security.menu;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.cache.JeeslMenuCache;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.txt.system.security.user.TxtIdentityFactory;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMenuHandler <V extends JeeslSecurityView<?,?,?,?,?,?>,
									CTX extends JeeslSecurityContext<?,?>,
									M extends JeeslSecurityMenu<?,V,CTX,M>,
									USER extends JeeslUser<?>,
									I extends JeeslIdentity<?,V,?,?,CTX,USER>>
						implements Serializable//JeeslMenuBean<V,CTX,M>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMenuHandler.class);
	private static final long serialVersionUID = 1L;

	private JeeslSecurityBean<?,?,?,?,?,CTX,M,?> bSecurity;
	
	private final List<M> mainMenu;
	
	private I identity;
	private long session;
	private CTX context;
	private boolean debugOnInfo; //protected void setDebugOnInfo(boolean log) {debugOnInfo = log;}
	public JeeslMenuHandler<V,CTX,M,USER,I> jogger() {this.debugOnInfo=true; return this;}

	public static <V extends JeeslSecurityView<?,?,?,?,?,?>,CTX extends JeeslSecurityContext<?,?>,M extends JeeslSecurityMenu<?,V,CTX,M>,USER extends JeeslUser<?>,I extends JeeslIdentity<?,V,?,?,CTX,USER>>
		JeeslMenuHandler<V,CTX,M,USER,I> instance() {return new JeeslMenuHandler<>();}
	
	public JeeslMenuHandler()
	{				
		mainMenu = new ArrayList<>();
		debugOnInfo = false;
	}
	
	public void postConstructMenu(JeeslSecurityBean<?,?,?,?,?,CTX,M,?> bSecurity, I identity)
	{
		this.bSecurity = bSecurity;
		this.context = identity.getContext();
		
		this.prepare(identity);
	}
	
	public void prepare(I identity)
	{
		session = OffsetDateTime.now().toEpochSecond();
		if(Objects.isNull(identity)) {logger.error("Preparing(identity), but the provided value is null");}
		else if(debugOnInfo) {logger.info("Preparing(identity) "+identity.toString());}
		this.identity = identity;
		this.reset();
	}
	
	public void reset()
	{
		mainMenu.clear();
		for(M m : bSecurity.getRootMenus(context)) {if(BooleanComparator.active(m.getVisible()) && this.userHasAccessTo(m)) {mainMenu.add(m);}}
	}
	
	public List<M> getMainMenu()
	{
		if(debugOnInfo) {logger.info("Requesting main-menu with size "+mainMenu.size());}
		return mainMenu;
	}
	
	public List<M> subMenu(JeeslMenuCache<M> cache, String viewCode)
	{
		if(Objects.isNull(identity)) {logger.error("subMenu, but identity is null!");}
		
		String cacheKey = TxtIdentityFactory.key("sub",identity,viewCode,session);
		if(cache.cacheContains(cacheKey)) {return cache.cacheGet(cacheKey);}
		else
		{
			if(debugOnInfo) {logger.info("Generating buildSub for ("+viewCode+") withContext:"+context);}
			List<M> tmp = new ArrayList<>();
			tmp.addAll(bSecurity.getAllMenus(context)
					.stream()
					.filter(m -> m.getParent()!=null && m.getParent().getView().getCode().equals(viewCode))
					.collect(Collectors.toList()));

			List<M> list = new ArrayList<>();
			for(M m : tmp)
			{
				if(userHasAccessTo(m) && (m.getVisible()==null || m.getVisible()))
				{	
					list.add(m);
				}
			}
			cache.cachePut(cacheKey, list);
			return list;
		}
	}
	
	public List<M> breadcrumb(JeeslMenuCache<M> cache, String viewCode)
	{
		if(Objects.isNull(identity)) {logger.error("breadcrumb, but identity is null!");}
		
		String cacheKey = TxtIdentityFactory.key("crumb",identity,viewCode,session);
		if(cache.cacheContains(cacheKey)){return cache.cacheGet(cacheKey);}
		else
		{
			List<M> list = new ArrayList<>();
			for(M m : bSecurity.getAllMenus(context))
			{
				if(m.getView().getCode().equals(viewCode))
				{
					traverseParent(list,m);
				}
			}
			Collections.reverse(list);
			cache.cachePut(cacheKey, list);
			return list;
		}
	}
	private void traverseParent(List<M> list, M m)
	{
		list.add(m);
		if(m.getParent()!=null) {traverseParent(list,m.getParent());}
	}

	public boolean userHasAccessTo(M m)
	{
		boolean visible = m.getView().isVisible() && (m.getView().getAccessPublic() || (identity.isLoggedIn() && (m.getView().getAccessLogin() || identity.hasView(m.getView()))));
		boolean developer = identity.getRoleCodeWithAccessToAllPages()!=null && identity.hasRole(identity.getRoleCodeWithAccessToAllPages());
		return (visible || developer);
	}
}