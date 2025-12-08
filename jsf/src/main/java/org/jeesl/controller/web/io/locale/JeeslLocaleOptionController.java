package org.jeesl.controller.web.io.locale;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicComponentFactory;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.label.JeeslOptionTableCallback;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithColour;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithImage;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithLocked;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImageAlt;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.JeeslOptionUploadable;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.ui.edit.UiEditBooleanHandler;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.db.updater.JeeslDbGraphicUpdater;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.ejb.JeeslInterfaceAnnotationQuery;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslLocaleOptionController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
										GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>>
			extends AbstractJeeslLocaleWebController<L,D,LOC>
			implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(JeeslLocaleOptionController.class);
	private static final long serialVersionUID = 1L;
	
	protected JeeslIoGraphicFacade<?,G,GT,GC,GS> fGraphic;
	
	protected final IoLocaleFactoryBuilder<L,D,LOC> fbStatus;
	protected final SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg;
	protected final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision;
	
	private final JeeslOptionTableCallback callback;
	
	private final UiEditBooleanHandler ehUpload; public UiEditBooleanHandler getEhUpload() {return ehUpload;}

	protected final Map<EjbWithPosition,RE> mapEntity; public Map<EjbWithPosition, RE> getMapEntity() {return mapEntity;}
	
	protected final List<EjbWithPosition> categories; public List<EjbWithPosition> getCategories(){return categories;}
	protected List<EjbWithPosition> parents; public List<EjbWithPosition> getParents(){return parents;}
	protected List<EjbWithPosition> items; public List<EjbWithPosition> getItems() {return items;}
	protected List<GT> graphicTypes; public List<GT> getGraphicTypes() {return graphicTypes;}
	protected List<GS> graphicStyles; public List<GS> getGraphicStyles() {return graphicStyles;}
	protected List<GC> figures; public List<GC> getFigures() {return figures;}
	
	protected boolean supportsSymbol; public boolean getSupportsSymbol(){return supportsSymbol;}
	protected boolean supportsLocked; public boolean isSupportsLocked() {return supportsLocked;}
	protected boolean supportsDownload; public boolean getSupportsDownload(){return supportsDownload;}
	protected boolean supportsColour; public boolean getSupportsColour(){return supportsColour;}
	protected boolean logOnInfo; public boolean isLogOnInfo() {return logOnInfo;} public void setLogOnInfo(boolean logOnInfo) {this.logOnInfo = logOnInfo;}
	
	protected final EjbGraphicFactory<L,D,G,GT,GS> efGraphic;
	protected final EjbGraphicComponentFactory<G,GC,GS> efFigure;

	@SuppressWarnings("rawtypes") protected Class clParent;
	protected long parentId; public long getParentId(){return parentId;} public void setParentId(long parentId) {this.parentId = parentId;}
	
	protected Object category; public Object getCategory() {return category;} public void setCategory(Object category) {this.category = category;}
	protected Object status; public Object getStatus() {return status;} public void setStatus(Object status) {this.status = status;}
	protected G graphic; public G getGraphic() {return graphic;} public void setGraphic(G graphic) {this.graphic = graphic;}
	protected RE entity; public RE getEntity() {return entity;}
	protected GC component; public GC getComponent() {return component;} public void setComponent(GC component) {this.component = component;}

	@SuppressWarnings("rawtypes")
	protected Class optionClass; public Class<?> getOptionClass() {return optionClass;}
	
	protected long index;
	protected Map<Long,Boolean> allowAdditionalElements; public Map<Long, Boolean> getAllowAdditionalElements(){return allowAdditionalElements;}

	private final JeeslDbGraphicUpdater<G,GT> dbuGraphic;

	protected boolean allowSvg; public boolean isAllowSvg() {return allowSvg;}
	private boolean showDescription; public boolean isShowDescription() {return showDescription;}

	private boolean supportsUpload; public boolean getSupportsUpload(){return supportsUpload;}	

	protected boolean supportsImage; public boolean getSupportsImage() {return supportsImage;}
	protected boolean supportsGraphic; public boolean getSupportsGraphic() {return supportsGraphic;}

	protected boolean uiAllowAdd; public boolean isUiAllowAdd() {return uiAllowAdd;}
	protected boolean uiAllowUpload; public boolean isUiAllowUpload() {return uiAllowUpload;}
	protected boolean uiAllowReorder; public boolean getUiAllowReorder() {return uiAllowReorder;}
	protected boolean uiAllowSave; public boolean getUiAllowSave() {return uiAllowSave;}
	protected boolean uiAllowRemove; public boolean isUiAllowRemove() {return uiAllowRemove;}
	protected boolean uiAllowCode; public boolean isUiAllowCode() {return uiAllowCode;}
	
	private byte[] previewBytes; public byte[] getPreviewBytes() {return previewBytes;}

	public JeeslLocaleOptionController(JeeslOptionTableCallback callback,
									IoLocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
	{
		super(fbRevision.getClassL(),fbRevision.getClassD());
		
		this.callback=callback;
		this.fbStatus=fbStatus;
		this.fbSvg=fbSvg;
		this.fbRevision=fbRevision;
		
		efGraphic = fbSvg.efGraphic();
		efFigure = fbSvg.efFigure();

		ehUpload = UiEditBooleanHandler.instance();
		
		index=1;

		
		status = null;
		allowAdditionalElements = new Hashtable<Long,Boolean>();

		mapEntity = new HashMap<>();
		categories = new ArrayList<EjbWithPosition>();
		
		logOnInfo = false;
		
		dbuGraphic = new JeeslDbGraphicUpdater<>(fbSvg);

		showDescription = false;

		uiAllowAdd = true;
		uiAllowUpload = false;
		uiAllowReorder = true;
		uiAllowSave = true;
		uiAllowRemove = true;
		uiAllowCode = true;
	}

	public void postConstructOptionTable(JeeslLocaleProvider<LOC> lp,
											JeeslIoGraphicFacade<?,G,GT,GC,GS> fGraphic,
											JeeslFacesMessageBean bMessage)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fGraphic=fGraphic;
		dbuGraphic.setFacade(fGraphic);

		graphicTypes = fGraphic.allOrderedPositionVisible(fbSvg.getClassGraphicType());
		graphicStyles = fGraphic.allOrderedPositionVisible(fbSvg.getClassFigureStyle());
	}

	protected void reset(boolean rEntity)
	{
		if(rEntity) {entity=null;}
	}
	
	public void loadEntities()
	{
		for(EjbWithPosition p : categories)
		{
			try
			{
				String fqcn = ((EjbWithImage)p).getImage();
				RE e = fGraphic.fByCode(fbRevision.getClassEntity(),fqcn);
				mapEntity.put(p,e);
			}
			catch (JeeslNotFoundException e) {}
		}
	}
	
	public void cancelStatus() {reset(true,true,true);}
	protected void reset(boolean rStatus, boolean rFigure, boolean rUpload)
	{
		if(rStatus) {status=null;}
		if(rFigure) {component=null;}
		if(rUpload) {ehUpload.denyEdit();}
	}

	protected void updateSecurity(JeeslJsfSecurityHandler jsfSecurityHandler, String viewCode)
	{
		logger.warn("NYI");
	}

	protected void updateUiForCategory()
	{
		supportsSymbol = EjbWithSymbol.class.isAssignableFrom(optionClass);
		supportsLocked = EjbWithLocked.class.isAssignableFrom(optionClass);
		supportsDownload = JeeslInterfaceAnnotationQuery.isAnnotationPresent(DownloadJeeslData.class,optionClass);
		supportsColour = JeeslStatusWithColour.class.isAssignableFrom(optionClass);

		supportsUpload = JeeslOptionUploadable.class.isAssignableFrom(optionClass);
		
		supportsImage = JeeslStatusWithImage.class.isAssignableFrom(optionClass);
		supportsGraphic = EjbWithGraphic.class.isAssignableFrom(optionClass);
	}

	protected void debugUi(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("Option Tables Settings");
			logger.info("\tImage? "+supportsImage);
			logger.info("\tGraphic? "+supportsGraphic);
			logger.info("\tSymbol? "+supportsSymbol);
			logger.info("\tLocked? "+supportsLocked);
			logger.info("\tsupportsDownload? "+supportsDownload);
			logger.info("\t"+JeeslOptionUploadable.class.getSimpleName()+"? "+supportsUpload);
		}
	}

	public void toggleDescription()
	{
		showDescription = !showDescription;
	}

	public void selectCategory() throws ClassNotFoundException{selectCategory(true);}
	@SuppressWarnings("unchecked")
	public void selectCategory(boolean reset) throws ClassNotFoundException
	{
		reset(true);
		if(category==null) {logger.error("selectCategory, but category is NULL");}

		StringBuilder sb = new StringBuilder();
		sb.append("selectCategory");
		sb.append(" ").append(((EjbWithCode)category).getCode());
		sb.append(" (").append(((EjbWithImageAlt)category).getImageAlt()).append(")");
		sb.append(" allowAdditionalElements:").append(allowAdditionalElements.get(((EjbWithId)category).getId()));
		logger.info(sb.toString());

		optionClass = Class.forName(((EjbWithImage)category).getImage());
		updateUiForCategory();

		try {entity = fGraphic.fByCode(fbRevision.getClassEntity(), optionClass.getName());}
		catch (JeeslNotFoundException e) {}

		

		if(((EjbWithImageAlt)category).getImageAlt()!=null)
		{
            clParent = Class.forName(((EjbWithImageAlt)category).getImageAlt())
            				.asSubclass(fbStatus.getClassStatusGlobal())
            				.asSubclass(EjbWithPosition.class);
            
            parents = fGraphic.allOrderedPosition(clParent);
            logger.info(optionClass.getSimpleName()+" "+parents.size());
		}
		else
		{
			clParent=null;
			parents=null;
		}
		reloadStatusEntries();
		if(reset){this.reset(true,true,true);}
		debugUi(true);
	}

	@SuppressWarnings("unchecked")
	protected void reloadStatusEntries()
	{
		items = fGraphic.allOrderedPosition(optionClass);
	}

	@SuppressWarnings("unchecked")
	public void add() throws JeeslConstraintViolationException, InstantiationException, IllegalAccessException, JeeslNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		logger.debug("add");
		
		status = optionClass.getDeclaredConstructor().newInstance();
		((EjbWithId)status).setId(0);
		((EjbWithCode)status).setCode("enter code");
		((EjbWithLang<L>)status).setName(efLang.build(lp));
		((EjbWithDescription<D>)status).setDescription(efDescription.build(lp));

		if(supportsGraphic)
		{
			GT type = fGraphic.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol.toString());
			GS style = fGraphic.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicShape.Code.shapeCircle.toString());
			graphic = efGraphic.buildSymbol(type, style);
			((EjbWithGraphic<G>)status).setGraphic(graphic);
			previewBytes = new byte[0];
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void selectStatus() throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		this.reset(false, true, true);
		figures = null; component=null;
		status = fGraphic.find(optionClass,(EjbWithId)status);
		status = fGraphic.loadGraphic(optionClass,(EjbWithId)status);
		logger.debug("selectStatus");
		status = efLang.persistMissingLangs(fGraphic,lp,(EjbWithLang)status);
		status = efDescription.persistMissingLangs(fGraphic,lp,(EjbWithDescription)status);

		if(((EjbWithParent)status).getParent()!=null)
		{
			parentId=((EjbWithParent)status).getParent().getId();
		}

		if(supportsGraphic)
		{
			if(((EjbWithGraphic<G>)status).getGraphic()==null)
			{
				logger.info("Need to create a graphic entity for this status");
				GT type = fGraphic.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol);
				GS style = fGraphic.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicShape.Code.shapeCircle);
				graphic = fGraphic.persist(efGraphic.buildSymbol(type, style));
				((EjbWithGraphic<G>)status).setGraphic(graphic);
				status = fGraphic.update(status);
			}
			graphic = ((EjbWithGraphic<G>)status).getGraphic();

			this.reloadFigures();
			previewBytes = new byte[0];
			if(ObjectUtils.isNotEmpty(graphic.getData()))
			{
				previewBytes = graphic.getData();
				logger.info("Preparing Preview with "+previewBytes.length);
				try {
					FileUtils.writeByteArrayToFile(Paths.get("/Volumes/ramdisk").resolve("test.svg").toFile(), previewBytes);
				}
				catch (IOException e) {e.printStackTrace();}
			}
		}

		debugUi(false);
		pageFlowPrimarySelect(status);
	}

	@SuppressWarnings("unchecked")
	public void save() throws ClassNotFoundException, JeeslNotFoundException
    {
		boolean debugSave=true;
		try
		{
            if(clParent!=null && parentId>0)
            {
            	((EjbWithParent)status).setParent((EjbWithCode)fGraphic.find(clParent, parentId));
            }
        	if(supportsGraphic && graphic!=null)
            {
        		graphic.setType(fGraphic.find(fbSvg.getClassGraphicType(), ((EjbWithGraphic<G>)status).getGraphic().getType()));
            	if(graphic.getStyle()!=null){graphic.setStyle(fGraphic.find(fbSvg.getClassFigureStyle(), ((EjbWithGraphic<G>)status).getGraphic().getStyle()));}
            	((EjbWithGraphic<G>)status).setGraphic(graphic);
            }

        	if(debugSave){logger.info("Saving "+status.getClass().getSimpleName()+" "+status.toString());}
			status = fGraphic.save((EjbSaveable)status);
			status = fGraphic.loadGraphic(optionClass,(EjbWithId)status);
			
			previewBytes = new byte[0];
			if(supportsGraphic)
			{
				graphic = ((EjbWithGraphic<G>)status).getGraphic();
				if(debugSave){logger.info("Saved "+graphic.getClass().getSimpleName()+" "+graphic.toString());}
				
				if(ObjectUtils.isNotEmpty(graphic.getData())) {previewBytes = graphic.getData();}
			}
			this.reloadFigures();
			if(debugSave){logger.info("Saved "+status.getClass().getSimpleName()+" "+status.toString());}

			updateAppScopeBean2(status);
			if(Objects.nonNull(callback)) {callback.callbackStatusSaved(status);}
			selectCategory(false);
			bMessage.growlSaved((EjbSaveable)status);
		}
		catch (JeeslConstraintViolationException e)
		{
			logger.error(JeeslConstraintViolationException.class.getSimpleName()+" "+e.getMessage());
			bMessage.constraintInUse(null);
		}
		catch (JeeslLockingException e)
		{
			logger.error(JeeslLockingException.class.getSimpleName()+" "+e.getMessage());
			bMessage.constraintInUse(null);
		}
		
	}

	public void rm() throws ClassNotFoundException
	{
		try
		{
			fGraphic.rm((EjbRemoveable)status);
			bMessage.growlDeleted((EjbRemoveable)status);
			updateAppScopeBean2(status);
			status=null;
			selectCategory();
			
		}
		catch (JeeslConstraintViolationException e)
		{
			bMessage.constraintInUse(null);
		}
	}

	public void cancelFigure() {this.reset(false,true,true);reloadFigures();}
