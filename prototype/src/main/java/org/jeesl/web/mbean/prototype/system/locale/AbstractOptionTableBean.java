package org.jeesl.web.mbean.prototype.system.locale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslExportRestFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.api.rest.JeeslExportRest;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFigureFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicStyle;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithImage;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.system.option.JeeslOptionRest;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDescription;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;
import org.jeesl.interfaces.model.system.option.JeeslOptionUploadable;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithLocked;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphicFigure;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImageAlt;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Entity;
import org.jeesl.util.db.updater.JeeslDbGraphicUpdater;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.exlp.util.io.StringUtil;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractOptionTableBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
										F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>
>
			extends AbstractTableBean<L,D,LOC,G,GT,F,FS,RE>
			implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractOptionTableBean.class);
	private static final long serialVersionUID = 1L;

	private final JeeslDbGraphicUpdater<G,GT> dbuGraphic;

	protected boolean allowSvg; public boolean isAllowSvg() {return allowSvg;}
	private boolean showDescription; public boolean isShowDescription() {return showDescription;}

	private boolean supportsUpload; public boolean getSupportsUpload(){return supportsUpload;}
//	private boolean supportsDownload; public boolean getSupportsDownload(){return supportsDownload;}
	private boolean supportsDescription; public boolean getSupportsDescription(){return supportsDescription;}
//	protected boolean supportsSymbol; public boolean getSupportsSymbol(){return supportsSymbol;}
	protected boolean supportsImage; public boolean getSupportsImage() {return supportsImage;}
	protected boolean supportsGraphic; public boolean getSupportsGraphic() {return supportsGraphic;}
	protected boolean supportsFigure; public boolean isSupportsFigure() {return supportsFigure;}
//	protected boolean supportsLocked; public boolean isSupportsLocked() {return supportsLocked;}


	@SuppressWarnings("rawtypes")
	protected Class clParent;

//	private final Map<EjbWithPosition,RE> mapEntity; public Map<EjbWithPosition, RE> getMapEntity() {return mapEntity;}
//	protected final List<EjbWithPosition> categories; public List<EjbWithPosition> getCategories(){return categories;}
	protected List<EjbWithPosition> parents; public List<EjbWithPosition> getParents(){return parents;}
//	protected List<EjbWithPosition> items; public List<EjbWithPosition> getItems() {return items;}
//	private List<GT> graphicTypes; public List<GT> getGraphicTypes() {return graphicTypes;}
//	private List<FS> graphicStyles; public List<FS> getGraphicStyles() {return graphicStyles;}
//	private List<F> figures; public List<F> getFigures() {return figures;}

//	private F figure; public F getFigure() {return figure;} public void setFigure(F figure) {this.figure = figure;}

	protected long parentId; public long getParentId(){return parentId;}public void setParentId(long parentId){this.parentId = parentId;}

