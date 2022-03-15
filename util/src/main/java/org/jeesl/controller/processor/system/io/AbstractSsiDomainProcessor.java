package org.jeesl.controller.processor.system.io;

import java.io.IOException;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.monitoring.counter.BucketSizeCounter;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiDataFactory;
import org.jeesl.interfaces.controller.processor.SsiMappingProcessor;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.JsonUtil;

public abstract class AbstractSsiDomainProcessor<L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
										LINK extends JeeslIoSsiLink<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										JSON extends Object
>
						implements SsiMappingProcessor<MAPPING,DATA,JSON>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiDomainProcessor.class);
	
	protected final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi;
	protected final JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,?> fSsi;
	
	protected final EjbIoSsiDataFactory<MAPPING,DATA,LINK> efData;
	
	protected final EjbCodeCache<LINK> cacheLink; public EjbCodeCache<LINK> getCacheLink() {return cacheLink;}
		
	protected MAPPING mapping; @Override public MAPPING getMapping() {return mapping;}
	protected BucketSizeCounter jec; public void setEventCounter(BucketSizeCounter jec) {this.jec = jec;}

	public AbstractSsiDomainProcessor(IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi,
									JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,?> fSsi)
	{
		this.fSsi=fSsi;
		this.fbSsi=fbSsi;
		
		jec = BucketSizeCounter.instance();
		cacheLink = new EjbCodeCache<>(fbSsi.getClassLink(),fSsi);
		
		efData = fbSsi.ejbData();
	}
	
	@Override public void ignoreData(List<DATA> datas)
	{
		for(DATA d : datas)
		{
			if(!d.getLink().getCode().equals(JeeslIoSsiLink.Code.linked.toString()))
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
			data.setLink(cacheLink.ejb(JeeslIoSsiLink.Code.ignore));
			fSsi.save(data);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
	}
	
	@Override public void unignoreData(List<DATA> datas)
	{
		for(DATA d : datas)
		{
			if(d.getLink().getCode().equals(JeeslIoSsiLink.Code.ignore.toString()))
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
			data.setLink(cacheLink.ejb(JeeslIoSsiLink.Code.unlinked));
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
				if(data.getLink().equals(cacheLink.ejb(JeeslIoSsiLink.Code.update)))
				{
					if(data.getLocalId()==null)
					{
						logger.warn("No local ID");
					}
					else
					{
						JSON json = JsonUtil.read(data.getJson(),this.getClassJson());
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
	protected abstract void updateData(DATA data, JSON json);
}