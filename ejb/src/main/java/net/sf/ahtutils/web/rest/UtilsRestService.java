package net.sf.ahtutils.web.rest;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.rest.util.status.UtilsStatusRestImport;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class UtilsRestService <L extends JeeslLang,
							D extends JeeslDescription,
							G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
							F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
	extends AbstractUtilsRest<L,D>
	implements UtilsStatusRestImport
{
	final static Logger logger = LoggerFactory.getLogger(UtilsRestService.class);
    
    @SuppressWarnings("unused")
	private final Class<G> cGraphic;
    private final Class<GT> cGraphicType;
    private final Class<FS> cGraphicStyle;
    
    public UtilsRestService(JeeslFacade fUtils, String[] localeCodes, final Class<L> cLang, final Class<D> cDescription, final Class<G> cGraphic,final Class<GT> cGraphicType, final Class<FS> cGraphicStyle)
	{   
    	super(fUtils,localeCodes,cLang,cDescription);
        
        this.cGraphic=cGraphic;
        this.cGraphicType=cGraphicType;
        this.cGraphicStyle=cGraphicStyle;
        
        this.fUtils=fUtils;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
					F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>> 
		UtilsRestService<L,D,G,GT,F,FS>
		factory(JeeslFacade fUtils,String[] localeCodes,final Class<L> cL, final Class<D> cD, final Class<G> cGraphic,final Class<GT> cGraphicType, final Class<FS> cGraphicStyle)
	{
		return new UtilsRestService<L,D,G,GT,F,FS>(fUtils,localeCodes,cL,cD,cGraphic,cGraphicType,cGraphicStyle);
	}

	@Override public DataUpdate importUtilsSymbolGraphicTypes(Aht types) {return super.importStatus(cGraphicType, null, types);}
	@Override public DataUpdate importUtilsSymbolGraphicStyle(Aht styles) {return super.importStatus(cGraphicStyle, null, styles);}
}