package org.jeesl.factory.xml.system.io.revision;

import java.util.Date;

import org.jeesl.factory.xml.system.security.XmlUserFactory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionContainer;
import org.jeesl.interfaces.model.io.revision.data.JeeslRevision;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.system.revision.Revision;
import org.jeesl.util.query.xml.system.io.XmlRevisionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class XmlRevisionFactory <REV extends JeeslRevision,
								USER extends JeeslUser<?>,
								C extends JeeslRevisionContainer<REV,?,USER>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRevisionFactory.class);
	
	private final Revision q;
	
	private XmlUserFactory<USER> xfUser;
	
	public static <REV extends JeeslRevision,
					USER extends JeeslUser<?>,
					C extends JeeslRevisionContainer<REV,?,USER>>
		XmlRevisionFactory<REV,USER,C> instance(String localeCode, XmlRevisionQuery.Key key)
	{
		return new XmlRevisionFactory<>(XmlRevisionQuery.get(key, localeCode).getRevision());
	}
	
	public XmlRevisionFactory(Revision q)
	{
		this.q=q;
		if(q.isSetUser()) {xfUser = new XmlUserFactory<>(q.getUser());}
	}
	

	public static Revision build(){return new Revision();}
	
	public static Revision build(org.jeesl.interfaces.model.system.security.user.JeeslRevision revision)
	{
		Revision xml =  build();
		xml.setRecord(DateUtil.toXmlGc(revision.getInfo().getAuditRecord()));
		return xml;
	}
	
	public static Revision build(int version, Date record)
	{
		Revision xml =  build();
		xml.setVersion(version);
		xml.setRecord(DateUtil.toXmlGc(record));
		return xml;
	}

	public Revision build(C container)
	{
		Revision xml = XmlRevisionFactory.build();
		if(q.isSetRecord()) {xml.setRecord(DateUtil.toXmlGc(container.getInfo().getAuditRecord()));}
		if(q.isSetUser()) {xml.setUser(xfUser.build(container.getUser()));}
		return xml;
	}
}