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
import org.jeesl.controller.provider.GenericLocaleProvider;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.system.symbol.EjbGraphicFactory;
import org.jeesl.factory.ejb.system.symbol.EjbGraphicFigureFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicStyle;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.status.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.mcs.JeeslWithMultiClientSupport;
import org.jeesl.interfaces.model.system.option.JeeslOptionRest;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.util.db.updater.JeeslDbMcsStatusUpdater;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.jsf.util.PositionListReorderer;
import net.sf.exlp.util.io.StringUtil;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractMcsTableBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,G>,
										R extends JeeslMcsRealm<L,D,R,G>, RREF extends EjbWithId,
										G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
										F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<FS,L,D>,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>
>
			extends AbstractAdminBean<L,D,LOC>
			implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractMcsTableBean.class);
	private static final long serialVersionUID = 1L;

	protected JeeslFacade fUtils;
	
	private final LocaleFactoryBuilder<L,D,LOC> fbStatus;
	private final SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?> fbRevision;
	
	private JeeslLocaleProvider<LOC> lp;
		
	protected boolean supportsSymbol; public boolean getSupportsSymbol(){return supportsSymbol;}

	protected long index;
	protected Map<Long,Boolean> allowAdditionalElements; public Map<Long, Boolean> getAllowAdditionalElements(){return allowAdditionalElements;}
	
	protected Object category; public Object getCategory() {return category;} public void setCategory(Object category) {this.category = category;}
	protected Object status; public Object getStatus() {return status;} public void setStatus(Object status) {this.status = status;}
	private G graphic; public G getGraphic() {return graphic;} public void setGraphic(G graphic) {this.graphic = graphic;}
	private RE entity; public RE getEntity() {return entity;}
	
	protected final EjbGraphicFactory<L,D,G,GT,F,FS> efGraphic;
	private final EjbGraphicFigureFactory<L,D,G,GT,F,FS> efFigure;
	
	private final Map<EjbWithPosition,RE> mapEntity; public Map<EjbWithPosition, RE> getMapEntity() {return mapEntity;}
	
	protected final List<EjbWithPosition> categories; public List<EjbWithPosition> getCategories(){return categories;}
	protected List<EjbWithPosition> items; public List<EjbWithPosition> getItems() {return items;}
	
	private List<GT> graphicTypes; public List<GT> getGraphicTypes() {return graphicTypes;}
	private List<FS> graphicStyles; public List<FS> getGraphicStyles() {return graphicStyles;}
	private List<F> figures; public List<F> getFigures() {return figures;}
	
	private R realm; public R getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}

	private F figure; public F getFigure() {return figure;} public void setFigure(F figure) {this.figure = figure;}

	private boolean supportsDownload; public boolean getSupportsDownload(){return supportsDownload;}
	
	@SuppressWarnings("rawtypes")
	protected Class cl;

	public AbstractMcsTableBean(LocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?> fbRevision)
	{
		super(fbStatus.getClassL(),fbStatus.getClassD());
		this.fbStatus=fbStatus;
		this.fbSvg=fbSvg;
		this.fbRevision=fbRevision;

		efGraphic = fbSvg.efGraphic();	
		efFigure = fbSvg.efFigure();
		
		index=1;
		
		hasDeveloperAction = false;
		hasAdministratorAction = true;
		hasTranslatorAction = true;
		
		status = null;
		allowAdditionalElements = new Hashtable<Long,Boolean>();
		
		mapEntity = new HashMap<>();
		categories = new ArrayList<EjbWithPosition>();
	}
	
	protected void postConstructOptionTable(JeeslTranslationBean<L,D,LOC> bTranslation,
											JeeslGraphicFacade<L,D,?,G,GT,F,FS> fGraphic,
											JeeslFacesMessageBean bMessage,
											R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fUtils=fGraphic;
		this.realm=realm;
			
		lp = new GenericLocaleProvider<>(bTranslation.getLocales());
		graphicTypes = fUtils.allOrderedPositionVisible(fbSvg.getClassGraphicType());
		graphicStyles = fUtils.allOrderedPositionVisible(fbSvg.getClassFigureStyle());
	}
	
	protected void updateRref(RREF rref)
	{
		this.rref=rref;
	}
	
	private void reset(boolean rEntity)
	{
		if(rEntity) {entity=null;}
	}
	
	protected void updateUiForCategory()
	{
		supportsSymbol = JeeslStatusWithSymbol.class.isAssignableFrom(cl);
		supportsDownload = JeeslOptionRestDownload.class.isAssignableFrom(cl);
	}
	
	protected void debugUi(boolean debug)
	{
		super.debugUi(debug);
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("Option Tables Settings");
			logger.info("\tSymbol? "+supportsSymbol);
		}
	}
	
	protected void loadEntities()
	{
		for(EjbWithPosition p : categories)
		{
			try
			{
				String fqcn = ((EjbWithSymbol)p).getSymbol();
				RE e = fUtils.fByCode(fbRevision.getClassEntity(),fqcn);
				mapEntity.put(p,e);
			}
			catch (JeeslNotFoundException e) {}
		}
	}

	public void selectCategory() throws ClassNotFoundException{selectCategory(true);}
	public void selectCategory(boolean reset) throws ClassNotFoundException
	{
		reset(true);
		if(category==null) {logger.error("selectCategory, but category is NULL");}
		String fqcn = ((EjbWithSymbol)category).getSymbol();
		
		StringBuilder sb = new StringBuilder();
		sb.append("selectCategory ");
		sb.append(fqcn);
		logger.info(sb.toString());
		
		cl = Class.forName(fqcn);
		updateUiForCategory();
		
		try {entity = fUtils.fByCode(fbRevision.getClassEntity(), cl.getName());}
		catch (JeeslNotFoundException e) {}
		
		uiAllowAdd = allowAdditionalElements.get(((EjbWithId)category).getId()) || hasDeveloperAction;
		
		reloadStatusEntries();
		if(reset){reset(true,true);}
		debugUi(true);
	}
	
	@SuppressWarnings("unchecked")
	protected void reloadStatusEntries()
	{
		items = fUtils.allOrderedPosition(cl);
	}
	
	@SuppressWarnings("unchecked")
	public void add() throws JeeslConstraintViolationException, InstantiationException, IllegalAccessException, JeeslNotFoundException
	{
		logger.debug("add");
		uiAllowCode=true;
		
		status = cl.newInstance();
		((EjbWithId)status).setId(0);
		((EjbWithCode)status).setCode("enter code");
		((EjbWithLang<L>)status).setName(efLang.createEmpty(localeCodes));
		((EjbWithDescription<D>)status).setDescription(efDescription.createEmpty(localeCodes));
		((JeeslWithMultiClientSupport<R>)status).setRealm(realm);
		((JeeslWithMultiClientSupport<R>)status).setRref(rref.getId());
		
		GT type = fUtils.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol.toString());
		FS style = fUtils.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicStyle.Code.circle.toString());
		graphic = efGraphic.buildSymbol(type, style);
		((EjbWithGraphic<G>)status).setGraphic(graphic);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void selectStatus() throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		figures = null; figure=null;
		status = fUtils.find(cl,(EjbWithId)status);
		status = fUtils.loadGraphic(cl,(EjbWithId)status);
		logger.debug("selectStatus");
		status = efLang.persistMissingLangs(fUtils,localeCodes,(EjbWithLang)status);
		status = efDescription.persistMissingLangs(fUtils,localeCodes,(EjbWithDescription)status);
				
		if(((EjbWithGraphic<G>)status).getGraphic()==null)
		{
			logger.info("Need to create a graphic entity for this status");
			GT type = fUtils.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol);
			FS style = fUtils.fByCode(fbSvg.getClassFigureStyle(), JeeslGraphicStyle.Code.circle);
			graphic = fUtils.persist(efGraphic.buildSymbol(type, style));
			((EjbWithGraphic<G>)status).setGraphic(graphic);
			status = fUtils.update(status);
		}
		graphic = ((EjbWithGraphic<G>)status).getGraphic();reloadFigures();
		
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
	}

	@SuppressWarnings("unchecked")
	public void save() throws ClassNotFoundException, JeeslNotFoundException
    {
		boolean debugSave=true;
		try
		{
			graphic.setType(fUtils.find(fbSvg.getClassGraphicType(), ((EjbWithGraphic<G>)status).getGraphic().getType()));
        	if(graphic.getStyle()!=null){graphic.setStyle(fUtils.find(fbSvg.getClassFigureStyle(), ((EjbWithGraphic<G>)status).getGraphic().getStyle()));}
    		
        	((EjbWithGraphic<G>)status).setGraphic(graphic);

        	if(debugSave){logger.info("Saving "+status.getClass().getSimpleName()+" "+status.toString());}
			status = fUtils.save((EjbSaveable)status);
			status = fUtils.loadGraphic(cl,(EjbWithId)status);
			
			graphic = ((EjbWithGraphic<G>)status).getGraphic();
			if(debugSave){logger.info("Saved "+graphic.getClass().getSimpleName()+" "+graphic.toString());}
			
			reloadFigures();
			if(debugSave){logger.info("Saved "+status.getClass().getSimpleName()+" "+status.toString());}
			
			updateAppScopeBean(fUtils,status);
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
			fUtils.rm((EjbRemoveable)status);
			updateAppScopeBean(fUtils,status);
			status=null;
			selectCategory();
			bMessage.growlSuccessRemoved();
		}
		catch (JeeslConstraintViolationException e)
		{
			bMessage.errorConstraintViolationInUse();
		}
	}
	
	public void cancelStatus() {reset(true,true);}
	public void cancelFigure() {reset(false,true);reloadFigures();}
	private void reset(boolean rStatus, boolean rFigure)
	{
		if(rStatus){status=null;}
		if(rFigure){figure=null;}
	}
	
	protected void updateAppScopeBean(JeeslFacade facade, Object o){}
	
	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fUtils, items);}
	public void reorderFigures() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fUtils,figures);}
	
	@SuppressWarnings("unchecked")
	public void handleFileUpload(FileUploadEvent event)
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		((EjbWithGraphic<G>)status).getGraphic().setData(file.getContents());  
	}
	
	@SuppressWarnings("unchecked")
	public void changeGraphicType()
	{
		((EjbWithGraphic<G>)status).getGraphic().setType(fUtils.find(fbSvg.getClassGraphicType(), ((EjbWithGraphic<G>)status).getGraphic().getType()));
		logger.info("changeGraphicType to "+((EjbWithGraphic<G>)status).getGraphic().getType().getCode());
	}
	
	private void reloadFigures()
	{
		figures = fUtils.allForParent(fbSvg.getClassFigure(),graphic);
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
		figure.setStyle(fUtils.find(fbSvg.getClassFigureStyle(),figure.getStyle()));
		figure = fUtils.save(figure);
		reloadFigures();
	}
	
	public void deleteFigure() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fUtils.rm(figure);
		reset(false,true);
		reloadFigures();
	}
	
	//JEESL REST DATA
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <REST extends JeeslOptionRest, Y extends JeeslMcsStatus<L,D,R,Y,G>, X extends JeeslStatus<X,L,D>> void downloadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Downloading REST");
		
		Class<REST> cRest = (Class<REST>)Class.forName(((EjbWithSymbol)category).getSymbol()).asSubclass(JeeslOptionRest.class);
