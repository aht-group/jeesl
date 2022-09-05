package org.jeesl.controller.monitoring.counter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jeesl.factory.xml.system.io.sync.XmlExceptionFactory;
import org.jeesl.factory.xml.system.io.sync.XmlExceptionsFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.model.json.system.io.ssi.update.JsonSsiMessage;
import org.jeesl.model.json.system.io.ssi.update.JsonSsiStatistic;
import org.jeesl.model.json.system.job.JsonJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Type;
import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.ahtutils.xml.sync.Mapper;
import net.sf.ahtutils.xml.sync.Result;
import net.sf.exlp.util.DateUtil;

public class DataUpdateTracker implements net.sf.ahtutils.interfaces.controller.DataUpdateTracker
{
	final static Logger logger = LoggerFactory.getLogger(DataUpdateTracker.class);
	
	public static enum Code {success,fail,partial}
	
	private DataUpdate update;
	private final org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate json;
	
	private final Map<String,Integer> updateSuccess,updateFail;
	private final Map<String,Integer> createSuccess,createFail;
	
	public static DataUpdateTracker instance() {return new DataUpdateTracker();}
	public DataUpdateTracker(){this(false);}
	public DataUpdateTracker(boolean autoStart)
	{
		update = new DataUpdate();
		json = new org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate();
		json.setJob(new JsonJob());
		json.setStatistic(new JsonSsiStatistic());
		
		update.setResult(new Result());
		update.getResult().setSuccess(0);
		update.getResult().setFail(0);
		update.getResult().setSkip(0);
		update.getResult().setTotal(0);
		
		updateSuccess = new HashMap<String,Integer>();
		updateFail = new HashMap<String,Integer>();
		createSuccess = new HashMap<String,Integer>();
		createFail = new HashMap<String,Integer>();
		
		if(autoStart){start();}
	}
	
	public DataUpdateTracker start()
	{
		update.setBegin(DateUtil.toXmlGc(new Date()));
		json.getJob().setStart(LocalDateTime.now());
		return this;
	}
	
	public void stop()
	{
		update.setFinished(DateUtil.toXmlGc(new Date()));
		update.getResult().setTotal(update.getResult().getSuccess()+update.getResult().getFail());
		
		if(json.getJob().getEnd()==null) {json.getJob().setEnd(LocalDateTime.now());}
	}
	
	public void success()
	{
		update.getResult().setSuccess(update.getResult().getSuccess()+1);
		if(json.getStatistic().getSuccess()==null) {json.getStatistic().setSuccess(1);}
		else {json.getStatistic().setSuccess(json.getStatistic().getSuccess()+1);}
		total();
	}
	
	public void obsolete()
	{
		if(json.getStatistic().getObsolete()==null) {json.getStatistic().setObsolete(1);}
		else {json.getStatistic().setObsolete(json.getStatistic().getObsolete()+1);}
		total();
	}
	
	private void total()
	{
		if(json.getStatistic().getTotal()==null) {json.getStatistic().setTotal(1);}
		else {json.getStatistic().setTotal(json.getStatistic().getTotal()+1);}
	}
	
	private void error()
	{
		if(json.getStatistic().getError()==null) {json.getStatistic().setError(1);}
		else {json.getStatistic().setError(json.getStatistic().getError()+1);}
		total();
	}
	
	public void skip()
	{
		update.getResult().setSkip(update.getResult().getSkip()+1);
	}
	
	@Override public void createSuccess(Class<?> c)
	{
		if(!createSuccess.containsKey(c.getName())){createSuccess.put(c.getName(), 0);}
		createSuccess.put(c.getName(), createSuccess.get(c.getName())+1);
	}
	@Override public void updateSuccess(Class<?> c, long id)
	{
		if(!updateSuccess.containsKey(c.getName())){updateSuccess.put(c.getName(), 0);}
		updateSuccess.put(c.getName(), updateSuccess.get(c.getName())+1);
	}
	
	@Override public void createFail(Class<?> c, Throwable t)
	{
		if(!createFail.containsKey(c.getName())){createFail.put(c.getName(), 0);}
		createFail.put(c.getName(), createFail.get(c.getName())+1);
	}
	@Override public void updateFail(Class<?> c, long id, Throwable t)
	{
		if(!updateFail.containsKey(c.getName())){updateFail.put(c.getName(), 0);}
		updateFail.put(c.getName(), updateFail.get(c.getName())+1);
	}
	
	public void error(Throwable t)
	{
		error();
		if(json.getMessages()==null) {json.setMessages(new ArrayList<>());}
		
		JsonSsiMessage m = new JsonSsiMessage();
		m.setMessage(t.getMessage());
		json.getMessages().add(m);
	}
	
	public void fail(Throwable t, boolean printStackTrace)
	{
		if(printStackTrace){t.printStackTrace();}
		update.getResult().setFail(update.getResult().getFail()+1);
		if(!update.isSetExceptions()){update.setExceptions(XmlExceptionsFactory.build());}
		update.getExceptions().getException().add(XmlExceptionFactory.build(t));
	}
	
	public void add(DataUpdate dataUpdate)
	{
		update.getResult().setFail(update.getResult().getFail()+dataUpdate.getResult().getFail());
		update.getResult().setSuccess(update.getResult().getSuccess()+dataUpdate.getResult().getSuccess());
	}
	
	public void setType(Type type)
	{
		update.setType(type);
	}
	
	public DataUpdate getUpdate() {return update;}
	
	public DataUpdate toDataUpdate()
	{
		if(!update.isSetFinished()){stop();}
		
		if(update.getResult().getSuccess()==update.getResult().getTotal()){update.getResult().setStatus(XmlStatusFactory.create(Code.success.toString()));}
		else if(update.getResult().getFail()==update.getResult().getTotal()){update.getResult().setStatus(XmlStatusFactory.create(Code.fail.toString()));}
		else if(update.getResult().getFail()!=0){update.getResult().setStatus(XmlStatusFactory.create(Code.partial.toString()));}
		
		if(!updateSuccess.isEmpty())
		{
			for(String key : updateSuccess.keySet())
			{
				Mapper m = new Mapper();
				m.setClazz(key);
				m.setCode("updateSuccess");
				m.setNewId(updateSuccess.get(key));
				update.getMapper().add(m);
			}
		}
		
		return update;
	}
	
	public void process(boolean debug)
	{
		Set<String> setClassNames = new HashSet<String>();
		
		setClassNames.addAll(updateSuccess.keySet());
		setClassNames.addAll(updateFail.keySet());
		setClassNames.addAll(createSuccess.keySet());
		setClassNames.addAll(createFail.keySet());
		
		for(String c : setClassNames)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(c);
			sb.append(" udpateSuccess:");if(updateSuccess.containsKey(c)){sb.append(updateSuccess.get(c));}else{sb.append(0);}
			sb.append(" udpateFail:");if(updateFail.containsKey(c)){sb.append(updateFail.get(c));}else{sb.append(0);}
			sb.append(" createSuccess:");if(createSuccess.containsKey(c)){sb.append(createSuccess.get(c));}else{sb.append(0);}
			sb.append(" createFail:");if(createFail.containsKey(c)){sb.append(createFail.get(c));}else{sb.append(0);}
			logger.info(sb.toString());
		}
	}
	
	public org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate toJson()
	{
		this.stop();
		return json;
	}
}