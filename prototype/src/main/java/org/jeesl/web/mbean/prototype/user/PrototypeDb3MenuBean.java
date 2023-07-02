package org.jeesl.web.mbean.prototype.user;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslMenuBean;
import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class PrototypeDb3MenuBean <L extends JeeslLang, D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									CTX extends JeeslSecurityContext<L,D>,
									M extends JeeslSecurityMenu<L,V,CTX,M>,
									USER extends JeeslUser<R>,
									I extends JeeslIdentity<R,V,U,A,CTX,USER>>
						implements JeeslMenuBean<V,CTX,M>
{
	final static Logger logger = LoggerFactory.getLogger(PrototypeDb3MenuBean.class);
	private static final long serialVersionUID = 1L;

	private JeeslSecurityBean<C,R,V,U,A,?,CTX,M,USER> bSecurity;
	
	private final LoadingCache<String,List<M>> cacheSub;
	private final LoadingCache<String,List<M>> cacheBreadcrumb;
	
	private final List<M> mainMenu; public List<M> getMainMenu() {return mainMenu;}

	private I identity;
	private CTX context;
	
	private boolean setupRequired=false;
	private boolean debugOnInfo; protected void setDebugOnInfo(boolean log) {debugOnInfo = log;}

	public PrototypeDb3MenuBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity)
	{				
		cacheSub = Caffeine.newBuilder()
			       .maximumSize(100)
			       .expireAfterWrite(Duration.ofMinutes(15))
//			       .removalListener((String key, String graph, RemovalCause cause) -> System.out.printf("Key %s was removed (%s)%n", key, cause))
			       .build(key -> buildSub(key));
		
		cacheBreadcrumb = Caffeine.newBuilder()
			       .maximumSize(100)
			       .expireAfterWrite(Duration.ofMinutes(15))
//			       .removalListener((String key, String graph, RemovalCause cause) -> System.out.printf("Key %s was removed (%s)%n", key, cause))
			       .build(key -> buildBreadcrumb(key));

		mainMenu = new ArrayList<>();

		debugOnInfo = false;
		setupRequired = true;
	}
	
	public void postConstructMenu(JeeslSecurityBean<C,R,V,U,A,?,CTX,M,USER> bSecurity, I identity)
	{
		this.bSecurity=bSecurity;
		this.context=identity.getContext();
		
		if(bSecurity==null)
		{
			logger.error("Implementation for a empty bSecurity is deprecated");
		}
		
		prepare(identity);
	}
	
	public List<M> subMenu(String key) {return cacheSub.get(key);}
	private List<M> buildSub(String viewCode)
	{
		if(debugOnInfo) {logger.info("Generating buildSub for ("+viewCode+") withContext:"+context+" setup:"+setupRequired);}
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
		return list;
	}
	
	public List<M> breadcrumb(String key) {return cacheBreadcrumb.get(key);}
	private List<M> buildBreadcrumb(String key)
	{
		if(debugOnInfo) {logger.info("Generating buildBreadcrumb for ("+key+") setup:"+setupRequired);}
		List<M> list = new ArrayList<>();
		if(bSecurity==null) {logger.error("Implementation for a empty bSecurity is not forseen"); return list;}
		
		for(M m : bSecurity.getAllMenus(context))
		{
			if(m.getView().getCode().equals(key))
			{
				traverseParent(list,m);
			}
		}
		
		Collections.reverse(list);
		return list;
	}
	
	private void traverseParent(List<M> list, M m)
	{
		list.add(m);
		if(m.getParent()!=null) {traverseParent(list,m.getParent());}
	}
	
	public void updateLocale(String localeCode) {}

	public void prepare(I identity)
	{
		this.identity=identity;
		reset();
	}

	public void reset()
	{
		if(debugOnInfo) {logger.info("Resettings Menu");}

		mainMenu.clear();
		for(M m : bSecurity.getRootMenus(context)) {if(this.userHasAccessTo(m)) {mainMenu.add(m);}}
		
		cacheSub.invalidateAll();
		cacheSub.cleanUp();
		
		cacheBreadcrumb.invalidateAll();
		cacheBreadcrumb.cleanUp();
		
		setupRequired = true;
	}

	private boolean userHasAccessTo(M m)
	{
		boolean visible = m.getView().isVisible() && (m.getView().getAccessPublic() || (identity.isLoggedIn() && (m.getView().getAccessLogin() || identity.hasView(m.getView()))));
		boolean developer = identity.getRoleCodeWithAccessToAllPages()!=null && identity.hasRole(identity.getRoleCodeWithAccessToAllPages());
		return (visible || developer);
	}
}