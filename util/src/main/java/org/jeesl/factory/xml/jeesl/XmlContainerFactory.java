package org.jeesl.factory.xml.jeesl;

import java.util.List;

import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.jeesl.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;

@SuppressWarnings("rawtypes")
public class XmlContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlContainerFactory.class);
	
	private XmlStatusFactory xfStatus;
	
	public XmlContainerFactory(Status query)
	{
		xfStatus = new XmlStatusFactory(query);
	}
	
	@SuppressWarnings("unchecked")
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Container build(List<S> list)
	{
		Container xml = build();
		for(S s : list)
		{
			xml.getStatus().add(xfStatus.build(s));
		}
		return xml;
	}
	
	public static Container buildStatusList(List<Status> list)
    {
    	Container xml = build();
    	xml.getStatus().addAll(list);
    	return xml;
    }
	
    public static Container build()
    {
    	Container xml = new Container();
    	return xml;
    }
}