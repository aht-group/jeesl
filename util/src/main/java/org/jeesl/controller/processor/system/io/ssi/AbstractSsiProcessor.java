package org.jeesl.controller.processor.system.io.ssi;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.monitoring.counter.BucketSizeCounter;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiDataFactory;
import org.jeesl.factory.sql.SqlFactory;
import org.jeesl.interfaces.controller.processor.SsiMappingProcessor;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.JsonUtil;

public abstract class AbstractSsiProcessor<L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
										LINK extends JeeslIoSsiStatus<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										JSON extends Object
>
						implements SsiMappingProcessor<MAPPING,DATA,JSON>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiProcessor.class);
	
	protected final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi;
	protected final JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,?> fSsi;
	
	protected final EjbIoSsiDataFactory<MAPPING,DATA,LINK> efData;
	
	protected final EjbCodeCache<LINK> cacheLink; public EjbCodeCache<LINK> getCacheLink() {return cacheLink;}
	protected final EjbCodeCache<JOB> cacheJob; public EjbCodeCache<LINK> getCacheJob() {return cacheLink;}
		
	protected MAPPING mapping; @Override public MAPPING getMapping() {return mapping;}
	protected BucketSizeCounter jec; public void setEventCounter(BucketSizeCounter jec) {this.jec = jec;}

	public AbstractSsiProcessor(IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi,
									JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,?> fSsi)
	{
		this.fSsi=fSsi;
		this.fbSsi=fbSsi;
		
		this.initMappings();
		
		jec = BucketSizeCounter.instance();
		cacheLink = EjbCodeCache.instance(fbSsi.getClassLink()).facade(fSsi);
		cacheJob = EjbCodeCache.instance(fbSsi.getClassJob()).facade(fSsi);
		
		efData = fbSsi.ejbData();
	}
	
	@Override public void initMappings()
	{
		try
		{
			mapping = fSsi.fMapping(this.getClassJson(),this.getClassLocal());
		}
		catch (JeeslNotFoundException e) {throw new RuntimeException(e);}
	}
	
	public String sqlDeleteAll()
	{
		StringBuilder sb = new StringBuilder(); String alias=null; boolean newLine=false;
		SqlFactory.delete(sb,fbSsi.getClassData(),alias,newLine);
		SqlFactory.where(sb,alias,false,JeeslIoSsiData.Attributes.mapping,mapping, newLine);
		SqlFactory.semicolon(sb,newLine);
		return sb.toString();
	}
	
	@Override public void ignoreData(List<DATA> datas)
	{
		for(DATA d : datas)
		{
			if(!d.getLink().getCode().equals(JeeslIoSsiStatus.Code.linked.toString()))
			{
				ignoreData(d);
			}
		}
	}
	private void ignoreData(DATA data)
	{
		logger.info("Ignoring "+data.toString());
		try
		{
			data.setLocalId(null);
			data.setLink(cacheLink.ejb(JeeslIoSsiStatus.Code.ignore));
			fSsi.save(data);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
	}
	
	@Override public void unignoreData(List<DATA> datas)
	{
		for(DATA d : datas)
		{
			if(d.getLink().getCode().equals(JeeslIoSsiStatus.Code.ignore.toString()))
			{
				unignoreData(d);
			}
		}
	}
	private void unignoreData(DATA data)
	{
		logger.info("UnIgnoring "+data.toString());
		try
		{
			data.setLocalId(null);
			data.setLink(cacheLink.ejb(JeeslIoSsiStatus.Code.unlinked));
			fSsi.save(data);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
	}
	
	public void updateData(List<DATA> datas)
	{
		for(DATA data : datas)
		{
			try
			{
				if(data.getLink().equals(cacheLink.ejb(JeeslIoSsiStatus.Code.update)))
				{
					if(data.getLocalId()==null)
					{
						logger.warn("No local ID");
					}
					else
					{
						JSON json = JsonUtil.read(this.getClassJson(),data.getJson());
						updateData(data,json);
					}
				}
			}
			catch (IOException  e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void updateData(DATA data, JSON json)
	{
		logger.warn("NYI");
	}
	
	@Override public void evaluateData(List<DATA> datas)
	{
		if(ObjectUtils.isNotEmpty(datas))
		{
			for(DATA data : datas)
			{
				try
				{
					if(!data.getLink().equals(cacheLink.ejb(JeeslIoSsiStatus.Code.linked)))
					{
						evaluate(data,JsonUtil.read(this.getClassJson(),data.getJson()));
					}
				}
				catch (IOException e) {e.printStackTrace();}
			}
			try
			{
				fSsi.save(datas);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	protected abstract DATA evaluate(DATA data, JSON json);
	
	@Override public void linkData(List<DATA> datas)
	{
		for(DATA data : datas)
		{
			
			if(data.getLink().equals(cacheLink.ejb(JeeslIoSsiStatus.Code.possible)))
			{
				try {importData(data,JsonUtil.read(this.getClassJson(),data.getJson()));}
				catch (IOException | JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e){e.printStackTrace();}
			}
		}
	}
	protected abstract void importData(DATA data, JSON json) throws IOException,JeeslNotFoundException,JeeslConstraintViolationException,JeeslLockingException;
}