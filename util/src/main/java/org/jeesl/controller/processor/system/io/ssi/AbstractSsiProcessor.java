package org.jeesl.controller.processor.system.io.ssi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.monitoring.counter.JeeslEventCounter;
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
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.db.cache.EjbNonUniqueCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSsiProcessor<L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										CONTEXT extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<CONTEXT,ENTITY>,
										DATA extends JeeslIoSsiData<CONTEXT,STATUS,ERROR,JOB>,
										STATUS extends JeeslIoSsiStatus<L,D,STATUS,?>,
										ERROR extends JeeslIoSsiError<L,D,CONTEXT,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										JSON extends Object
>
						implements SsiMappingProcessor<CONTEXT,DATA,JSON>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiProcessor.class);
	
	protected final IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB> fbSsi;
	protected final JeeslIoSsiFacade<SYSTEM,CRED,CONTEXT,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB,?> fSsi; public JeeslIoSsiFacade<SYSTEM, CRED, CONTEXT, ATTRIBUTE, DATA, STATUS, ERROR, ENTITY, CLEANING, JOB, ?> getFacadeSsi() {return fSsi;}
	
	protected final EjbIoSsiDataFactory<CONTEXT,DATA,STATUS> efData;
	
	protected final EjbCodeCache<STATUS> cacheLink; public EjbCodeCache<STATUS> getCacheLink() {return cacheLink;}
	protected final EjbCodeCache<JOB> cacheJob; //public EjbCodeCache<JOB> getCacheJob() {return cacheJob;}
	protected final EjbNonUniqueCodeCache<ERROR> cacheError;
	
	protected CONTEXT context; @Override public CONTEXT getMapping() {return context;}
	private final String localeCode;
	protected JeeslEventCounter jec; public void setEventCounter(JeeslEventCounter jec) {this.jec = jec;}

	public AbstractSsiProcessor(String localeCode, IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB> fbSsi,
									JeeslIoSsiFacade<SYSTEM,CRED,CONTEXT,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB,?> fSsi)
	{
		this.localeCode=localeCode;
		this.fSsi=fSsi;
		this.fbSsi=fbSsi;
		
		this.loadContext();
		
		cacheError = EjbNonUniqueCodeCache.instance();
		cacheError.addAll(fSsi.allForParent(fbSsi.getClassError(),context));
		
		jec = JeeslEventCounter.instance();
		cacheLink = EjbCodeCache.instance(fbSsi.getClassStatus()).facade(fSsi);
		cacheJob = EjbCodeCache.instance(fbSsi.getClassJob()).facade(fSsi);
		
		efData = fbSsi.ejbData();
	}
	
	@Override public void loadContext()
	{
		try
		{
			context = fSsi.fSsiContext(this.getClassJson(),this.getClassLocal());
			logger.info("Using context "+context.toString());
		}
		catch (JeeslNotFoundException e) {throw new RuntimeException(e);}
	}
	
	public String sqlDeleteAll()
	{
		StringBuilder sb = new StringBuilder(); String alias=null; boolean newLine=false;
		SqlFactory.deleteFrom(sb,fbSsi.getClassData(),alias,newLine);
		SqlFactory.where(sb,alias,false,JeeslIoSsiData.Attributes.mapping,context, newLine);
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
			List<DATA> processed = new ArrayList<>();
			for(DATA data : datas)
			{
				try
				{
					if(cacheLink.notEquals(data.getLink(),JeeslIoSsiStatus.Code.linked))
					{
						DATA d = evaluate(data,JsonUtil.read(this.getClassJson(),data.getJson()));
						if(Objects.nonNull(d)) {processed.add(d);}
					}
				}
				catch (IOException e) {e.printStackTrace();}
			}
			try
			{
				if(ObjectUtils.isNotEmpty(processed))
				{
					fSsi.save(datas);
				}
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	protected DATA evaluate(DATA data, JSON json)
	{
		logger.warn("This method needs to be implemented in child class"); return data;
	}
	
	@Override public void linkData(List<DATA> datas)
	{
		logger.info("Linking "+datas.size()+" "+fbSsi.getClassData());
		for(DATA data : datas)
		{
			if(data.getLink().equals(cacheLink.ejb(JeeslIoSsiStatus.Code.possible)))
			{
				try {importData(data,JsonUtil.read(this.getClassJson(),data.getJson()));}
				catch (IOException | JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e){e.printStackTrace();}
			}
		}
	}
	protected void importData(DATA data, JSON json) throws IOException, JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.warn("This method needs to be implemented in child class");
	}
	
	protected <E extends Enum<E>> void applyError(StringBuilder sb, E code, DATA data, Object... substitutions)
	{
		ERROR error = cacheError.ejb(code);
		if(Objects.isNull(error)) {logger.warn("No Error defined for "+code);}
		if(Objects.isNull(data.getError())) {data.setError(error);}
		sb.append(" ").append(String.format(error.getDescription().get(localeCode).getLang(),substitutions));
	}
	
	@Override public void applyRefA(EjbWithId ejb, DATA data)
	{
		if(!this.getClassA().isAssignableFrom(ejb.getClass())) {throw new RuntimeException("Wrong Class for refA provided");}
		data.setRefA(ejb.getId());
	}
	@Override public void applyRefB(EjbWithId ejb, DATA data)
	{
		if(!this.getClassB().isAssignableFrom(ejb.getClass())) {throw new RuntimeException("Wrong Class for refB provided");}
		data.setRefB(ejb.getId());
	}
	@Override public void applyRefC(EjbWithId ejb, DATA data)
	{
		if(!this.getClassC().isAssignableFrom(ejb.getClass())) {throw new RuntimeException("Wrong Class for refC provided");}
		data.setRefC(ejb.getId());
	}
}