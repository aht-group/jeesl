package org.jeesl.controller.io.db.xml;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.controller.db.UtilsDbXmlInit.Priority;
import org.jeesl.model.xml.io.db.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDbRestInit extends UtilsDbXmlSeedUtil
{
	final static Logger logger = LoggerFactory.getLogger(AbstractDbRestInit.class);
	
	protected Configuration config;
	protected UtilsIdMapper idMapper;
	
	public AbstractDbRestInit(Db dbSeed, DataSource datasource, org.exlp.interfaces.system.property.Configuration config, UtilsIdMapper idMapper)
	{
		super(dbSeed, datasource);
//		this.config=config;
		this.idMapper=idMapper;
	}
	public AbstractDbRestInit(Db dbSeed, DataSource datasource, org.apache.commons.configuration.Configuration config, UtilsIdMapper idMapper)
	{
		super(dbSeed, datasource);
		this.config=config;
		this.idMapper=idMapper;
	}
	public AbstractDbRestInit(Db dbSeed, DataSource datasource, org.apache.commons.configuration2.Configuration config, UtilsIdMapper idMapper)
	{
		super(dbSeed, datasource);
		
		logger.error("NYI Configuration 2");
//		this.config=config;
//		this.idMapper=idMapper;
	}
	
	public void initFromXml(Priority priority) throws FileNotFoundException,JeeslConstraintViolationException, UtilsConfigurationException
	{
		switch(priority)
		{
			case statics: initStatics();break;
			case required: initRequired();break;
			case mandatory: initMandatory();break;
			case optional: initOptional();break;
			case A: initA();break;
			case B: initB();break;
			case C: initC();break;
			case D: initD();break;
			case E: initE();break;
		}
	}
	
	protected void initStatics() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initRequired() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initMandatory() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initOptional() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	
	protected void initA() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initB() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initC() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initD() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	protected void initE() throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException{}
	
	public static List<Priority> allPriorities()
	{
		List<Priority> list = new ArrayList<Priority>();
		list.add(Priority.statics);
		list.add(Priority.required);
		list.add(Priority.mandatory);
		list.add(Priority.optional);
		
		list.add(Priority.A);
		list.add(Priority.B);
		list.add(Priority.C);
		list.add(Priority.D);
		list.add(Priority.E);
		return list;
	}
}