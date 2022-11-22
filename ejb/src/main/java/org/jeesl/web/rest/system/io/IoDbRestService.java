package org.jeesl.web.rest.system.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.rs.io.db.JeeslIoDbRestExport;
import org.jeesl.api.rest.rs.io.db.JeeslIoDbRestImport;
import org.jeesl.api.rest.rs.io.db.JeeslIoDbRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.ejb.io.db.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.io.db.EjbIoDumpFactory;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.exlp.xml.io.Dir;
import net.sf.exlp.xml.io.File;

public class IoDbRestService<L extends JeeslLang,D extends JeeslDescription,
							SYSTEM extends JeeslIoSsiSystem<L,D>,
							DUMP extends JeeslDbDump<SYSTEM,FILE>,
							FILE extends JeeslDbDumpFile<DUMP,HOST,STATUS>,
							HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
							STATUS extends JeeslDbDumpStatus<L,D,STATUS,?>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslIoDbRestInterface,JeeslIoDbRestExport,JeeslIoDbRestImport
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestService.class);
	
	private final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS,?,?,?,?,?> fbDb;
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi;
	
	private final JeeslIoDbFacade<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fDb;
	
	private EjbIoDumpFactory<SYSTEM,DUMP> efDump;
	private EjbDbDumpFileFactory<DUMP,FILE,HOST,STATUS> efDumpFile;
	
	private final SYSTEM system;
	
	public IoDbRestService(IoDbFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS,?,?,?,?,?> fbDb,
							IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi,
							JeeslIoDbFacade<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fDb,
							SYSTEM system)
	{
		super(fDb,fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;
		this.fbSsi=fbSsi;
		this.fDb = fDb;
		
		this.system=system;
		
		efDump = fbDb.dump();
		efDumpFile = fbDb.dumpFile();
	}
	
//	@Override public Container exportSystemDbActivityState() {return xfContainer.build(fDb.allOrderedPosition(cCategory));}
	@Override public Container exportSystemIoDbDumpStatus() {return xfContainer.build(fDb.allOrderedPosition(fbDb.getClassDumpStatus()));}
	
	@Override public DataUpdate uploadDumps(Dir directory)
	{
		DataUpdateTracker dut = new DataUpdateTracker();
		
		STATUS eStatusStored;
		STATUS eStatusDeleted;
		
		try
		{
			eStatusStored = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbDumpFile.Status.stored);
			eStatusDeleted = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbDumpFile.Status.deleted);
		}
		catch (JeeslNotFoundException e) {dut.fail(e, true);return dut.toDataUpdate();}
		
		HOST eHost;
		try{eHost = fDb.fByCode(fbDb.getClassDumpHost(), directory.getCode());}
		catch (JeeslNotFoundException e)
		{
			try{eHost = fDb.persist(fbSsi.ejbHost().build(system,directory.getCode(),null));}
			catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
		}
		
		Set<FILE> setExisting = new HashSet<FILE>(fDb.fDumpFiles(eHost));
		
		for(File xFile : directory.getFile())
		{
			DUMP eDump;
			try{eDump = fDb.fByName(fbDb.getClassDump(), xFile.getName());}
			catch (JeeslNotFoundException e)
			{
				try
				{
					eDump = efDump.build(system,xFile);
					if(directory.isSetClassifier()) {eDump.setSystem(fDb.fByCode(fbSsi.getClassSystem(), directory.getClassifier()));}
					eDump = fDb.persist(eDump);
					
				}
				catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
				catch (JeeslNotFoundException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
			}
			FILE eFile;
			try {eFile = fDb.fDumpFile(eDump,eHost);}
			catch (JeeslNotFoundException e)
			{
				try {eFile = fDb.persist(efDumpFile.build(eDump,eHost,eStatusStored));}
				catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
			}
			
			try
			{
				if(setExisting.contains(eFile)){setExisting.remove(eFile);}
				eFile.setStatus(eStatusStored);
				eFile = fDb.save(eFile);
			}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);return dut.toDataUpdate();}
			catch (JeeslLockingException e) {dut.fail(e,true);return dut.toDataUpdate();}
		}
		
		for(FILE f : new ArrayList<FILE>(setExisting))
		{
			try
			{
				f.setStatus(eStatusDeleted);
				f = fDb.save(f);
			}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);}
			catch (JeeslLockingException e) {dut.fail(e,true);}
		}
		
		return dut.toDataUpdate();
	}
	
	@Override public DataUpdate importSystemDbActivityState(Aht states){logger.warn("NYI importSystemDbActivityState");return new DataUpdate();}
	@Override public DataUpdate importSystemIoDbDumpStatus(Container container){return importStatus(fbDb.getClassDumpStatus(),container,null);}
}