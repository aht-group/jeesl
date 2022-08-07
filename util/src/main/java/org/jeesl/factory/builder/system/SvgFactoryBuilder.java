package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFigureFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.svg.SvgFigureFactory;
import org.jeesl.factory.svg.SvgSymbolFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvgFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(SvgFactoryBuilder.class);
	
	private final Class<G> cG; public Class<G> getClassGraphic(){return cG;}
	private final Class<GT> cGT; public Class<GT> getClassGraphicType(){return cGT;}
	private final Class<F> cF; public Class<F> getClassFigure(){return cF;}
	private final Class<FS> cFs; public Class<FS> getClassFigureStyle(){return cFs;}
	
	public SvgFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<G> cG, final Class<GT> cGT, final Class<F> cF, final Class<FS> cFs)
	{       
		super(cL,cD);
		this.cG = cG;
		this.cGT = cGT;
		this.cF = cF;
		this.cFs = cFs;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
					F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
		SvgFactoryBuilder<L,D,G,GT,F,FS> factory(final Class<L> cL, final Class<D> cD, final Class<G> cG, final Class<GT> cGT, final Class<F> cF, final Class<FS> cFs)
	{
		return new SvgFactoryBuilder<L,D,G,GT,F,FS>(cL,cD,cG,cGT,cF,cFs);
	}
	
    public EjbGraphicFactory<L,D,G,GT,F,FS> efGraphic() {return new EjbGraphicFactory<L,D,G,GT,F,FS>(cG);}
    
    public EjbGraphicFigureFactory<L,D,G,GT,F,FS> efFigure()
    {
    		return new EjbGraphicFigureFactory<L,D,G,GT,F,FS>(cF);
    }
	
	public EjbStatusFactory<FS,L,D> style()
	{
		return EjbStatusFactory.createFactory(cFs,cL,cD);
	}
	
	public SvgSymbolFactory<L,D,G,GT,F,FS> symbol()
	{
		return SvgSymbolFactory.factory();
	}
	
	public SvgFigureFactory<L,D,G,GT,F,FS> figure()
	{
		return SvgFigureFactory.factory();
	}
}