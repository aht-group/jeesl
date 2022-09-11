package org.jeesl.factory.xml.system.security;

import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.Actions;

public class XmlActionsFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,?>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActionsFactory.class);
		
	private Actions q;
	
	private XmlActionFactory<L,D,C,R,V,U,A,AT> xfAction;
	
	public XmlActionsFactory(Actions q)
	{
		this.q=q;
		if(q.isSetAction()) {xfAction = new XmlActionFactory<>(q.getAction().get(0));}
	}

	public Actions build(List<A> actions)
	{
		Actions xml = build();
		if(q.isSetAction())
		{
			for(A action : actions)
			{
				xml.getAction().add(xfAction.build(action));
			}
		}
		return xml;
	}
	
	public static Actions build()
	{
		return new Actions();
	}
}