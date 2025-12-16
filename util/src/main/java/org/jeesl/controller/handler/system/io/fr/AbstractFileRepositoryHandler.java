package org.jeesl.controller.handler.system.io.fr;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.exlp.model.xml.io.File;
import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.ejb.io.fr.EjbIoFrContainerFactory;
import org.jeesl.factory.ejb.io.fr.EjbIoFrMetaFactory;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.system.io.mail.core.TxtMimeTypeFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.report.format.JeeslZipReport;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationType;
import org.jeesl.interfaces.model.io.fr.JeeslFileStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageEngine;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.factory.xml.io.XmlDataFactory;
import net.sf.exlp.factory.xml.io.XmlFileFactory;

public abstract class AbstractFileRepositoryHandler<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									STORAGE extends JeeslFileStorage<L,D,SYSTEM,STYPE,ENGINE>,
									STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
									ENGINE extends JeeslFileStorageEngine<L,D,ENGINE,?>,
									CONTAINER extends JeeslFileContainer<STORAGE,META>,
									META extends JeeslFileMeta<D,CONTAINER,TYPE,RSTATUS>,
									TYPE extends JeeslFileType<L,D,TYPE,?>,
									REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
									RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
									RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>
//,CONSTRAINT extends JeeslConstraint<L,D,?,?,?,?,?,?>
>
					implements JeeslFileRepositoryHandler<LOC,STORAGE,CONTAINER,META>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractFileRepositoryHandler.class);

	protected boolean debugOnInfo; @Override public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public enum Mode{directSave,deferredSave}
	public enum ContainerInit {withTransaction,withoutTransaction}

	private Mode mode; public void setMode(Mode mode) {this.mode = mode;}
	private ContainerInit containerInit; public void setContainerInit(ContainerInit containerInit) {this.containerInit = containerInit;}
	protected final JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fFr;
	protected final IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fbFile;
	protected final JeeslFileRepositoryCallback callback;

	private final JeeslFileTypeHandler<META,TYPE> fth;
	protected final EjbDescriptionFactory<D> efDescription;
	protected final EjbIoFrContainerFactory<STORAGE,CONTAINER> efContainer;
	protected final EjbIoFrMetaFactory<CONTAINER,META,TYPE> efMeta;
	
	private Comparator<META> cpMeta; public void setComparatorMeta(Comparator<META> cpMeta) {this.cpMeta = cpMeta;}
	
	protected final List<META> metas; @Override public List<META> getMetas() {return metas;}
	protected final Map<META,File> mapDeferred; public Map<META, File> getMapDeferred() {return mapDeferred;}

	private final List<LOC> locales;

	private String zipName; public String getZipName() {return zipName;} public void setZipName(String zipName) {this.zipName = zipName;}
	private String zipPrefix; public String getZipPrefix() {return zipPrefix;} public void setZipPrefix(String zipPrefix) {this.zipPrefix = zipPrefix;}

	private STORAGE storage; @Override public STORAGE getStorage() {return storage;} @Override
	public void setStorage(STORAGE storage) {this.storage=storage;}
	protected CONTAINER container; @Override public CONTAINER getContainer() {return container;}
	protected META meta; public META getMeta() {return meta;} public void setMeta(META meta) {this.meta = meta;}
	private LOC locale; public LOC getLocale() {return locale;} @Override public void setLocale(LOC locale) {this.locale = locale;}

	protected File xmlFile;
	private String fileName; public String getFileName() {return fileName;} public void setFileName(String fileName) {this.fileName = fileName;}


	private boolean showInlineUpload; public boolean isShowInlineUpload() {return showInlineUpload;}

	private boolean allowUpload; public boolean isAllowUpload() {return allowUpload;}
	private boolean allowChanges; public boolean isAllowChanges() {return allowChanges;}
	private boolean allowChangeName; public boolean isAllowChangeName() {return allowChangeName;}
	private boolean allowChangeDescription; public boolean isAllowChangeDescription() {return allowChangeDescription;}

	public AbstractFileRepositoryHandler(JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fFr,
								IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fbFile,
								JeeslFileRepositoryCallback callback)
	{
		this.fFr=fFr;
		this.fbFile=fbFile;
		this.callback=callback;
		debugOnInfo = false;

		containerInit = ContainerInit.withoutTransaction;
		mode = Mode.directSave;

		fth = new JeeslFileTypeHandler<META,TYPE>(fbFile,fFr);
		efDescription = EjbDescriptionFactory.instance(fbFile.getClassD());
		efContainer = fbFile.ejbContainer();
		efMeta = fbFile.ejbMeta();
		metas = new ArrayList<>();
		mapDeferred = new HashMap<>();
		locales = new ArrayList<>();
		zipName = "zip.zip";

		allowChanges = true;
		allowChangeName = false;
		allowChangeDescription = false;

		showInlineUpload = false;
	}

	public void allowControls(boolean upload, boolean name, boolean description)
	{
		this.allowUpload=upload;
		allowChangeName = name;
		allowChangeDescription = description;
		allowChanges = allowChangeName || allowChangeDescription;
	}

	@Override
	public void setLocales(List<LOC> locales)
	{
		this.locales.clear();
		locale = null;
		this.locales.addAll(locales);
		if(locales.size()==1) {locale = locales.get(0);}
	}

	public <E extends Enum<E>> void initStorage(E code)
	{
		try
		{
			if(fFr==null) {throw new JeeslNotFoundException("Facade is null");}
			setStorage(fFr.fByCode(fbFile.getClassStorage(), code));
			if(debugOnInfo)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("Storage: "+storage.getCode());
				sb.append(" FileSizeLimit: "+storage.getFileSizeLimit());
				logger.info(sb.toString());
			}
		}
		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
	}

	@Override public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> void initSilent(W with)
	{
		try {this.init(with);}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
	}
	@Override public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> void init(W with) throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean reset = true;
		this.init(storage,with,reset);
	}
	@Override public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> void init(STORAGE initForStorage, W with) throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean reset = true;
		init(initForStorage,with,reset);
	}
	private <W extends JeeslWithFileRepositoryContainer<CONTAINER>> void init(STORAGE initForStorage, W with, boolean reset) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(reset)
		{	// The reset is required for a special case in deferredMode
			reset(true,true);
		}

		if(Objects.isNull(with.getFrContainer()))
		{
			container = efContainer.build(initForStorage);
			if(EjbIdFactory.isSaved(with))
			{
				switch (containerInit)
				{
					case withoutTransaction:	container = fFr.save(container);break;
					case withTransaction: 		container = fFr.saveTransaction(container);break;
				}
				if(debugOnInfo) {logger.info("Saved container "+container.toString());}
				if(callback!=null)
				{
					callback.callbackFrContainerSaved(with);
				}
			}
		}
		else
		{
			container = with.getFrContainer();
			if(reset) {reload(true);}
		}
		if(debugOnInfo) {logger.info(with.toString()+" is inititialized with container:"+container.toString());}
	}
	public void init(CONTAINER container)
	{
		this.container=container;
		this.reload();
	}

	@Override
	public void reset() {reset(true,true);}
	public void cancelMeta() {reset(false,true);}

	private void reset(boolean rMetas, boolean rMeta)
	{
		if(rMetas) {metas.clear();}
		if(rMeta) {meta=null;}
	}

	public void reload() {reload(false);}
	public void reload(boolean forceFind)
	{
		if(mode.equals(Mode.directSave) || forceFind)
		{
			container = fFr.find(fbFile.getClassContainer(), container);
			metas.clear();
			metas.addAll(fFr.allForParent(fbFile.getClassMeta(),container));
		}
		else
		{

		}
		
		if(Objects.nonNull(cpMeta)) {Collections.sort(metas,cpMeta);}
		if(debugOnInfo) {logger.info("Reloaded "+fbFile.getClassMeta().getSimpleName()+":"+metas.size()+" for container:"+container.toString());}
	}

	public void addFileOverlay()
	{
		if(debugOnInfo) {logger.info("Adding File Overlay");}
		addFile();
		showInlineUpload = false;
	}

	public void addFileInline()
	{
		if(debugOnInfo) {logger.info("Adding File Inline");}
		this.addFile();
		showInlineUpload = true;
	}

	private void addFile()
	{
		if(debugOnInfo) {logger.info("Adding File");}
		xmlFile = XmlFileFactory.build("");
	}
	public void addFile(Path path) throws IOException {addFile(path.getFileName().toString(), Files.readAllBytes(path), null);}
	public void addFile(java.io.File f) throws FileNotFoundException, IOException {addFile(f.getName(), IOUtils.toByteArray(new FileInputStream(f)), null);}
	public void addFile(String name, byte[] bytes) {addFile(name, bytes, null);}
	public void addFile(String name, InputStream is, String category) throws FileNotFoundException, IOException {addFile(name, IOUtils.toByteArray(is), null);}
	@Override public void addFile(String name, byte[] bytes, String category)
	{
		this.addFile();
		xmlFile.setName(name);
		xmlFile.setSize(Integer.valueOf(bytes.length).longValue());
		xmlFile.setData(XmlDataFactory.build(bytes));
		meta = efMeta.build(container,name,bytes.length,LocalDateTime.now());
		meta.setCategory(category);
		if(mode.equals(Mode.deferredSave)) {EjbIdFactory.setNextNegativeId(meta,metas);}
		fth.updateType(meta);
	}

	public final void handleFileUpload(FileUploadEvent event) throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info("Handling FileUpload: "+event.getFile().getFileName());}
		xmlFile.setName(event.getFile().getFileName());
		xmlFile.setSize(event.getFile().getSize());
		xmlFile.setData(XmlDataFactory.build(event.getFile().getContent()));
		meta = efMeta.build(container,event.getFile().getFileName(),event.getFile().getSize(),LocalDateTime.now());
		meta.setType(fFr.fByCode(fbFile.getClassType(), JeeslFileType.Code.unknown));
		meta.setDescription(efDescription.buildEmpty(locales));

		this.handledFileUpload();
    }
	protected void handledFileUpload()
	{
		if(debugOnInfo) {logger.info("handledFileUpload (start): "+meta.toString());}
		fth.updateType(meta);
		showInlineUpload = false;
		if(debugOnInfo) {logger.info("handledFileUpload (end): "+meta.toString());}
	}

	public void selectFile()
	{
		if(debugOnInfo) {logger.info("selectFile "+meta.toString());}
		fileName = meta.getFileName();
		meta = efDescription.persistMissingLangs(fFr,locales,meta);
		if(Objects.nonNull(callback)) {callback.callbackFrMetaSelected();}
	}

	@Override public META saveFile() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("Saving: "+xmlFile.getName()+" to "+storage.getCode()+" with "+mode+" mode.");}
		if(mode.equals(Mode.directSave))
		{
			if(debugOnInfo) {logger.info("Saving to FR "+storage.toString());}
			meta = fFr.saveToFileRepository(meta,xmlFile.getData().getValue());
			this.reload();
		}
		else
		{
			metas.add(meta);
			mapDeferred.put(meta,xmlFile);
		}

		if(debugOnInfo) {logger.info("Saved");}

		META m = meta;
		reset(false,true);
		return m;
    }
	public void saveMeta() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("save meta "+meta.toString());}
		if(Objects.nonNull(fileName))
		{
			if(FilenameUtils.getExtension(fileName).equals(FilenameUtils.getExtension(meta.getFileName())))
			{
				meta.setFileName(fileName);
			}
			else {meta.setFileName(fileName+"."+FilenameUtils.getExtension(meta.getFileName()));}
		}

		if(mode.equals(Mode.directSave))
		{
			meta = fFr.save(meta);
		}
		else
		{
			if(metas.contains(meta)) {metas.remove(meta);}
			metas.add(meta);
		}

		fileName = meta.getFileName();
		reload();
	}
	@Override public void saveThreadsafe(CONTAINER c, String name, byte[] bytes, String category) throws JeeslConstraintViolationException, JeeslLockingException
	{
		META m = efMeta.build(c,name,bytes.length,LocalDateTime.now());
		m.setCategory(category);
		fth.updateType(m);
		m = fFr.saveToFileRepository(m,bytes);
	}

	@Override
	public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> void saveDeferred(W with) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("Saving Defrred "+metas.size());}
		this.init(storage,with,false);
		List<META> handlings = new ArrayList<>();
		handlings.addAll(metas);
		if(debugOnInfo) {logger.info("saveDeferred 2 "+metas.size()+"-"+handlings.size());}
		metas.clear();
		if(debugOnInfo) {logger.info("saveDeferred 3 "+metas.size()+"-"+handlings.size());}
		for(META m : handlings)
		{
			if(debugOnInfo) {logger.info("Save Deferred: "+m.toString());}
			if(EjbIdFactory.isUnSaved(m))
			{
				File xml = mapDeferred.get(m);
				mapDeferred.remove(m);
				m.setContainer(container);
				EjbIdFactory.negativeToZero(m);
				m = fFr.saveToFileRepository(m,xml.getData().getValue());
			}
			else
			{
				m = fFr.save(m);
			}
			metas.add(m);
		}
		reload(true);
	}

	@Override public void deleteFile() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("DELETING: "+meta.toString());}
		fFr.delteFileFromRepository(meta);
		this.reset(true,true);
		this.reload();
	}

	public byte[] zip() throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zof = new ZipOutputStream(bos);

		for(META m : metas)
		{
			StringBuilder sb = new StringBuilder();
			if(zipPrefix!=null && zipPrefix.length()>0)
			{
				sb.append(zipPrefix).append("/");
			}
			sb.append(m.getFileName());

			ZipEntry ze = new ZipEntry(sb.toString());
			zof.putNextEntry(ze);

			zof.write(fFr.loadFromFileRepository(m));

			zof.closeEntry();
		}
		zof.close();
		bos.close();
		return bos.toByteArray();
	}

	@Override public InputStream download(META m) throws JeeslNotFoundException
	{
		if(mode.equals(Mode.directSave) || EjbIdFactory.isSaved(m))
		{
			return new SelfCleaningByteArrayInputStream(fFr.loadFromFileRepository(m));
		}
		else
		{
			return new SelfCleaningByteArrayInputStream(mapDeferred.get(m).getData().getValue());
		}
	}

	protected InputStream toInputStream() throws JeeslNotFoundException
	{
		return download(meta);
	}

	public final StreamedContent fileStream() throws Exception
	{
		InputStream is = this.toInputStream();
		return DefaultStreamedContent.builder().contentType(TxtMimeTypeFactory.build(meta.getFileName())).stream(()->is).name(meta.getFileName()).build();
	}

	public final StreamedContent zipStream() throws Exception
	{
		InputStream is = new SelfCleaningByteArrayInputStream(this.zip());
		return DefaultStreamedContent.builder().contentType(JeeslZipReport.mimeType).stream(()->is).name(this.getZipName()).build();
	}

	@Override public void copyTo(JeeslFileRepositoryHandler<LOC,STORAGE,CONTAINER,META> target) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info("Copy To");
		for(META oldMeta : metas)
		{
			META newMeta = efMeta.build(target.getContainer(), oldMeta.getFileName(), oldMeta.getSize(), oldMeta.getRecord());
			newMeta.setCategory(oldMeta.getCategory());
			newMeta.setMd5Hash(oldMeta.getMd5Hash());
			newMeta.setType(oldMeta.getType());
			fFr.saveToFileRepository(newMeta, fFr.loadFromFileRepository(oldMeta));
		}
	}

	public void moveTo1(STORAGE storage) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		for(META m : metas)
		{
			byte[] bytes = fFr.loadFromFileRepository(m);
			fFr.saveToFileRepository(m,bytes);
		}
	}

	public void reorderMetas() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.warn("NYI until META implements position");
//		PositionListReorderer.reorder(fFr,metas);
	}
}