package org.jeesl.factory.xml.io.mail;

import net.sf.ahtutils.interfaces.model.system.mail.UtilsMailTracker;
import net.sf.exlp.util.DateUtil;

import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.mail.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTrackerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
	private Tracker q;
	
	public XmlTrackerFactory(Tracker q)
	{
		this.q=q;
	}
	
    public <T extends UtilsMailTracker<S,L,U,D>, S extends JeeslStatus<L,D,S>, L extends JeeslLang, U extends EjbWithId,D extends JeeslDescription>
    	Tracker create(T ejb)
    {
    	Tracker xml = new Tracker();
    	
    	if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
    	if(Objects.nonNull(q.getRefId())) {xml.setRefId(ejb.getRefId());}
    	if(Objects.nonNull(q.getType())) {xml.setType(ejb.getType().getCode());}
    	if(Objects.nonNull(q.getCreated())) {xml.setCreated(DateUtil.toXmlGc(ejb.getRecordCreated()));}
    	if(Objects.nonNull(q.getSent())) {xml.setSent(DateUtil.toXmlGc(ejb.getRecordSent()));}
    	if(Objects.nonNull(q.getRetryCounter())) {xml.setRetryCounter(ejb.getRetryCounter());}
    	
    	return xml;
    }
}