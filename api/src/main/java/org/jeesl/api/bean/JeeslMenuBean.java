package org.jeesl.api.bean;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;

public interface JeeslMenuBean<V extends JeeslSecurityView<?,?,?,?,?,?>,
								CTX extends JeeslSecurityContext<?,?>,
								M extends JeeslSecurityMenu<?,V,CTX,M>>
							extends Serializable
{	
	void updateLocale(String localeCode);
}