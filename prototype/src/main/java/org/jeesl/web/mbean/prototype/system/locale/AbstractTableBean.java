package org.jeesl.web.mbean.prototype.system.locale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFigureFactory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithColour;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithLocked;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractTableBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>,
								RE extends JeeslRevisionEntity<L,D,?,?,?,?>
>
			extends AbstractAdminBean<L,D,LOC>
			implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractTableBean.class);
	private static final long serialVersionUID = 1L;
	
	protected JeeslGraphicFacade<L,D,?,G,GT,F,FS> fGraphic;
	
	protected final LocaleFactoryBuilder<L,D,LOC> fbStatus;
	protected final SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg;
	protected final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision;

	protected final Map<EjbWithPosition,RE> mapEntity; public Map<EjbWithPosition, RE> getMapEntity() {return mapEntity;}
	
	protected final List<EjbWithPosition> categories; public List<EjbWithPosition> getCategories(){return categories;}
	protected List<EjbWithPosition> parents; public List<EjbWithPosition> getParents(){return parents;}
	protected List<EjbWithPosition> items; public List<EjbWithPosition> getItems() {return items;}
	protected List<GT> graphicTypes; public List<GT> getGraphicTypes() {return graphicTypes;}
	protected List<FS> graphicStyles; public List<FS> getGraphicStyles() {return graphicStyles;}
	protected List<F> figures; public List<F> getFigures() {return figures;}
	
	protected boolean supportsSymbol; public boolean getSupportsSymbol(){return supportsSymbol;}
	protected boolean supportsLocked; public boolean isSupportsLocked() {return supportsLocked;}
	protected boolean supportsDownload; public boolean getSupportsDownload(){return supportsDownload;}
	protected boolean supportsColour; public boolean getSupportsColour(){return supportsColour;}
	protected boolean logOnInfo; public boolean isLogOnInfo() {return logOnInfo;} public void setLogOnInfo(boolean logOnInfo) {this.logOnInfo = logOnInfo;}
	
	protected final EjbGraphicFactory<L,D,G,GT,F,FS> efGraphic;
	protected final EjbGraphicFigureFactory<L,D,G,GT,F,FS> efFigure;

	@SuppressWarnings("rawtypes") protected Class clParent;
	protected long parentId; public long getParentId(){return parentId;} public void setParentId(long parentId) {this.parentId = parentId;}
	
	protected Object category; public Object getCategory() {return category;} public void setCategory(Object category) {this.category = category;}
	protected Object status; public Object getStatus() {return status;} public void setStatus(Object status) {this.status = status;}
	protected G graphic; public G getGraphic() {return graphic;} public void setGraphic(G graphic) {this.graphic = graphic;}
	protected RE entity; public RE getEntity() {return entity;}
	protected F figure; public F getFigure() {return figure;} public void setFigure(F figure) {this.figure = figure;}

	@SuppressWarnings("rawtypes")
	protected Class optionClass; public Class<?> getOptionClass() {return optionClass;}
	
	protected long index;
	protected Map<Long,Boolean> allowAdditionalElements; public Map<Long, Boolean> getAllowAdditionalElements(){return allowAdditionalElements;}

	public AbstractTableBean(LocaleFactoryBuilder<L,D,LOC> fbStatus,
									SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg,
									IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
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
		
		logOnInfo = false;
	}
	
	protected void reset(boolean rEntity)
	{
		if(rEntity) {entity=null;}
	}
	
	protected void loadEntities()
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
	
	public void cancelStatus() {reset(true,true);}
//	public void cancelFigure() {reset(false,true);reloadFigures();}
	protected void reset(boolean rStatus, boolean rFigure)
	{
		if(rStatus){status=null;}
		if(rFigure){figure=null;}
	}
	
	protected void updateUiForCategory()
	{
		supportsSymbol = JeeslStatusWithSymbol.class.isAssignableFrom(optionClass);
		supportsLocked = EjbWithLocked.class.isAssignableFrom(optionClass);
		supportsDownload = JeeslRestDownloadOption.class.isAssignableFrom(optionClass);
		supportsColour = JeeslStatusWithColour.class.isAssignableFrom(optionClass);
	}	
}