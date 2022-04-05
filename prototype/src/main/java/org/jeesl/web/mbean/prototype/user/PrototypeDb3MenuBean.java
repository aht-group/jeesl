package org.jeesl.web.mbean.prototype.user;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslMenuBean;
import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
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
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import net.sf.exlp.util.io.StringUtil;

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
									I extends JeeslIdentity<R,V,U,A,USER>>
		implements Serializable,JeeslMenuBean<V,CTX,M>
{
	final static Logger logger = LoggerFactory.getLogger(PrototypeDb3MenuBean.class);
	private static final long serialVersionUID = 1L;

	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity;
	private JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity;
	private JeeslSecurityBean<L,D,C,R,V,U,A,AT,?,CTX,M,USER> bSecurity;

	private final EjbSecurityMenuFactory<V,CTX,M> efMenu;
	private final PositionComparator<M> cpMenu;

	private final Map<String,M> mapRoot; public Map<String,M> getMapRoot() {return mapRoot;}
	private final Set<Long> setAllowed;
	
	private final LoadingCache<String,List<M>> cacheSub;
	private final LoadingCache<String,List<M>> cacheBreadcrumb;
	
	private final List<M> mainMenu; public List<M> getMainMenu() {if(setupRequired) {this.setup();} return mainMenu;}

	private I identity;
	private CTX context;
	
	private boolean setupRequired=false;
	private boolean debugOnInfo; protected void setDebugOnInfo(boolean log) {debugOnInfo = log;}

	public PrototypeDb3MenuBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity)
	{
		this.fbSecurity=fbSecurity;
		
		efMenu = fbSecurity.ejbMenu();
		cpMenu = new PositionComparator<M>();
		
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
	
		
		mapRoot = new HashMap<>();
		setAllowed = new HashSet<>();
		
		mainMenu = new ArrayList<>();

		debugOnInfo = false;
		setupRequired = true;
	}
	
	public void postConstructMenu(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity,
									JeeslSecurityBean<L,D,C,R,V,U,A,AT,?,CTX,M,USER> bSecurity,
									CTX context, I identity)
	{
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		this.context=context;
		prepare(identity);
	}
	
	public List<M> subMenu(String key)
	{
//		if(debugOnInfo) {logger.info("SubMenu "+key);}
		return cacheSub.get(key);
	}
	private List<M> buildSub(String key)
	{
		boolean withContext = context!=null;
		if(debugOnInfo) {logger.info("Generating buildSub for ("+key+") withContext:"+context+" setup:"+setupRequired);}
		if(setupRequired) {this.setup();}
		List<M> list = new ArrayList<>();
		if(bSecurity==null)
		{
			logger.error("Implementation for a empty bSecurity is not forseen");
			return list;
		}
		
		if(withContext)
		{
			list.addAll(bSecurity.getMenus()
				.stream()
				.filter(m -> m.getContext().equals(context) && m.getParent()!=null && m.getParent().getView().getCode().equals(key) && setAllowed.contains(m.getId()))
				.collect(Collectors.toList()));
		}
		else
		{
			list.addAll(bSecurity.getMenus()
				.stream()
				.filter(m -> m.getParent()!=null && m.getParent().getView().getCode().equals(key) && setAllowed.contains(m.getId()))
				.collect(Collectors.toList()));
		}
		logger.info("Key: "+key+" list "+list.size());
		Collections.sort(list,cpMenu);
		return list;
	}
	
	public List<M> breadcrumb(String key)
	{
//		if(debugOnInfo) {logger.info("Requesting breadcrum for ("+key+")");}
		return cacheBreadcrumb.get(key);
	}
	private List<M> buildBreadcrumb(String key)
	{
		if(debugOnInfo) {logger.info("Generating buildBreadcrumb for ("+key+") setup:"+setupRequired);}
		if(setupRequired) {this.setup();}
		List<M> list = new ArrayList<>();
		if(bSecurity==null)
		{
			logger.error("Implementation for a empty bSecurity is not forseen");
			return list;
		}
		
		if(context==null)
		{
			for(M m : bSecurity.getMenus())
			{
				if(m.getView().getCode().equals(key))
				{
					traverseParent(list,m);
				}
			}
		}
		else
		{
			for(M m : bSecurity.getMenus())
			{
				if(m.getContext().equals(context) && m.getView().getCode().equals(key))
				{
					traverseParent(list,m);
				}
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

	public void reset()
	{
		if(debugOnInfo) {logger.info("Resettings Menu");}
		mapRoot.clear();
		setAllowed.clear();
		mainMenu.clear();
		
		cacheSub.invalidateAll();
		cacheSub.cleanUp();
		
		cacheBreadcrumb.invalidateAll();
		cacheBreadcrumb.cleanUp();
		
		setupRequired = true;
	}
	
	public void prepare(I identity)
	{
		this.identity=identity;
		reset();
	}

	private synchronized void setup()
	{
		if(setupRequired)
		{
			if(debugOnInfo)
			{
				logger.info(StringUtil.stars());
				logger.info("Setup Menu");
				logger.info("\tUsing "+JeeslSecurityBean.class.getSimpleName()+" "+(bSecurity!=null));
				logger.info("\tUsing "+fbSecurity.getClassContext().getSimpleName()+" "+(context!=null));
			}
			
			List<M> list = new ArrayList<>();
			if(context!=null)
			{
				if(bSecurity!=null) {list.addAll(bSecurity.getMenus().stream().filter(m -> m.getContext().equals(context)).collect(Collectors.toList()));}
				else {list.addAll(fSecurity.allForParent(fbSecurity.getClassMenu(),JeeslSecurityMenu.Attributes.context,context));}
				if(debugOnInfo) {logger.info("\t"+fbSecurity.getClassMenu().getSimpleName()+": "+list.size()+" in context "+context.getCode());}
			}
			else
			{
				if(bSecurity!=null) {list.addAll(bSecurity.getMenus());}
				else {list.addAll(fSecurity.all(fbSecurity.getClassMenu()));}
				if(debugOnInfo) {logger.info("\t"+fbSecurity.getClassMenu().getSimpleName()+": "+list.size());}
			}
			Collections.sort(list,cpMenu);
			
			setAllowed.clear();
			for(M m : list)
			{
				mapRoot.put(m.getView().getCode(),efMenu.toRoot(m));

				boolean visible = m.getView().isVisible() && (m.getView().getAccessPublic() || (identity.isLoggedIn() && (m.getView().getAccessLogin() || identity.hasView(m.getView()))));
				boolean developer = identity.getRoleCodeWithAccessToAllPages()!=null && identity.hasRole(identity.getRoleCodeWithAccessToAllPages());
				
				if(debugOnInfo) {logger.info("\t\t"+m.getView().getCode()+" visible:"+visible+" developer:"+developer);}
				if(visible || developer)
				{	
					setAllowed.add(m.getId());
					if(m.getParent()==null) {mainMenu.add(m);}
				}
			}
			logger.info("\tAllowd Pages "+setAllowed.size());
			setupRequired = false;
		}
	}
}