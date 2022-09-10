package org.jeesl.web.mbean.prototype.system;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppUtilsBean<L extends JeeslLang, D extends JeeslDescription,
											G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
											GC extends JeeslGraphicComponent<G,GT,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppUtilsBean.class);
	
	protected JeeslFacade fUtils;
	
	protected Class<GT> cGraphicType;
	protected Class<GS> cGraphicStyle;

	public AbstractAppUtilsBean()
	{
		
	}
	
	protected void initSuper(Class<GT> cGraphicType,Class<GS> cGraphicStyle)
	{
		this.cGraphicType=cGraphicType;
		this.cGraphicStyle=cGraphicStyle;
		
		reloadGraphicTypes();
		reloadGraphicStyles();
	}
	
	private List<GT> graphicTypes;
	public List<GT> getGraphicTypes() {return graphicTypes;}
	public void reloadGraphicTypes(){graphicTypes = fUtils.allOrderedPositionVisible(cGraphicType);}
	
	private List<GS> graphicStyles;
	public List<GS> getGraphicStyles(){return graphicStyles;}
	public void reloadGraphicStyles(){graphicStyles = fUtils.allOrderedPositionVisible(cGraphicStyle);}	
}