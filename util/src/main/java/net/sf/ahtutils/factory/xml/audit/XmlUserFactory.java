package net.sf.ahtutils.factory.xml.audit;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.audit.User;

public class XmlUserFactory <L extends JeeslLang,
							D extends JeeslDescription, 
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlUserFactory.class);
	
	public User build(USER user)
	{
		User xml = new User();
		xml.setFirstName(user.getFirstName());
		xml.setLastName(user.getLastName());
		
		
		
		return xml;
	}
	
	public User labelFirstLastEmail(USER user)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(user.getFirstName());
		sb.append(" ");
		sb.append(user.getLastName());
		if(user.getClass().isAssignableFrom(EjbWithEmail.class))
		{
			EjbWithEmail email = (EjbWithEmail)user;
			sb.append(" (").append(email.getEmail()).append(")");
		}
		
		User xml = new User();
		xml.setLabel(sb.toString());
		
		return xml;
	}
}