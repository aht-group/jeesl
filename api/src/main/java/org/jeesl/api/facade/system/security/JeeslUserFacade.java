package org.jeesl.api.facade.system.security;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public interface JeeslUserFacade <USER extends JeeslUser<?>> extends JeeslFacade
{	
	USER load(USER user);
	List<USER> likeNameFirstLast(String query);
}