package org.jeesl.factory.xml.system.io.db;

import java.sql.Timestamp;

import org.exlp.util.system.DateUtil;
import org.jeesl.factory.xml.io.locale.status.XmlStatusFactory;
import org.jeesl.model.xml.io.db.Query;
import org.jeesl.model.xml.io.db.Time;
import org.jeesl.model.xml.io.db.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlQueryFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlQueryFactory.class);
	
	public enum CodeTime{transaction,query,state}
	
	public static Query build()
	{
		Query xml = new Query();
		return xml;
	}
	
	public static Query build(Timestamp tsTransaction,Timestamp tsQuery, Timestamp tsState,
								String state, String query)
	{
		Query xml = new Query();
		xml.setTimes(times(tsTransaction,tsQuery,tsState));
		xml.setStatus(XmlStatusFactory.create(state));
		xml.setStatement(query);
		return xml;
	}
	
	private static Times times(Timestamp tsTransaction,Timestamp tsQuery, Timestamp tsState)
	{
		Times times = new Times();
		if(tsTransaction!=null) {times.getTime().add(time(CodeTime.transaction,tsTransaction));}
		if(tsQuery!=null) {times.getTime().add(time(CodeTime.query,tsQuery));}
		if(tsState!=null) {times.getTime().add(time(CodeTime.state,tsState));}
		return times;
	}
	
	private static Time time(CodeTime code, Timestamp tsTransaction)
	{
		Time time = new Time();
		time.setCode(code.toString());
		time.setValue(DateUtil.toXmlGc(tsTransaction));
		return time;
	}
}