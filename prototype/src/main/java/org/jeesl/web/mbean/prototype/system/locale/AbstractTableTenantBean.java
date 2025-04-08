package org.jeesl.web.mbean.prototype.system.locale;

import java.io.Serializable;

import org.exlp.util.io.StringUtil;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.controller.provider.ListLocaleProvider;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.tenant.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithStyle;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.db.updater.JeeslDbTenantStatusUpdater;
import org.jeesl.util.query.ejb.JeeslInterfaceAnnotationQuery;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTableTenantBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,G>,
										R extends JeeslTenantRealm<L,D,R,G>, RREF extends EjbWithId,
										G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
										GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>>
					extends AbstractTableBean<L,D,LOC,G,GT,GC,GS,RE> implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractTableTenantBean.class);
	private static final long serialVersionUID = 1L;

	private JeeslLocaleManager<LOC> lm;

	protected boolean supportsParent; public boolean isSupportsParent() {return supportsParent;}

	private R realm; public R getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}
	
	public AbstractTableTenantBean(IoLocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
	{
		super(fbStatus,fbSvg,fbRevision);

	}

	protected void postConstructOptionTable(JeeslLocaleProvider<LOC> lp,
											JeeslIoGraphicFacade<?,G,GT,GC,GS> fGraphic,
											JeeslFacesMessageBean bMessage,
											R realm)
	{
		super.initJeeslAdmin(lp,bMessage);
		this.fGraphic=fGraphic;
		this.realm=realm;

		this.lm = new ListLocaleProvider<>(lp.getLocales());
		graphicTypes = fGraphic.allOrderedPositionVisible(fbSvg.getClassGraphicType());
		graphicStyles = fGraphic.allOrderedPositionVisible(fbSvg.getClassFigureStyle());
	}

	protected void updateRref(RREF rref)
	{
		this.rref=rref;
	}

//	protected void reset(boolean rEntity)
//	{
//		if(rEntity) {entity=null;}
//	}

	@Override
	protected void updateUiForCategory()
	{
		super.updateUiForCategory();
	}

	@Override
	protected void debugUi(boolean debug)
	{
		super.debugUi(debug);
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("Option Tables Settings");
			logger.info("\tSymbol? "+supportsSymbol);
			logger.info("\tDownload "+supportsDownload);
			logger.info("\tLocked? "+supportsLocked);
			logger.info("\tParent? "+supportsParent);
		}
	}

	@Override
	protected void loadEntities()
	{
		for(EjbWithPosition p : categories)
		{
			try
			{
				String fqcn = ((EjbWithSymbol)p).getSymbol();
				RE e = fGraphic.fByCode(fbRevision.getClassEntity(),fqcn);
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

		optionClass = Class.forName(fqcn);
		updateUiForCategory();

		try {entity = fGraphic.fByCode(fbRevision.getClassEntity(), optionClass.getName());}
		catch (JeeslNotFoundException e) {}
		
		supportsParent = ((EjbWithStyle)category).getStyle()!=null;
		if(supportsParent)
		{
            clParent = Class.forName(((EjbWithStyle)category).getStyle())
    					.asSubclass(fbStatus.getClassStatusTenant())
    					.asSubclass(EjbWithPosition.class);
            
            parents = fGraphic.all(clParent,realm,rref);
            logger.info(optionClass.getSimpleName()+" "+parents.size());
		}
		else
		{
			clParent=null;
			parents=null;
		}

		uiAllowAdd = allowAdditionalElements.get(((EjbWithId)category).getId()) || hasDeveloperAction;

		reloadStatusEntries();
		if(reset){reset(true,true);}
		debugUi(true);
	}

	@SuppressWarnings("unchecked")
	protected void reloadStatusEntries()
	{
		items = fGraphic.all(optionClass,realm,rref);
	}

	@SuppressWarnings("unchecked")
	public void add() throws JeeslConstraintViolationException, InstantiationException, IllegalAccessException
	{
		logger.debug("add");
		uiAllowCode=true;

		status = optionClass.newInstance();
		((EjbWithId)status).setId(0);
		((EjbWithCode)status).setCode("enter code");
		((EjbWithLang<L>)status).setName(efLang.createEmpty(localeCodes));
		((EjbWithDescription<D>)status).setDescription(efDescription.createEmpty(localeCodes));
		((JeeslWithTenantSupport<R>)status).setRealm(realm);
		((JeeslWithTenantSupport<R>)status).setRref(rref.getId());

		GT type = fGraphic.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol);
		GS style = fGraphic.fByEnum(fbSvg.getClassFigureStyle(), JeeslGraphicShape.Code.shapeCircle);
		graphic = efGraphic.buildSymbol(type, style);
		((EjbWithGraphic<G>)status).setGraphic(graphic);
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

		if(((EjbWithGraphic<G>)status).getGraphic()==null)
		{
			logger.info("Need to create a graphic entity for this status");
			GT type = fGraphic.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.symbol);
			GS style = fGraphic.fByEnum(fbSvg.getClassFigureStyle(), JeeslGraphicShape.Code.shapeCircle);
			graphic = fGraphic.persist(efGraphic.buildSymbol(type, style));
			((EjbWithGraphic<G>)status).setGraphic(graphic);
			status = fGraphic.update(status);
		}
		if(((EjbWithParent)status).getParent()!=null)
		{
			parentId=((EjbWithParent)status).getParent().getId();
		}
		else {parentId=0;}
		
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
		try
		{
			 if(clParent!=null && parentId>0)
			 {
				 if(debugOnInfo)
				 {
					 logger.debug(clParent.getName());
				 }
				 ((EjbWithParent)status).setParent((EjbWithCode)fGraphic.find(clParent, parentId));
			 }
			
			graphic.setType(fGraphic.find(fbSvg.getClassGraphicType(), ((EjbWithGraphic<G>)status).getGraphic().getType()));
        	if(graphic.getStyle()!=null){graphic.setStyle(fGraphic.find(fbSvg.getClassFigureStyle(), ((EjbWithGraphic<G>)status).getGraphic().getStyle()));}

        	((EjbWithGraphic<G>)status).setGraphic(graphic);

        	if(logOnInfo){logger.info("Saving "+status.getClass().getSimpleName()+" "+status.toString()+" rref:"+rref+" realm:"+realm.toString());}
			status = fGraphic.save((EjbSaveable)status);
			status = fGraphic.loadGraphic(optionClass,(EjbWithId)status);

			graphic = ((EjbWithGraphic<G>)status).getGraphic();
			if(logOnInfo){logger.info("Saved "+graphic.getClass().getSimpleName()+" "+graphic.toString());}

			reloadFigures();
			if(logOnInfo){logger.info("Saved "+status.getClass().getSimpleName()+" "+status.toString());}

			updateAppScopeBean(fGraphic,status);
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
			updateAppScopeBean(fGraphic,status);
			bMessage.growlDeleted((EjbRemoveable)status);
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

	protected void updateAppScopeBean(JeeslFacade facade, Object o){}

	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic, items);}
	public void reorderFigures() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fGraphic,figures);}

	@SuppressWarnings("unchecked")
	public void handleFileUpload(FileUploadEvent event)
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		((EjbWithGraphic<G>)status).getGraphic().setData(file.getContent());
	}

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
	
	protected abstract JeeslSystemRestInterface rest(String iFqcn);
	

	//JEESL REST DATA
	@SuppressWarnings("unchecked")
	public <Y extends JeeslMcsStatus<L,D,R,Y,G>, X extends JeeslStatus<L,D,X>> void downloadData() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException
	{
		logger.info("Downloading REST");

		String fqcn = ((EjbWithSymbol)category).getSymbol();
		Class<?> c = Class.forName(fqcn);
		Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslData.class,c);
		
		Container xml = this.rest(i.getName()).exportStatus(i.getName());;
		
		JaxbUtil.trace(xml);

		JeeslDbTenantStatusUpdater<L,D,LOC,R,RREF,G,GT> updater = new JeeslDbTenantStatusUpdater<L,D,LOC,R,RREF,G,GT>(fbStatus,fbSvg,fGraphic,lm);
		updater.initTenant(realm,rref);
		updater.iStatus(optionClass,xml);

        selectCategory();
	}
}