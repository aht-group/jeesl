package org.jeesl.factory.xml.system.security;

import java.util.List;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.xml.security.Actions;

public class XmlActionsFactory <L extends UtilsLang,
								D extends UtilsDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C,R,V,U,A,AT,USER>,
								USER extends JeeslUser<L,D,C,R,V,U,A,AT,USER>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActionsFactory.class);
		
	private Actions q;
	
	public XmlActionsFactory(Actions q)
	{
		this.q=q;
	}
	

	public Actions build(List<A> actions)
	{
		XmlActionFactory<L,D,C,R,V,U,A,AT,USER> f = new XmlActionFactory<L,D,C,R,V,U,A,AT,USER>(q.getAction().get(0));
		
		Actions xml = build();
		for(A action : actions)
		{
			xml.getAction().add(f.build(action));
		}
		return xml;
	}
	
	public static Actions build()
	{
		return new Actions();
	}
	
	public static net.sf.ahtutils.xml.access.Actions create()
	{
		return new net.sf.ahtutils.xml.access.Actions();
	}
}