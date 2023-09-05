package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFigureFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.svg.SvgFigureFactory;
import org.jeesl.factory.svg.SvgSymbolFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvgFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
								GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SvgFactoryBuilder.class);
	
	private final Class<G> cG; public Class<G> getClassGraphic(){return cG;}
	private final Class<GT> cType; public Class<GT> getClassGraphicType(){return cType;}
	private final Class<GC> cF; public Class<GC> getClassFigure(){return cF;}
	private final Class<GS> cFs; public Class<GS> getClassFigureStyle(){return cFs;}
	
	public SvgFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<G> cG, final Class<GT> cType, final Class<GC> cF, final Class<GS> cFs)
	{       
		super(cL,cD);
		this.cG = cG;
		this.cType = cType;
		this.cF = cF;
		this.cFs = cFs;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
					GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
		SvgFactoryBuilder<L,D,G,GT,GC,GS> factory(final Class<L> cL, final Class<D> cD, final Class<G> cG, final Class<GT> cGT, final Class<GC> cF, final Class<GS> cFs)
	{
		return new SvgFactoryBuilder<L,D,G,GT,GC,GS>(cL,cD,cG,cGT,cF,cFs);
	}
	
    public EjbGraphicFactory<L,D,G,GT,GS> efGraphic() {return new EjbGraphicFactory<>(cG,cType,cFs);}
    public EjbGraphicFigureFactory<L,D,G,GC,GS> efFigure() {return new EjbGraphicFigureFactory<>(cF);}
	public EjbStatusFactory<L,D,GS> style() {return EjbStatusFactory.instance(cFs,cL,cD);}
	
	public SvgSymbolFactory<G,GT,GC,GS> symbol() {return SvgSymbolFactory.factory();}
	public SvgFigureFactory<G,GT,GC,GS> figure() {return SvgFigureFactory.factory();}
}