package org.jeesl.controller.processor.system.io.ssi;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jeesl.controller.monitoring.counter.JeeslEventCounter;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.txt.io.ssi.data.TxtIoSsiMappingFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSsiImport <SYSTEM extends JeeslIoSsiSystem<?,?>,
										MAPPING extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										LINK extends JeeslIoSsiStatus<?,?,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										JOB extends JeeslJobStatus<?,?,JOB,?>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiImport.class);
	
	protected EjbCodeCache<LINK> cacheLink;
	
	protected final TxtIoSsiMappingFactory<SYSTEM,MAPPING,ENTITY> tfMapping;
	
	protected EjbCodeCache<LINK> cDataStatus;
	protected EjbCodeCache<JOB> cJobStatus;
	
	protected File xlsDir,xlsFile;
	
	protected JeeslLogger jogger;
	protected JeeslEventCounter jec;
	
	protected int indexColumnStart, indexColumnEnd;
	protected int indexRowStart; public void setIndexRowStart(int indexRowStart) {this.indexRowStart = indexRowStart;}

	protected int indexSheet;
	
	public AbstractSsiImport(IoSsiDataFactoryBuilder<?,?,?,?,?,?,LINK,?,?,?,JOB> fBSsi, String localeCode)
	{
		tfMapping = new TxtIoSsiMappingFactory<>(localeCode);
		
		indexSheet = 0;
		
		cDataStatus = EjbCodeCache.instance(fBSsi.getClassStatus());
		cJobStatus = EjbCodeCache.instance(fBSsi.getClassJob());
		
		jec = new JeeslEventCounter("Import");
	}
	
	public void streamDir() throws IOException, UtilsConfigurationException
	{
		logger.info("Streaming "+xlsDir.getAbsolutePath());
		for(File f : xlsDir.listFiles())
		{
			logger.info(f.getAbsolutePath());
			if(f.getName().endsWith("xlsx") && !f.getName().startsWith("~$") && f.isFile()) 
			{
				this.xlsFile=f;
				streamFile();
			}
		}
	}
	
	protected abstract void streamFile() throws IOException, UtilsConfigurationException;
	protected abstract void row(Sheet sheet, int rowIndex, Row row) throws NumberFormatException, UtilsConfigurationException;
	protected void debug() {jec.ofx(System.out);}
}