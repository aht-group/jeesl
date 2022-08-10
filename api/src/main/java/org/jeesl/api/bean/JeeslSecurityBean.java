package org.jeesl.api.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
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

public interface JeeslSecurityBean<L extends JeeslLang,D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									AR extends JeeslSecurityArea<L,D,V>,
									CTX extends JeeslSecurityContext<L,D>,
									M extends JeeslSecurityMenu<L,V,CTX,M>,
									USER extends JeeslUser<R>>
					extends Serializable
{	
	void update(V view);
	void update(R role);
	void update(U usecase);
	
	List<V> getViews();
	List<M> getAllMenus(CTX ctx);
	List<M> getRootMenus(CTX ctx);
	M getMenu(CTX ctx, V view);
	Map<M,M> getMapRoot();
	
	V findViewByCode(String cdoe);
	V findViewByHttpPattern(String pattern);
	V findViewByUrlMapping(String pattern);
	
	List<R> fRoles(V view);
	List<AR> fAreas(V view);
	
	List<V> fViews(R role);
	List<V> fViews(U usecase);
	
	List<U> fUsecases(R role);
	
	List<A> fActions(V view);
	List<A> fActions(R role);
	List<A> fActions(U usecase);
}