package org.jeesl.web.mbean.io;

import java.io.Serializable;
import java.util.List;

import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIoGraphicBean<L extends JeeslLang, D extends JeeslDescription,
											G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
											GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIoGraphicBean.class);
	
	private JeeslFacade fUtils;
	
	private final SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg;

	public AbstractIoGraphicBean(SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg)
	{
		this.fbSvg=fbSvg;
	}
	
	protected void postConstruct(JeeslFacade fUtils)
	{
		this.fUtils=fUtils;
		
		reloadGraphicTypes();
		reloadShapes(fUtils);
	}
	
	private List<GT> graphicTypes;
	public List<GT> getGraphicTypes() {return graphicTypes;}
	public void reloadGraphicTypes(){graphicTypes = fUtils.allOrderedPositionVisible(fbSvg.getClassGraphicType());}
	
	private List<GS> shapes;
	public List<GS> getShapes(){return shapes;}
	public void reloadShapes(JeeslFacade facade){shapes = facade.allOrderedPositionVisible(fbSvg.getClassFigureStyle());}	
}