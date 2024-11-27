package org.jeesl.factory.xml.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Goals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlGoalsFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlGoalsFactory.class);
		
//	private Goals q;
	
//	private XmlGoalFactory<L,D,S> xfGoal;
	
	public XmlGoalsFactory(String localeCode, Goals q)
	{
//		this.q=q;
//		if(Objects.nonNull(q.getScope())) {xfScope = new XmlScopeFactory<>(localeCode,q.getScope().get(0));}
	}
	
	public static Goals build() {return new Goals();}
	
//	public Scopes build(List<S> ejbs) throws JeeslXmlStructureException
//	{
//		Scopes xml = new Scopes();
//		
//		if(Objects.nonNull(xml.getSize()))
//		{
//			if(ejbs!=null){xml.setSize(ejbs.size());}
//			else{xml.setSize(0);}
//		}
//		
//		if(Objects.nonNull(q.getScope()) && ejbs!=null)
//		{
//			for(S s : ejbs)
//			{
//				xml.getScope().add(xfScope.build(s));
//			}
//		}
//		
//		return xml;
//	}	
}