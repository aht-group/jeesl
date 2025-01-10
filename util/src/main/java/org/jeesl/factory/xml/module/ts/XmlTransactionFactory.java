package org.jeesl.factory.xml.module.ts;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.system.DateUtil;
import org.jeesl.factory.xml.io.locale.status.XmlSourceFactory;
import org.jeesl.factory.xml.system.security.XmlUserFactory;
import org.jeesl.factory.xml.system.util.text.XmlReferenceFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.model.xml.io.db.query.QueryTs;
import org.jeesl.model.xml.io.locale.status.Source;
import org.jeesl.model.xml.module.ts.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTransactionFactory<L extends JeeslLang,D extends JeeslDescription,
								   TRANSACTION extends JeeslTsTransaction<SOURCE,?,USER,?>,
								   SOURCE extends EjbWithLangDescription<L,D>, 
								   USER extends JeeslUser<?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTransactionFactory.class);

	private final String localeCode;
	private final Transaction q;
	
	private XmlUserFactory<USER> xfUser;

	public XmlTransactionFactory(QueryTs query){this(query.getLocaleCode(),query.getTransaction());}
	public XmlTransactionFactory(String localeCode,Transaction q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(Objects.nonNull(q.getUser())) {xfUser = new XmlUserFactory<>(q.getUser());}
	}

	public static Transaction build() {return new Transaction();}

	public Transaction build(TRANSACTION ejb)
	{
		Transaction xml = new Transaction();

		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getRecord())) {xml.setRecord(DateUtil.toXmlGc(ejb.getRecord()));}
		if(Objects.nonNull(q.getUser())) {xml.setUser(xfUser.build(ejb.getUser()));}
		
		if(Objects.nonNull(q.getRemark())) {xml.setRemark(XmlRemarkFactory.build(ejb.getRemark()));}
		if(Objects.nonNull(q.getReference())) {xml.setReference(XmlReferenceFactory.build(ejb.getReference()));}
		
		if(ObjectUtils.allNotNull(q.getSource(),ejb.getSource()) && ejb.getSource().getName().containsKey(localeCode))
		{
			Source source = XmlSourceFactory.label(null,ejb.getSource().getName().get(localeCode).getLang());
			xml.setSource(source);
		}
		
		return xml;
	}
}