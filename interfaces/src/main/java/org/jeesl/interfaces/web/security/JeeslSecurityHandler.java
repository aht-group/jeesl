package org.jeesl.interfaces.web.security;

import java.io.Serializable;

public interface JeeslSecurityHandler extends Serializable
{
	<E extends Enum<E>> boolean allowSuffixCode(E actionCode);
}