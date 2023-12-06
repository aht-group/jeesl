package org.jeesl.api.bean.cache;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;

public interface JeeslMenuCache <M extends JeeslSecurityMenu<?,?,?,M>>
							extends Serializable
{	
//	Cache<String,List<M>> cacheMenu();
//	Cache<String,List<M>> cacheCrumb();
//	Cache<String,List<M>> cacheSub();
	
	boolean cacheContains(String key);
	List<M> cacheGet(String key);
	void cachePut(String key, List<M> list);
}