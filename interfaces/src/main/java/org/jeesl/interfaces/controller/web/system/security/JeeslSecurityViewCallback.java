package org.jeesl.interfaces.controller.web.system.security;

import java.io.Serializable;

public interface JeeslSecurityViewCallback extends Serializable
{
	void propagateChanges();
}