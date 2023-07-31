package org.jeesl.api.bean.cache;

import java.io.Serializable;
import java.util.List;

import org.infinispan.Cache;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;

public interface JeeslMenuCache <M extends JeeslSecurityMenu<?,?,?,M>>
							extends Serializable
{	
	Cache<String,List<M>> cacheCrumb();
	Cache<String,List<M>> cacheSub();
}