package org.jeesl.factory.xml.system.status;

import java.util.List;
import java.util.Objects;

import net.sf.ahtutils.xml.status.Scopes;

import org.jeesl.api.exception.xml.JeeslXmlStructureException;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlScopesFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlScopesFactory.class);
		
	private Scopes q;
	
	private XmlScopeFactory<L,D,S> xfScope;
	
	public XmlScopesFactory(String localeCode, Scopes q)
	{
		this.q=q;
		if(Objects.nonNull(q.getScope())) {xfScope = new XmlScopeFactory<>(localeCode,q.getScope().get(0));}
	}
	
	public Scopes build(List<S> ejbs) throws JeeslXmlStructureException
	{
		Scopes xml = new Scopes();
		
		if(Objects.nonNull(xml.getSize()))
		{
			if(ejbs!=null){xml.setSize(ejbs.size());}
			else{xml.setSize(0);}
		}
		
		if(Objects.nonNull(q.getScope()) && ejbs!=null)
		{
			for(S s : ejbs)
			{
				xml.getScope().add(xfScope.build(s));
			}
		}
		
		return xml;
	}	
}