package org.jeesl.jsf.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJsfSecurityHandler <R extends JeeslSecurityRole<?,?,?,V,U,A>,
													V extends JeeslSecurityView<?,?,?,R,U,A>,
													U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
													A extends JeeslSecurityAction<?,?,R,V,U,AT>,
													AT extends JeeslSecurityTemplate<?,?,?>,
													CTX extends JeeslSecurityContext<?,?>,
													M extends JeeslSecurityMenu<?,V,CTX,M>,
													AR extends JeeslSecurityArea<?,?,V>,
													USER extends JeeslUser<R>,
													I extends JeeslIdentity<R,V,U,A,CTX,USER>>
								implements JeeslJsfSecurityHandler<R,V,U,A,AR,USER>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJsfSecurityHandler.class);
	public static final long serialVersionUID=1;

	private SecurityFactoryBuilder<?,?,?,R,V,U,A,AT,?,?,AR,?,?,?,?,?,?,?,USER> fbSecurity;
	private JeeslSecurityFacade<?,R,V,U,A,CTX,M,USER> fSecurity;
	protected JeeslSecurityBean<R,V,U,A,AR,?,?,USER> bSecurity;
	
	protected List<R> roles; public List<R> getRoles() {return roles;}
	protected final List<A> actions; public List<A> getActions() {return actions;}
	private final List<AR> areas; public List<AR> getAreas() {return areas;}
	
	protected String pageCode; public String getPageCode() {return pageCode;}
	protected V view; public V getView() {return view;}
	private M menu; public M getMenu() {return menu;}
	
	protected final Map<R,Boolean> mapHasRole; @Override public Map<R,Boolean> getMapHasRole() {return mapHasRole;}
	protected final Map<String,Boolean> mapAllow; public Map<String,Boolean> getMapAllow(){return mapAllow;}
	protected final Map<Long,Boolean> mapIdAllow; public Map<Long,Boolean> getMapIdAllow(){return mapIdAllow;}
	protected final Map<String,Boolean> mapArea; public Map<String,Boolean> getMapArea() {return mapArea;}
	
	protected I identity; @Override public USER getUser() {return identity.getUser();}
	
	protected boolean noActions; public boolean isNoActions() {return noActions;}
	protected boolean noRoles; public boolean isNoRoles() {return noRoles;}
	
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	
	@Override public boolean isDeveloper() {return Objects.nonNull(identity) && identity.isDeveloper();}

	public AbstractJsfSecurityHandler(SecurityFactoryBuilder<?,?,?,R,V,U,A,AT,?,?,AR,?,?,?,?,?,?,?,USER> fbSecurity,
			JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity,
			I identity,
			V view)
	{
		logger.trace(this.getClass().getSimpleName()+" with "+JeeslSecurityBean.class.getSimpleName());
		this.fbSecurity=fbSecurity;
		this.bSecurity=bSecurity;
		this.identity=identity;

		this.view=view;
		this.pageCode=view.getCode();
		menu = bSecurity.getMenu(identity.getContext(),view);
		
		debugOnInfo = false;
		noActions=true;
		noRoles=true;
		
		mapAllow = new HashMap<>();
		mapIdAllow = new HashMap<>();
		mapArea = new HashMap<>();
		mapHasRole = new HashMap<>();
		actions = new ArrayList<>();
		
		roles = bSecurity.fRoles(view);
		areas = bSecurity.fAreas(view);
		
		noRoles = roles.size()==0;
		this.update();
	}
	
	protected void update()
	{
		this.clear();
		List<A> actions = new ArrayList<>();
		if(Objects.nonNull(bSecurity)) {actions.addAll(bSecurity.fActions(view));}
		else {actions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(),view));}
		
		if(debugOnInfo) {logger.info("Checking assignment of "+actions.size()+" "+fbSecurity.getClassAction().getSimpleName()+" for user");}
		for(A action : actions)
		{
			boolean allow = false;
			String code = JeeslSecurityAction.toCode(action);
			if(Objects.nonNull(identity)) {allow = identity.hasAction(code);}
			if(debugOnInfo) {logger.info("   - "+code+":"+allow);}
			addActionWithSecurity(action,allow);
		}
		checkIcon();
	}
	
	protected void clear()
	{
		actions.clear();
//		areas.clear();
		
		mapAllow.clear();
		mapIdAllow.clear();
		mapArea.clear();
		mapHasRole.clear();
		
		for(AR a : areas)
		{
			mapArea.put(a.getCode(),BooleanComparator.active(a.getVisible()));
		}
		
		if(debugOnInfo) {logger.info("Checking Assignment of "+roles.size()+" "+fbSecurity.getClassRole().getSimpleName()+" for user");}
		for(R r : roles)
		{
			StringBuilder sb = null;
			if(debugOnInfo)
			{
				sb = new StringBuilder();
				sb.append("\t").append(r.getCode()).append(": ");
			}
			if(identity!=null)
			{
				boolean b = identity.hasRole(r.getCode());
				if(debugOnInfo) {sb.append(b);}
				mapHasRole.put(r,b);
			}
			else
			{
				if(debugOnInfo) {sb.append(" false (identity is null)");}
				mapHasRole.put(r,false);
			}
			if(debugOnInfo) {logger.info(sb.toString());}
		}
	}

	protected void addActionWithSecurity(A action, boolean allow)
	{
		actions.add(action);
		mapAllow.put(JeeslSecurityAction.toCode(action), override(allow));
		mapIdAllow.put(action.getId(), override(allow));
		
		logger.trace(action.toString() + " " + allow(JeeslSecurityAction.toCode(action)));
	}
	
	private boolean override(boolean allow)
	{
		return allow;
	}
	
	@Override public boolean hasRole(R role)
	{
		return role!=null && mapHasRole.containsKey(role) && mapHasRole.get(role);
	}
	
	protected boolean hasDomainRole(A action, Collection<R> staffRoles)
	{
		boolean allowDomain = false;
		for(R r : staffRoles)
		{
			List<A> lA1 = new ArrayList<>();
			if(bSecurity==null)
			{
				r = fSecurity.load(r);
				lA1.addAll(r.getActions());
			}
			else {lA1.addAll(bSecurity.fActions(r));}
			
			if(lA1.contains(action)){allowDomain=true;}
			else
			{
				List<U> usecases = new ArrayList<>();
				if(bSecurity==null) {usecases.addAll(r.getUsecases());}
				else {usecases.addAll(bSecurity.fUsecases(r));}
				for(U uc : usecases)
				{
					List<A> lA2 = new ArrayList<>();
					if(bSecurity==null)
					{
						uc = fSecurity.load(fbSecurity.getClassUsecase(), uc);
						lA2.addAll(uc.getActions());
					}
					else {lA2.addAll(bSecurity.fActions(uc));}
					
					if(lA2.contains(action)){allowDomain=true;}
				}
			}
		}
		return allowDomain;
	}
	
	protected void checkIcon() {noActions = actions.size()==0;}
	
	@Override public <E extends Enum<E>> boolean allowSuffixCode(E actionCode)
	{
		return allow(pageCode+"."+actionCode.toString());
	}
	@Override public boolean allow(String actionCode)
	{
		if(actionCode==null){return false;}
		else {return (mapAllow.containsKey(actionCode) && mapAllow.get(actionCode));}
	}
	
	public void debug(boolean debug)
	{
		if(debug)
		{
			logger.info("Debugging SecurityHandler ("+pageCode+")");
			for(String key : mapAllow.keySet())
			{
				logger.info("\t"+key+": "+mapAllow.get(key));
			}
			logger.info("\t"+JeeslSecurityAction.class.getSimpleName()+": "+actions.size());
			for(A a : actions)
			{
				logger.info("\t\t"+a.toString());
			}
			logger.info("\t"+JeeslSecurityArea.class.getSimpleName()+": "+areas.size());
			for(AR area : areas)
			{
				logger.info("\t\t"+area.toString()+" "+BooleanComparator.active(mapArea.get(area.getCode())));
			}
		}
	}
	
	protected void updateActionsForDomainRoles(List<R> staffRoles)
	{
		List<A> actions = new ArrayList<>();
		if(bSecurity!=null) {actions.addAll(bSecurity.fActions(view));}
		else {actions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(),view));}
		
		for(A action : actions)
		{
			boolean allowSystem = identity.hasAction(JeeslSecurityAction.toCode(action));
			boolean allowDomain = hasDomainRole(action,staffRoles);

			if(debugOnInfo) {logger.info("\t\t"+action.getCode()+" system:"+allowSystem+" domain:"+allowDomain);}
			
			boolean allow = allowSystem || allowDomain;
			this.addActionWithSecurity(action,allow);
		}
		checkIcon();
	}
	
	public <E extends Enum<E>> AR getArea(E code)
	{
		for(AR a : areas)
		{
			if(a.getCode().equals(code.toString())){return a;}
		}
		return null;
	}
	
	public <E extends Enum<E>> void toggleArea(E code, boolean value)
	{
		for(AR a : areas)
		{
			if(a.getCode().equals(code.toString())) {mapArea.put(a.getCode(),value);}
		}
	}
	public <E extends Enum<E>> void toggleAreas(List<AR> togged, boolean value)
	{
		for(AR a : areas) {mapArea.put(a.getCode(),false);}
		for(AR a : togged)
		{
			mapArea.put(a.getCode(),value);
		}
	}
	public <E extends Enum<E>> void toggleArea(E code)
	{
		for(AR a : areas)
		{
			if(a.getCode().equals(code.toString())) {toggleArea(a);}
		}
	}
	public void toggleArea(AR area)
	{
		mapArea.put(area.getCode(), !BooleanComparator.active(mapArea.get(area.getCode())));
	}
	
	public boolean toggleActive(AR area)
	{
		return BooleanComparator.active(mapArea.get(area.getCode()));
	}
}