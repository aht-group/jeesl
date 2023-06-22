package org.jeesl.factory.xml.module.workflow;


import java.util.Objects;

import org.jeesl.factory.xml.system.security.XmlUserFactory;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.module.workflow.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlActivityFactory<WA extends JeeslWorkflowActivity<?,?,?,?,USER>,
								USER extends JeeslUser<?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActivityFactory.class);
	
	
	@SuppressWarnings("unused") private final String localeCode;
	private final Activity q;
	
	private XmlUserFactory<USER> xfUser;
	
	public static <WA extends JeeslWorkflowActivity<?,?,?,?,USER>, USER extends JeeslUser<?>> XmlActivityFactory<WA,USER>
		instance(String localeCode, Activity q)
	{
		return new XmlActivityFactory<>(localeCode,q);
	}
	
	private XmlActivityFactory(String localeCode, Activity q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getUser())) {xfUser = XmlUserFactory.instance(q.getUser());}
	}
	
	public static Activity build(){return new Activity();}
	
	public Activity build(WA ejb)
	{
		Activity xml = XmlActivityFactory.build();

		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getUser())) {xml.setUser(xfUser.build(ejb.getUser()));}
		return xml;
	}
	
}