package org.jeesl.interfaces.web.security;

import java.io.Serializable;

public interface JeeslSecurityHandler extends Serializable
{
	boolean isDeveloper();
	
	<E extends Enum<E>> boolean allowSuffixCode(E actionCode);
}