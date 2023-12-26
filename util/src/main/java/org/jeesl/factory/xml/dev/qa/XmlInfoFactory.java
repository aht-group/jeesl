package org.jeesl.factory.xml.dev.qa;

import java.util.Objects;

import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.module.dev.qa.Comment;
import org.jeesl.model.xml.module.dev.qa.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaTestInfo;

public class XmlInfoFactory<L extends JeeslLang, D extends JeeslDescription,
							QATI extends UtilsQaTestInfo<QATC>,
							QATC extends JeeslStatus<L,D,QATC>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlInfoFactory.class);
		
	private Info q;
	private XmlStatusFactory<L,D,QATC> xfCondition;
	
	public XmlInfoFactory(Info q)
	{
		this.q=q;
		if(Objects.nonNull(q.getStatus())) {xfCondition = new XmlStatusFactory<>(null,q.getStatus());}
	}
	
	public Info build(QATI info)
	{
		Info xml = new Info();
	
		if(Objects.nonNull(q.getStatus())) {xml.setStatus(xfCondition.build(info.getCondition()));}
		
		if(q.isSetComment())
		{
			xml.setComment(new Comment());
			xml.getComment().setValue(info.getDescription());
			if(xml.getComment().getValue()==null){xml.getComment().setValue("");}
		}
		
		return xml;
	}
}