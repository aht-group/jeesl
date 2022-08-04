package org.jeesl.web.mbean.prototype.system.security;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.exlp.util.io.ObjectIO;
import net.sf.exlp.util.io.StringUtil;

public class AbstractAppSecurityBean <L extends JeeslLang,D extends JeeslDescription,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>,
											AR extends JeeslSecurityArea<L,D,V>,
											OT extends JeeslSecurityOnlineTutorial<L,D,V>,
											OH extends JeeslSecurityOnlineHelp<V,?,?>,
											USER extends JeeslUser<R>>
					implements JeeslSecurityBean<L,D,C,R,V,U,A,AT,AR,CTX,M,USER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppSecurityBean.class);
	
	protected enum JoggerLoop {loadView}
	
	protected JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity;
	protected final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;

	private final List<V> views; @Override public List<V> getViews() {return views;}
	private final List<R> roles; public List<R> getRoles() {return roles;}

	private final Map<CTX,List<M>> mapMenuAll;
	private final Map<CTX,List<M>> mapMenuRoot;
	
	private final Map<String,V> mapUrlPattern;
	private final Map<String,V> mapUrlMapping;
	private final Map<String,V> mapCodeView;
	
	private final Map<V,List<R>> mapRoles;
	private final Map<V,List<AR>> mapAreas;
	
	private final Map<R,List<V>> mapViewsByRole;
	private final Map<U,List<V>> mapViewsByUsecase;
	
	private final Map<R,List<U>> mapUsecasesByRole;
	
	private final Map<V,List<A>> mapActionsByView;
	private final Map<R,List<A>> mapActionsByRole;
	private final Map<U,List<A>> mapActionsByUsecase;
	
	private final Comparator<R> cpRole;
	private final PositionComparator<M> cpMenu;
	
	protected JeeslLogger jogger;
	
	protected File dirCaching; protected void activateCaching(File dirCaching) {this.dirCaching=dirCaching;}
	private boolean cachingFilesSaved;
	private boolean debugOnInfo; protected void setDebugOnInfo(boolean log) {debugOnInfo = log;}
	private final CTX nullCtx;

	public AbstractAppSecurityBean(final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		this.fbSecurity=fbSecurity;
		
		views = new ArrayList<>();
		roles = new ArrayList<>();
		mapMenuAll = new HashMap<>();
		mapMenuRoot = new HashMap<>();
		
		mapUrlPattern = new HashMap<>();
		mapUrlMapping = new HashMap<>();
		mapCodeView = new HashMap<>();
		
		mapRoles = new HashMap<>();
		mapAreas = new HashMap<>();
		mapViewsByRole = new HashMap<>();
		mapUsecasesByRole = new HashMap<>();
		mapViewsByUsecase = new HashMap<>();
		
		mapActionsByView = new HashMap<>();
		mapActionsByRole = new HashMap<>();
		mapActionsByUsecase = new HashMap<>();
		
		debugOnInfo = false;
		cachingFilesSaved = false;
		nullCtx = fbSecurity.ejbContext().build();
		
		cpRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		cpMenu = new PositionComparator<M>();
	}
	
	public void postConstructDb(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity)
	{
		this.fSecurity=fSecurity;
		views.addAll(fSecurity.all(fbSecurity.getClassView()));
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),"Loaded", views.size());}
		
		roles.addAll(fSecurity.all(fbSecurity.getClassRole()));
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassRole().getSimpleName(),"Loaded", roles.size());}
		
		this.reloadMenu(fSecurity);
		
		List<A> actions = fSecurity.all(fbSecurity.getClassAction());
		Map<V,List<A>> mapAction = fbSecurity.ejbAction().toMapView(actions);
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassAction().getSimpleName(),"Loaded and put to Map", actions.size());}
		
		List<AR> areas = fSecurity.all(fbSecurity.getClassArea());	
		Map<V,List<AR>> mapArea = fbSecurity.ejbArea().toMapView(areas);
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassArea().getSimpleName(),"Loaded and put to Map", areas.size());}
		
		
		for(V v : views)
		{
			if(mapUrlPattern.containsKey(v.getViewPattern()))
			{
				logger.error("Duplicate View Pattern: "+mapUrlPattern.get(v.getViewPattern()).getCode()+":"+v.getCode());
			}
			if(mapUrlMapping.containsKey(v.getUrlMapping()))
			{
				logger.error("Duplicate URL Mapping: "+mapUrlMapping.get(v.getUrlMapping()).getCode()+":"+v.getCode());
			}
			update(v,mapAction.get(v),mapArea.get(v));
		}
		
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassView().getSimpleName(),"Updated", views.size());}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassView(), views));}
	}
	
	@SuppressWarnings("unchecked")
	public void postConstructFile(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity)
	{
		this.fSecurity=fSecurity;
		
		views.addAll((List<V>)ObjectIO.load(new File(dirCaching,"views.ser")));
		roles.addAll((List<R>)ObjectIO.load(new File(dirCaching,"roles.ser")));
//		menus.addAll((List<M>)ObjectIO.load(new File(dirCaching,"menus.ser")));
		
		mapUrlPattern.putAll((Map<String,V>)ObjectIO.load(new File(dirCaching,"mapUrlPattern.ser")));
		mapUrlMapping.putAll((Map<String,V>)ObjectIO.load(new File(dirCaching,"mapUrlMapping.ser")));
		mapCodeView.putAll((Map<String,V>)ObjectIO.load(new File(dirCaching,"mapCodeView.ser")));
		
		mapRoles.putAll((Map<V,List<R>>)ObjectIO.load(new File(dirCaching,"mapRoles.ser")));
		mapAreas.putAll((Map<V,List<AR>>)ObjectIO.load(new File(dirCaching,"mapAreas.ser")));
		mapViewsByRole.putAll((Map<R,List<V>>)ObjectIO.load(new File(dirCaching,"mapViewsByRole.ser")));
		mapViewsByUsecase.putAll((Map<U,List<V>>)ObjectIO.load(new File(dirCaching,"mapViewsByUsecase.ser")));

		mapActionsByView.putAll((Map<V,List<A>>)ObjectIO.load(new File(dirCaching,"mapActionsByView.ser")));
		mapActionsByRole.putAll((Map<R,List<A>>)ObjectIO.load(new File(dirCaching,"mapActionsByRole.ser")));
		mapActionsByUsecase.putAll((Map<U,List<A>>)ObjectIO.load(new File(dirCaching,"mapActionsByUsecase.ser")));
	}
	
	public void saveCachingFilesOnce()
	{
		if(dirCaching!=null && !cachingFilesSaved)
		{
			ObjectIO.save(new File(dirCaching,"views.ser"),views);
			ObjectIO.save(new File(dirCaching,"roles.ser"),roles);
//			ObjectIO.save(new File(dirCaching,"menus.ser"),menus);
			
			ObjectIO.save(new File(dirCaching,"mapUrlPattern.ser"),mapUrlPattern);
			ObjectIO.save(new File(dirCaching,"mapUrlMapping.ser"),mapUrlMapping);
			ObjectIO.save(new File(dirCaching,"mapCodeView.ser"),mapCodeView);
			
			ObjectIO.save(new File(dirCaching,"mapRoles.ser"),mapRoles);
			ObjectIO.save(new File(dirCaching,"mapAreas.ser"),mapAreas);
			ObjectIO.save(new File(dirCaching,"mapViewsByRole.ser"),mapViewsByRole);
			ObjectIO.save(new File(dirCaching,"mapViewsByUsecase.ser"),mapViewsByUsecase);
			
			ObjectIO.save(new File(dirCaching,"mapActionsByView.ser"),mapActionsByView);
			ObjectIO.save(new File(dirCaching,"mapActionsByRole.ser"),mapActionsByRole);
			ObjectIO.save(new File(dirCaching,"mapActionsByUsecase.ser"),mapActionsByUsecase);
			
			cachingFilesSaved = true;
		}
	}
	
	public void reloadMenu(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fProvidedSecurity)
	{
		mapMenuAll.clear();
		mapMenuRoot.clear();
			
		for(M m : fProvidedSecurity.all(fbSecurity.getClassMenu()))
		{
			CTX ctx = null;
			if(m.getContext()==null){ctx = nullCtx;}
			else {ctx = m.getContext();}
			
			if(!mapMenuAll.containsKey(ctx)) {mapMenuAll.put(ctx,new ArrayList<>());}
			if(!mapMenuRoot.containsKey(ctx)) {mapMenuRoot.put(ctx,new ArrayList<>());}
			
			mapMenuAll.get(ctx).add(m);
			if(m.getParent()==null) {mapMenuRoot.get(ctx).add(m);}
		}
		
		for(CTX ctx : mapMenuAll.keySet()) {Collections.sort(mapMenuAll.get(ctx),cpMenu);}
		for(CTX ctx : mapMenuRoot.keySet()) {Collections.sort(mapMenuRoot.get(ctx),cpMenu);}
		
		if(jogger!=null) {jogger.milestone(fbSecurity.getClassMenu().getSimpleName(),"Loaded Contexts", mapMenuAll.size());}
	}
	
	@Override public List<M> getAllMenus(CTX ctx)
	{
		if(ctx==null && mapMenuAll.containsKey(nullCtx)) {return mapMenuAll.get(nullCtx);}
		else if(mapMenuAll.containsKey(ctx))  {return mapMenuAll.get(ctx);}
		else {return new ArrayList<>();}
	}
	
	@Override public List<M> getRootMenus(CTX ctx)
	{
		if(ctx==null && mapMenuRoot.containsKey(nullCtx)) {return mapMenuRoot.get(nullCtx);}
		else if(mapMenuRoot.containsKey(ctx))  {return mapMenuRoot.get(ctx);}
		else {return new ArrayList<>();}
	}
	
	public void update(V view)
	{
		if(jogger!=null) {jogger.loopStart(JoggerLoop.loadView);}
		view = fSecurity.load(fbSecurity.getClassView(), view);
		if(jogger!=null) {jogger.loopEnd(JoggerLoop.loadView,1);}
		
		update(view,view.getActions(),fSecurity.allForParent(fbSecurity.getClassArea(),view));
	}
	
	private void update(V view, List<A> actions, List<AR> areas)
	{
		mapUrlPattern.put(view.getViewPattern(),view);
		mapUrlMapping.put(view.getUrlMapping(),view);
		mapCodeView.put(view.getCode(),view);
		if(actions!=null) {mapActionsByView.put(view,actions);} else {mapActionsByView.put(view,new ArrayList<>());}
		if(areas!=null) {mapAreas.put(view,areas);} else {mapAreas.put(view,new ArrayList<>());}
		if(debugOnInfo) {logger.info("Updated "+JeeslSecurityView.class.getSimpleName()+" "+view.getCode()+" "+mapAreas.get(view).size());}
	}
	
	public void update(R role)
	{
		if(debugOnInfo) {logger.info("Updating "+JeeslSecurityRole.class.getSimpleName()+" "+role.getCode());}
		role = fSecurity.load(role,false);
		mapViewsByRole.put(role,role.getViews());
		mapUsecasesByRole.put(role,role.getUsecases());
		mapActionsByRole.put(role,role.getActions());
	}
	
	public void update(U usecase)
	{
		if(debugOnInfo) {logger.info("Updating "+JeeslSecurityUsecase.class.getSimpleName()+" "+usecase.getCode());}
		usecase = fSecurity.load(fbSecurity.getClassUsecase(), usecase);
		mapViewsByUsecase.put(usecase,usecase.getViews());
		mapActionsByUsecase.put(usecase, usecase.getActions());
	}
	
	@Override public V findViewByCode(String code)
	{
		if(mapCodeView.containsKey(code)) {return mapCodeView.get(code);}
		else {return null;}
	}
	
	@Override public V findViewByHttpPattern(String pattern)
	{
//		logger.info("findViewByHttpPattern: "+pattern);
		if(mapUrlPattern.containsKey(pattern)) {return mapUrlPattern.get(pattern);}
		else if(pattern.endsWith(".jsf"))
		{
			String patternXhtml = pattern.substring(0,pattern.lastIndexOf(".jsf"))+".xhtml";
			return findViewByHttpPattern(patternXhtml);
		}
		else {return null;}
	}
	
	@Override public V findViewByUrlMapping(String pattern)
	{
		if(mapUrlMapping.containsKey(pattern)) {return mapUrlMapping.get(pattern);}
		else {return null;}
	}

	@Override public List<R> fRoles(V view)
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}
		if(debugOnInfo) {logger.info("fRoles for view "+view.getCode());}
		
		if(!mapRoles.containsKey(view))
		{
			List<R> list = fSecurity.rolesForView(view);
			Collections.sort(list,cpRole);
			if(debugOnInfo) {for(R r : list) {logger.info(r.getCategory().getPosition()+"."+r.getPosition()+" "+r.getCode());}}
			mapRoles.put(view,list);
		}
		return mapRoles.get(view);
	}
	@Override public List<AR> fAreas(V view)
	{
		List<AR> result = new ArrayList<>();
		StringBuilder sb = null;
		if(debugOnInfo) {sb = new StringBuilder(); sb.append("fAreas("+view.getCode()+") "+mapAreas.containsKey(view));}
		if(!mapAreas.containsKey(view))
		{
			if(debugOnInfo) {sb.append(" Updating "+JeeslSecurityView.class.getSimpleName());}
			update(view);
		}
		result.addAll(mapAreas.get(view));
		if(debugOnInfo) {sb.append(" ").append(result.size());logger.info(sb.toString());}
		return result;
	}
	
	@Override public List<V> fViews(R role)
	{
		if(!mapViewsByRole.containsKey(role)){update(role);}
		return mapViewsByRole.get(role);
	}
	@Override public List<V> fViews(U usecase)
	{
		if(!mapViewsByUsecase.containsKey(usecase)){update(usecase);}
		return mapViewsByUsecase.get(usecase);
	}
	@Override public List<U> fUsecases(R role)
	{
		if(!mapUsecasesByRole.containsKey(role)){update(role);}
		return mapUsecasesByRole.get(role);
	}
	
	@Override public List<A> fActions(V view)
	{
		if(!mapActionsByView.containsKey(view)){update(view);}
		return mapActionsByView.get(view);
	}
	@Override public List<A> fActions(R role)
	{
		if(!mapActionsByRole.containsKey(role)){update(role);}
		return mapActionsByRole.get(role);
	}
	@Override public List<A> fActions(U usecase)
	{
		if(!mapActionsByUsecase.containsKey(usecase)){update(usecase);}
		return mapActionsByUsecase.get(usecase);
	}
}