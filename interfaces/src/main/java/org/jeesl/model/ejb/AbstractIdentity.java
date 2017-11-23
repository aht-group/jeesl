package org.jeesl.model.ejb;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;

public class AbstractIdentity <L extends UtilsLang, D extends UtilsDescription,
								   C extends JeeslSecurityCategory<L,D>,
								   R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								   V extends JeeslSecurityView<L,D,C,R,U,A>,
								   U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								   A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								   AT extends JeeslSecurityTemplate<L,D,C>,
								   USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractIdentity.class);
	public static final long serialVersionUID=1;
	
	private String loginName;
	public String getLoginName() {return loginName;}
	public void setLoginName(String loginName) {this.loginName = loginName;}
	
	private String loginPassword;
	public String getLoginPassword() {return loginPassword;}
	public void setLoginPassword(String loginPassword) {this.loginPassword = loginPassword;}
	
	private Map<String,Boolean> mapUsecases,mapRoles,mapActions;
	
	private Map<String,Boolean> mapSystemViews; //Only systems views, domain views not included
	
	private Map<String,Boolean> mapViews;
	
	private boolean loggedIn; public boolean isLoggedIn() {return loggedIn;}  public void setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn;}

	
	public AbstractIdentity()
	{		
		mapUsecases = new Hashtable<String,Boolean>();
		mapViews = new Hashtable<String,Boolean>();
		mapSystemViews = new Hashtable<String,Boolean>();
		mapRoles = new Hashtable<String,Boolean>();
		mapActions = new Hashtable<String,Boolean>();
		
		loggedIn = false;
	}
	
	public void allowUsecase(U usecase) {mapUsecases.put(usecase.getCode(), true);}
	public void allowView(V view) {mapViews.put(view.getCode(), true);}
	public void allowRole(R role) {mapRoles.put(role.getCode(), true);}
	public void allowAction(A action) {mapActions.put(action.toCode(), true);}
	
	public boolean hasUsecase(String code)
	{
		if(mapUsecases.containsKey(code))
		{
			return mapUsecases.get(code);
		}
		return false;
	}
	
	public boolean hasView(V view)
	{
		if(mapViews.containsKey(view.toString())){return mapViews.get(view.toString());}
		return false;
	}
	public boolean hasView(String code)
	{
		if(mapViews.containsKey(code)){return mapViews.get(code);}
		return false;
	}
	
	public boolean hasSystemView(String code)
	{
		if(mapSystemViews.containsKey(code)){return mapSystemViews.get(code);}
		return false;
	}
	
	public boolean hasRole(String code)
	{
		if(mapRoles.containsKey(code))
		{
			return mapRoles.get(code);
		}
		return false;
	}
	
	public boolean hasAction(String code)
	{
		if(mapActions.containsKey(code))
		{
			return mapActions.get(code);
		}
		return false;
	}
	
	public int sizeAllowedUsecases() {return mapUsecases.size();}
	public int sizeAllowedViews() {return mapViews.size();}
	public int sizeAllowedSystemViews() {return mapSystemViews.size();}
	public int sizeAllowedRoles() {return mapRoles.size();}
	public int sizeAllowedActions() {return mapActions.size();}
	
	public Map<String, Boolean> getMapUsecases() {return mapUsecases;}
	
	public Map<String, Boolean> getMapRoles() {return mapRoles;}
	public Map<String, Boolean> getMapActions() {return mapActions;}
	
	public Map<String, Boolean> getMapViews() {return mapViews;}
	public Map<String, Boolean> getMapSystemViews() {return mapSystemViews;}
	public void setMapSystemViews(Map<String, Boolean> mapSystemViews) {this.mapSystemViews = mapSystemViews;}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("loggedIn:"+loggedIn);
		return sb.toString();
	}
}