//	private void reset(boolean rStatus, boolean rFigure)
//	{
//		if(rStatus){status=null;}
//		if(rFigure){figure=null;}
//	}

	protected void updateAppScopeBean2(Object o) {}

	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic, items);}
	public void reorderFigures() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic,figures);}

	@SuppressWarnings("unchecked")
	public void handleFileUpload(FileUploadEvent event)
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		previewBytes = file.getContent();
		((EjbWithGraphic<G>)status).getGraphic().setData(previewBytes);
	}

	public InputStream previewIs() throws JeeslNotFoundException
	{
		if(Objects.nonNull(previewBytes))
		{
			logger.info(InputStream.class.getSimpleName()+" "+previewBytes.length);
			return new ByteArrayInputStream(previewBytes);
		}
		else
		{
			logger.info("Returning empty stream");
			return new ByteArrayInputStream(new byte[0]);
		}
	}
	
//	@Override
	@SuppressWarnings("unchecked")
	public void changeGraphicType()
	{
		((EjbWithGraphic<G>)status).getGraphic().setType(fGraphic.find(fbSvg.getClassGraphicType(), ((EjbWithGraphic<G>)status).getGraphic().getType()));
		logger.info("changeGraphicType to "+((EjbWithGraphic<G>)status).getGraphic().getType().getCode());
	}

	private void reloadFigures()
	{
		figures = fGraphic.allForParent(fbSvg.getClassFigure(),graphic);
	}

	public void addFigure()
	{
		logger.info("Add "+fbSvg.getClassFigure().getSimpleName());
		component = efFigure.build(graphic);
	}

	public void selectComponent()
	{
		logger.info("selectComponent");
		logger.info(AbstractLogMessage.selectEntity(component));
	}

	public void saveFigure() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Select "+component.toString());
		component.setShape(fGraphic.find(fbSvg.getClassFigureStyle(),component.getShape()));
		component = fGraphic.save(component);
		reloadFigures();
	}

	public void deleteFigure() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fGraphic.rm(component);
		reset(false,true,true);
		reloadFigures();
	}

	//Revision
	public void pageFlowPrimarySelect(Object revision) {}
	public void pageFlowPrimaryCancel() {}
	public void pageFlowPrimarySave(Object revision) {}
	public void pageFlowPrimaryAdd() {}


	//JEESL REST DATA
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <S extends JeeslStatus, W extends EjbWithCodeGraphic<G>> void downloadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Downloading REST");

		String fqcn = ((EjbWithImage)category).getImage();
		Class<?> c = Class.forName(fqcn);
		Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslData.class,c);
		
		Class<S> cS = (Class<S>)Class.forName(fqcn).asSubclass(JeeslStatus.class);
		Class<W> cW = (Class<W>)Class.forName(fqcn).asSubclass(EjbWithCodeGraphic.class);

		Container xml = this.downloadData(i.getName());

		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
		
        asdi.setStatusEjbFactory(EjbStatusFactory.instance(cS,cL,cD,EjbCodeFactory.toListCode(lp.getLocales())));
        asdi.setFacade(fGraphic);
        DataUpdate dataUpdate = asdi.iuStatus(xml.getStatus(),cS,cL,clParent);
        asdi.deleteUnusedStatus(cS, cL, cD);
//        JaxbUtil.info(dataUpdate);

        dbuGraphic.update(cW,xml.getStatus());

        selectCategory();
	}
	
	protected Container downloadData(String iFqcn) throws UtilsConfigurationException
	{
		return callback.rest(iFqcn).exportStatus(iFqcn);
	}
	
	@SuppressWarnings({"rawtypes" })
	public <S extends JeeslStatus, W extends EjbWithCodeGraphic<G>> void uploadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Uploading REST");

		Class<?> c = Class.forName(entity.getCode());
		Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslData.class,c);

		Container xml = callback.rest(i.getName()).exportStatus(i.getName());
	}
}