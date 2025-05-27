package org.jeesl.web.mbean.prototype.system.locale;

import java.io.Serializable;

import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithImage;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphicFigure;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImageAlt;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.JeeslOptionUploadable;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.db.updater.JeeslDbGraphicUpdater;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.ejb.JeeslInterfaceAnnotationQuery;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTableGlobalBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
										GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>>
			extends AbstractTableBean<L,D,LOC,G,GT,GC,GS,RE>
			implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractTableGlobalBean.class);
	private static final long serialVersionUID = 1L;

	private final JeeslDbGraphicUpdater<G,GT> dbuGraphic;

	protected boolean allowSvg; public boolean isAllowSvg() {return allowSvg;}
	private boolean showDescription; public boolean isShowDescription() {return showDescription;}

	private boolean supportsUpload; public boolean getSupportsUpload(){return supportsUpload;}	

	protected boolean supportsImage; public boolean getSupportsImage() {return supportsImage;}
	protected boolean supportsGraphic; public boolean getSupportsGraphic() {return supportsGraphic;}
	protected boolean supportsComponents; public boolean isSupportsComponents() {return supportsComponents;}

	public AbstractTableGlobalBean(
									IoLocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
	{
		super(fbStatus,fbSvg,fbRevision);
		
		dbuGraphic = new JeeslDbGraphicUpdater<>(fbSvg);

		showDescription = false;
//		hasDeveloperAction = false;
//		hasAdministratorAction = true;
//		hasTranslatorAction = true;

//		status = null;
//		allowAdditionalElements = new Hashtable<Long,Boolean>();
//
//		categories = new ArrayList<EjbWithPosition>();
	}
	
	protected abstract void test();

	protected void postConstructOptionTable(JeeslLocaleProvider<LOC> lp,
											JeeslIoGraphicFacade<?,G,GT,GC,GS> fGraphic,
											JeeslFacesMessageBean bMessage)
	{
		super.initJeeslAdmin(lp, bMessage);
		this.fGraphic=fGraphic;
		dbuGraphic.setFacade(fGraphic);

		graphicTypes = fGraphic.allOrderedPositionVisible(fbSvg.getClassGraphicType());
		graphicStyles = fGraphic.allOrderedPositionVisible(fbSvg.getClassFigureStyle());
	}

//	private void reset(boolean rEntity)
//	{
//		if(rEntity) {entity=null;}
//	}

	protected void updateSecurity(JeeslJsfSecurityHandler jsfSecurityHandler, String viewCode)
	{
		super.updateSecurity2(jsfSecurityHandler, viewCode);
	}

	@Override
	protected void updateUiForCategory()
	{
		super.updateUiForCategory();

		supportsUpload = JeeslOptionUploadable.class.isAssignableFrom(optionClass);
		
		supportsImage = JeeslStatusWithImage.class.isAssignableFrom(optionClass);
		supportsGraphic = EjbWithGraphic.class.isAssignableFrom(optionClass);
		supportsComponents = EjbWithGraphicFigure.class.isAssignableFrom(optionClass);
	}

	@Override
	protected void debugUi(boolean debug)
	{
		super.debugUi(debug);
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("Option Tables Settings");
			logger.info("\tImage? "+supportsImage);
			logger.info("\tGraphic? "+supportsGraphic);
			logger.info("\tSymbol? "+supportsSymbol);
			logger.info("\tComponents? "+supportsComponents);
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

		uiAllowAdd = allowAdditionalElements.get(((EjbWithId)category).getId()) || hasDeveloperAction;

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
		if(reset){reset(true,true);}
		debugUi(true);
	}

	@SuppressWarnings("unchecked")
	protected void reloadStatusEntries()
	{
		items = fGraphic.allOrderedPosition(optionClass);
	}

	@SuppressWarnings("unchecked")
	public void add() throws JeeslConstraintViolationException, InstantiationException, IllegalAccessException, JeeslNotFoundException
	{
		logger.debug("add");
		uiAllowCode=true;

		status = optionClass.newInstance();
		((EjbWithId)status).setId(0);
		((EjbWithCode)status).setCode("enter code");
		((EjbWithLang<L>)status).setName(efLang.createEmpty(localeCodes));
		((EjbWithDescription<D>)status).setDescription(efDescription.createEmpty(localeCodes));

		if(supportsGraphic)
		{
			GT type = fGraphic.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol.toString());
			GS style = fGraphic.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicShape.Code.shapeCircle.toString());
			graphic = efGraphic.buildSymbol(type, style);
			((EjbWithGraphic<G>)status).setGraphic(graphic);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void selectStatus() throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		figures = null; figure=null;
		status = fGraphic.find(optionClass,(EjbWithId)status);
		status = fGraphic.loadGraphic(optionClass,(EjbWithId)status);
		logger.debug("selectStatus");
		status = efLang.persistMissingLangs(fGraphic,localeCodes,(EjbWithLang)status);
		status = efDescription.persistMissingLangs(fGraphic,localeCodes,(EjbWithDescription)status);

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

			if(supportsComponents){reloadFigures();}
		}

		uiAllowCode = hasDeveloperAction || hasAdministratorAction;
		if(hasDeveloperAction){uiAllowCode=true;}
		else if(status instanceof JeeslStatusFixedCode)
		{
			for(String fixed : ((JeeslStatusFixedCode)status).getFixedCodes())
			{
				if(fixed.equals(((JeeslStatus)status).getCode()))
				{
					uiAllowCode=false;
				}
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
			if(supportsGraphic)
			{
				graphic = ((EjbWithGraphic<G>)status).getGraphic();
				if(debugSave){logger.info("Saved "+graphic.getClass().getSimpleName()+" "+graphic.toString());}
			}
			if(supportsComponents){reloadFigures();}
			if(debugSave){logger.info("Saved "+status.getClass().getSimpleName()+" "+status.toString());}

			updateAppScopeBean2(status);
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

//	public void cancelStatus() {reset(true,true);}
	public void cancelFigure() {reset(false,true);reloadFigures();}
//	private void reset(boolean rStatus, boolean rFigure)
//	{
//		if(rStatus){status=null;}
//		if(rFigure){figure=null;}
//	}

	protected void updateAppScopeBean2(Object o){}

	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic, items);}
	public void reorderFigures() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic,figures);}

	@SuppressWarnings("unchecked")
	public void handleFileUpload(FileUploadEvent event)
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		((EjbWithGraphic<G>)status).getGraphic().setData(file.getContent());
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
		figure = efFigure.build(graphic);
	}

	public void selectFigure()
	{
		logger.info("Select "+figure.toString());
	}

	public void saveFigure() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Select "+figure.toString());
		figure.setStyle(fGraphic.find(fbSvg.getClassFigureStyle(),figure.getStyle()));
		figure = fGraphic.save(figure);
		reloadFigures();
	}

	public void deleteFigure() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fGraphic.rm(figure);
		reset(false,true);
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
//      JaxbUtil.info(dataUpdate);

        dbuGraphic.update(cW,xml.getStatus());

        this.selectCategory();
	}
	
	protected abstract JeeslSystemRestInterface rest(String iFqcn);
	
	protected Container downloadData(String iFqcn) throws UtilsConfigurationException
	{
		return this.rest(iFqcn).exportStatus(iFqcn);
	}
	
	@SuppressWarnings({"rawtypes" })
	public <S extends JeeslStatus, W extends EjbWithCodeGraphic<G>> void uploadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Uploading REST");

		Class<?> c = Class.forName(entity.getCode());
		Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslData.class,c);

		Container xml = this.rest(i.getName()).exportStatus(i.getName());
	}
}