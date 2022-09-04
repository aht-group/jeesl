package org.jeesl.util.query.xml.module;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.system.status.XmlSourceFactory;
import org.jeesl.factory.xml.system.util.text.XmlReferenceFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.model.xml.jeesl.QueryTs;
import org.jeesl.model.xml.module.ts.Transaction;

import net.sf.ahtutils.xml.security.User;
import net.sf.exlp.util.DateUtil;

public class XmlTsQuery
{
	public static enum Key {rTransaction}
	
	private static Map<Key,QueryTs> mQueries;
	
	public static QueryTs get(Key key){return get(key,null);}
	public static QueryTs get(Key key,String localeCode)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,QueryTs>();}
		if(!mQueries.containsKey(key))
		{
			QueryTs q = new QueryTs();
			switch(key)
			{
				case rTransaction: q.setTransaction(rTransaction());break;
			}
			mQueries.put(key, q);
		}
		QueryTs q = mQueries.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}
	
	private static Transaction rTransaction()
	{		
		User user = new User();
		user.setEmail("");
		user.setFirstName("");
		user.setLastName("");
		user.setName("");
		
		Transaction xml = new Transaction();
		xml.setId(0);
		xml.setRecord(DateUtil.toXmlGc(new Date()));
		xml.setUser(user);
		xml.setRemark(XmlRemarkFactory.build(""));
		xml.setReference(XmlReferenceFactory.build(""));
		xml.setSource(XmlSourceFactory.build(""));
		return xml;
	}

}