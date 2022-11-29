package org.jeesl.controller.processor.system.io.ssi;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jeesl.controller.monitoring.counter.BucketSizeCounter;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.txt.io.ssi.data.TxtIoSsiMappingFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSsiImport <SYSTEM extends JeeslIoSsiSystem<?,?>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										LINK extends JeeslIoSsiLink<?,?,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiImport.class);
	
	protected EjbCodeCache<LINK> cacheLink;
	
	protected final TxtIoSsiMappingFactory<SYSTEM,MAPPING,ENTITY> tfMapping;
	
	protected File xlsDir,xlsFile;
	
	protected JeeslLogger jogger;
	protected BucketSizeCounter jec;
	
	protected int indexColumnStart, indexColumnEnd;
	protected int indexRowStart; public void setIndexRowStart(int indexRowStart) {this.indexRowStart = indexRowStart;}

	protected int indexSheet;
	
	public AbstractSsiImport(String localeCode)
	{
		tfMapping = new TxtIoSsiMappingFactory<>(localeCode);
		
		indexSheet = 0;
		
		jec = new BucketSizeCounter("Import");
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