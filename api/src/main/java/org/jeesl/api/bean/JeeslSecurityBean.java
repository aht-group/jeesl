package org.jeesl.api.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public interface JeeslSecurityBean<R extends JeeslSecurityRole<?,?,?,V,U,A>,
									V extends JeeslSecurityView<?,?,?,R,U,A>,
									U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
									A extends JeeslSecurityAction<?,?,R,V,U,?>,
									AR extends JeeslSecurityArea<?,?,V>,
									CTX extends JeeslSecurityContext<?,?>,
									M extends JeeslSecurityMenu<?,V,CTX,M>,
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
	
	V findViewByCode(String code);
	V findViewByUrlMapping(String pattern);
	V findViewByHttpPattern(String pattern);
	
	List<R> fRoles(V view);
	List<AR> fAreas(V view);
	
	List<V> fViews(R role);
	List<V> fViews(U usecase);
	
	List<U> fUsecases(R role);
	
	List<A> fActions(V view);
	List<A> fActions(R role);
	List<A> fActions(U usecase);
}