package org.jeesl.interfaces.controller.web.system.security;

import java.io.Serializable;

public interface JeeslSecurityMenuCallback extends Serializable
{
	void propagateChanges();
}