//	protected final EjbGraphicFactory<L,D,G,GT,F,FS> efGraphic;
//	private final EjbGraphicFigureFactory<L,D,G,GT,F,FS> efFigure;

	public AbstractOptionTableBean(LocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
	{
		super(fbStatus,fbSvg,fbRevision);
//		this.fbStatus=fbStatus;
//		this.fbSvg=fbSvg;
//		this.fbRevision=fbRevision;
		dbuGraphic = new JeeslDbGraphicUpdater<>(fbSvg);

//		efGraphic = fbSvg.efGraphic();
//
//		efFigure = fbSvg.efFigure();

//		index=1;

		showDescription = false;
//		hasDeveloperAction = false;
//		hasAdministratorAction = true;
//		hasTranslatorAction = true;

//		status = null;
//		allowAdditionalElements = new Hashtable<Long,Boolean>();
//
//		mapEntity = new HashMap<>();
//		categories = new ArrayList<EjbWithPosition>();
	}

	protected void postConstructOptionTable(JeeslTranslationBean<L,D,LOC> bTranslation,
											JeeslGraphicFacade<L,D,?,G,GT,F,FS> fGraphic,
											JeeslFacesMessageBean bMessage)
	{
		super.initJeeslAdmin(bTranslation, bMessage);
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

	protected void updateUiForCategory()
	{
		supportsUpload = JeeslOptionUploadable.class.isAssignableFrom(cStatus);
		supportsDownload = JeeslOptionRestDownload.class.isAssignableFrom(cStatus);
		supportsDescription = JeeslOptionRestDescription.class.isAssignableFrom(cStatus);
		supportsImage = JeeslStatusWithImage.class.isAssignableFrom(cStatus);
		supportsGraphic = EjbWithGraphic.class.isAssignableFrom(cStatus);
		supportsSymbol = JeeslStatusWithSymbol.class.isAssignableFrom(cStatus);
		supportsFigure = EjbWithGraphicFigure.class.isAssignableFrom(cStatus);
		supportsLocked = EjbWithLocked.class.isAssignableFrom(cStatus);
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
			logger.info("\tFigure? "+supportsFigure);
			logger.info("\tLocked? "+supportsLocked);
			logger.info("\t"+JeeslOptionRestDownload.class.getSimpleName()+"? "+supportsDownload);
			logger.info("\t"+JeeslOptionRestDescription.class.getSimpleName()+"? "+supportsDescription);
			logger.info("\t"+JeeslOptionUploadable.class.getSimpleName()+"? "+supportsUpload);
		}
	}

//	protected void loadEntities()
//	{
//		for(EjbWithPosition p : categories)
//		{
//			try
//			{
//				String fqcn = ((EjbWithImage)p).getImage();
//				RE e = fGraphic.fByCode(fbRevision.getClassEntity(),fqcn);
//				mapEntity.put(p,e);
//			}
//			catch (JeeslNotFoundException e) {}
//		}
//	}

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

		cStatus = Class.forName(((EjbWithImage)category).getImage());
		updateUiForCategory();

		try {entity = fGraphic.fByCode(fbRevision.getClassEntity(), cStatus.getName());}
		catch (JeeslNotFoundException e) {}

		uiAllowAdd = allowAdditionalElements.get(((EjbWithId)category).getId()) || hasDeveloperAction;

		if(((EjbWithImageAlt)category).getImageAlt()!=null)
		{
            clParent = Class.forName(((EjbWithImageAlt)category).getImageAlt()).asSubclass(fbStatus.getClassStatus());
            parents = fGraphic.all(clParent);
            logger.info(cStatus.getSimpleName()+" "+parents.size());
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
		items = fGraphic.allOrderedPosition(cStatus);
	}

	@SuppressWarnings("unchecked")
	public void add() throws JeeslConstraintViolationException, InstantiationException, IllegalAccessException, JeeslNotFoundException
	{
		logger.debug("add");
		uiAllowCode=true;

		status = cStatus.newInstance();
		((EjbWithId)status).setId(0);
		((EjbWithCode)status).setCode("enter code");
		((EjbWithLang<L>)status).setName(efLang.createEmpty(localeCodes));
		((EjbWithDescription<D>)status).setDescription(efDescription.createEmpty(localeCodes));

		if(supportsGraphic)
		{
			GT type = fGraphic.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol.toString());
			FS style = fGraphic.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicStyle.Code.circle.toString());
			graphic = efGraphic.buildSymbol(type, style);
			((EjbWithGraphic<G>)status).setGraphic(graphic);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void selectStatus() throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		figures = null; figure=null;
		status = fGraphic.find(cStatus,(EjbWithId)status);
		status = fGraphic.loadGraphic(cStatus,(EjbWithId)status);
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
				FS style = fGraphic.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicStyle.Code.circle);
				graphic = fGraphic.persist(efGraphic.buildSymbol(type, style));
				((EjbWithGraphic<G>)status).setGraphic(graphic);
				status = fGraphic.update(status);
			}
			graphic = ((EjbWithGraphic<G>)status).getGraphic();

			if(supportsFigure){reloadFigures();}
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
			status = fGraphic.loadGraphic(cStatus,(EjbWithId)status);
			if(supportsGraphic)
			{
				graphic = ((EjbWithGraphic<G>)status).getGraphic();
				if(debugSave){logger.info("Saved "+graphic.getClass().getSimpleName()+" "+graphic.toString());}
			}
			if(supportsFigure){reloadFigures();}
			if(debugSave){logger.info("Saved "+status.getClass().getSimpleName()+" "+status.toString());}



			updateAppScopeBean2(status);
			selectCategory(false);
			bMessage.growlSuccessSaved();
		}
		catch (JeeslConstraintViolationException e)
		{
			logger.error(JeeslConstraintViolationException.class.getSimpleName()+" "+e.getMessage());
			bMessage.errorConstraintViolationInUse();
		}
		catch (JeeslLockingException e)
		{
			logger.error(JeeslLockingException.class.getSimpleName()+" "+e.getMessage());
			bMessage.errorConstraintViolationInUse();
		}
	}

	public void rm() throws ClassNotFoundException
	{
		try
		{
			fGraphic.rm((EjbRemoveable)status);
			updateAppScopeBean2(status);
			status=null;
			selectCategory();
			bMessage.growlSuccessRemoved();
		}
		catch (JeeslConstraintViolationException e)
		{
			bMessage.errorConstraintViolationInUse();
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
		((EjbWithGraphic<G>)status).getGraphic().setData(file.getContents());
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
	public <X extends JeeslOptionRest, S extends JeeslStatus, W extends EjbWithCodeGraphic<G>> void downloadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Downloading REST");

		Class<X> cX = (Class<X>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(JeeslOptionRest.class);
		Class<S> cS = (Class<S>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(JeeslStatus.class);
		Class<W> cW = (Class<W>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(EjbWithCodeGraphic.class);
		X x = cX.newInstance();

		Container xml;
		if(fGraphic instanceof JeeslExportRestFacade)
		{
			logger.info("Using Facade Connection for JBoss EAP6 ("+fGraphic.getClass().getSimpleName()+" implements "+JeeslExportRestFacade.class.getSimpleName()+") "+x.getRestCode());
			xml = ((JeeslExportRestFacade)fGraphic).exportJeeslReferenceRest(x.getRestCode());
		}
		else
		{
			logger.info("Using Direct Connection (JBoss EAP7) "+x.getRestCode());
			xml = this.downloadOptionsFromRest(x.getRestCode());
		}

//		JaxbUtil.info(xml);

		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.createFactory(cS,cL,cD,bTranslation.getLangKeys()));
        asdi.setFacade(fGraphic);
        DataUpdate dataUpdate = asdi.iuStatus(xml.getStatus(),cS,cL,clParent);
        asdi.deleteUnusedStatus(cS, cL, cD);
        JaxbUtil.info(dataUpdate);

        dbuGraphic.update(cW,xml.getStatus());

        selectCategory();
	}
	@SuppressWarnings("unchecked")
	private Container downloadOptionsFromRest(String code) throws UtilsConfigurationException
	{
		StringBuilder url = new StringBuilder();
		if(code.startsWith(JeeslExportRestFacade.packageJeesl)) {url.append(JeeslExportRestFacade.urlJeesl);}
		else if(code.startsWith(JeeslExportRestFacade.packageGeojsf)) {url.append(JeeslExportRestFacade.urlGeojsf);}

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target(url.toString());
		JeeslExportRest<L,D,?,G> rest = restTarget.proxy(JeeslExportRest.class);
		return rest.exportStatus(code);
	}

	//JEESL REST Description
	@SuppressWarnings({ "unchecked"})
	public <X extends JeeslOptionRest> void downloadDescription() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Downloading REST");

		Class<X> cX = (Class<X>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(JeeslOptionRest.class);

		X x = cX.newInstance();

		Entity xml = null;
		if(fGraphic instanceof JeeslExportRestFacade)
		{
			logger.info("Using Facade Connection for JBoss EAP6 ("+fGraphic.getClass().getSimpleName()+" implements "+JeeslExportRestFacade.class.getSimpleName()+"): "+x.getRestCode());
			xml = ((JeeslExportRestFacade)fGraphic).exportJeeslReferenceRevisionEntity(x.getRestCode());
		}
		else
		{
			logger.info("Using Direct Connection (JBoss EAP7) "+x.getRestCode());
			xml = downloadRevisionFromRest(x.getRestCode());
			JaxbUtil.info(xml);
		}

		if(xml!=null && xml.isSetDescriptions())
		{
			try
			{
				for(Description d : xml.getDescriptions().getDescription())
				{
					if(entity.getDescription().containsKey(d.getKey()))
					{
						entity.getDescription().get(d.getKey()).setLang(d.getValue());
					}
				}
				entity = fGraphic.save(entity);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	@SuppressWarnings("unchecked")
	private Entity downloadRevisionFromRest(String code) throws UtilsConfigurationException
	{
		StringBuilder url = new StringBuilder();
		if(code.startsWith(JeeslExportRestFacade.packageJeesl)) {url.append(JeeslExportRestFacade.urlJeesl);}
		else if(code.startsWith(JeeslExportRestFacade.packageGeojsf)) {url.append(JeeslExportRestFacade.urlGeojsf);}

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target(url.toString());
		JeeslExportRest<L,D,?,G> rest = restTarget.proxy(JeeslExportRest.class);
		return rest.exportRevisionEntity(code);
	}
}