//		Class<S> cS = (Class<S>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(JeeslStatus.class);
//		Class<W> cW = (Class<W>)Class.forName(((EjbWithImage)category).getImage()).asSubclass(EjbWithCodeGraphic.class);
		REST rest = cRest.newInstance();
		
		Container xml;
		if(fUtils instanceof JeeslExportRestFacade)
		{
			logger.info("Using Facade Connection for JBoss EAP6 ("+fUtils.getClass().getSimpleName()+" implements "+JeeslExportRestFacade.class.getSimpleName()+")");
			xml = ((JeeslExportRestFacade)fUtils).exportJeeslReferenceRest(rest.getRestCode());
		}
		else
		{
			logger.info("Using Direct Connection (JBoss EAP7)");
			xml = downloadOptionsFromRest(rest.getRestCode());
		}
		JaxbUtil.info(xml);
		
		JeeslDbMcsStatusUpdater<L,D,LOC,R,RREF,G,GT> updater = new JeeslDbMcsStatusUpdater<>(fbStatus,fUtils,lp);
		updater.initMcs(realm,rref);
//        asdi.setStatusEjbFactory(EjbStatusFactory.createFactory(cS,cL,cD,bTranslation.getLangKeys()));
		
//        DataUpdate dataUpdate = asdi.iuStatus(xml.getStatus(),cS,cL,clParent);
//        asdi.deleteUnusedStatus(cS, cL, cD);
//        JaxbUtil.info(dataUpdate);
//        
//        dbuGraphic.update(cW,xml.getStatus());
        
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
}