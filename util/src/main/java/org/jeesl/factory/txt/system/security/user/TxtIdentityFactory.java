package org.jeesl.factory.txt.system.security.user;

import java.util.Objects;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtIdentityFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtIdentityFactory.class);
   
    public static <CTX extends JeeslSecurityContext<?,?>, USER extends JeeslUser<?>, I extends JeeslIdentity<?,?,?,?,CTX,USER>> String key(String prefix, I identity, String key)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append(prefix).append("-");
    	sb.append(identity.getContext().getId()).append("-");
    	if(Objects.isNull(identity.getUser())) {sb.append("0");}
    	else {sb.append(identity.getUser().getId());}
    	sb.append("-").append(key);
    	return sb.toString();
    }
}