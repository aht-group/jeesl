package org.jeesl.api.bean;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;

public interface JeeslMenuBean<V extends JeeslSecurityView<?,?,?,?,?,?>,
								CTX extends JeeslSecurityContext<?,?>,
								M extends JeeslSecurityMenu<?,V,CTX,M>>
							extends Serializable
{	
//	void updateLocale(String localeCode);
	List<M> getMainMenu();
	List<M> subMenu(String key);
	List<M> breadcrumb(String key);
}