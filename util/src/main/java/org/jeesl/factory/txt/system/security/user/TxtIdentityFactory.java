package org.jeesl.factory.txt.system.security.user;

import java.util.Objects;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtIdentityFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtIdentityFactory.class);
    
    
    public static <CTX extends JeeslSecurityContext<?,?>, USER extends JeeslUser<?>, I extends JeeslIdentity<?,?,?,?,CTX,USER>> String key(I identity, String key)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append(identity.getContext().getId()).append("-");
    	if(Objects.isNull(identity.getUser())) {sb.append("0");}
    	else {sb.append(identity.getUser().getId());}
    	sb.append("-").append(key);
    	return sb.toString();
    }
}