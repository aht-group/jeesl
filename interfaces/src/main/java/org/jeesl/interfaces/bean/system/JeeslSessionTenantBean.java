package org.jeesl.interfaces.bean.system;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;

public interface JeeslSessionTenantBean <CTX extends JeeslSecurityContext<?,?>> extends Serializable
{
	CTX getContext();
	boolean isHoverMainMenuSubItems();
}