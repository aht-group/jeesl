package org.jeesl.util.db.updater;

import java.util.List;

import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.system.symbol.EjbGraphicFactory;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;
import net.sf.exlp.util.io.StringUtil;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslDbGraphicUpdater <G extends JeeslGraphic<?,?,GT,?,?>, GT extends JeeslGraphicType<?,?,GT,G>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbGraphicUpdater.class);

	private JeeslGraphicFacade<?,?,?,G,GT,?,?> fGraphic;
	private static boolean debugOnInfo = true;
	
	private SvgFactoryBuilder<?,?,G,GT,?,?> fbGraphic;
	private EjbGraphicFactory<?,?,G,GT,?,?> efGraphic;
	
	public JeeslDbGraphicUpdater(SvgFactoryBuilder<?,?,G,GT,?,?> fbGraphic)
	{
		this.fbGraphic=fbGraphic;
		efGraphic = fbGraphic.efGraphic();
	}
	
	public void setFacade(JeeslGraphicFacade<?,?,?,G,GT,?,?> fGraphic){this.fGraphic=fGraphic;}
	
	
	public <W extends EjbWithCodeGraphic<G>> void update(Class<W> cStatus, List<Status> list)
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}		
		
		for(Status xml : list)
		{
			try
			{
				W ejb = fGraphic.fByCode(cStatus,xml.getCode());
				if(debugOnInfo) {logger.info(ejb.toString());}
				
				if(xml.isSetGraphic())
				{
					if(xml.getGraphic().isSetType() && xml.getGraphic().getType().isSetCode() && xml.getGraphic().getType().getCode().equals(JeeslGraphicType.Code.svg.toString())
						&& xml.getGraphic().isSetFile() && xml.getGraphic().getFile().isSetData())
					{
						updateSvg(cStatus,ejb,xml);
					}
					else if(xml.getGraphic().isSetType() && xml.getGraphic().getType().isSetCode() && xml.getGraphic().getType().getCode().equals(JeeslGraphicType.Code.symbol.toString()))
					{
						updateSymbol(cStatus,ejb,xml);	
					}
				}
				
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			catch (JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	public <W extends EjbWithGraphic<G>> void updateSvg(Class<W> cStatus, W ejb, Status xml) throws JeeslConstraintViolationException, JeeslLockingException
	{
		JaxbUtil.info(xml);
		GT svg = fGraphic.fByEnum(fbGraphic.getClassGraphicType(),JeeslGraphicType.Code.svg);
		
		G graphic;
		try
		{
			graphic = fGraphic.fGraphic(cStatus, ejb.getId());
			graphic.setType(svg);
		}
		catch (JeeslNotFoundException e)
		{
			if(debugOnInfo) {logger.info("Creating new "+fbGraphic.getClassGraphic());}
			graphic = fGraphic.save(efGraphic.build(svg));
			ejb.setGraphic(graphic);
			fGraphic.update(ejb);
		}
		graphic.setData(xml.getGraphic().getFile().getData().getValue());
		fGraphic.update(graphic);
	}
	
	public <W extends EjbWithCodeGraphic<G>> void updateSymbol(Class<W> cStatus, W ejb, Status xml) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		GT symbol = fGraphic.fByCode(fbGraphic.getClassGraphicType(), JeeslGraphicType.Code.symbol);
		
		G graphic;
		try
		{
			graphic = fGraphic.fGraphic(cStatus, ejb.getId());
			graphic.setType(symbol);
		}
		catch (JeeslNotFoundException e)
		{
			if(debugOnInfo) {logger.info("Creating new "+fbGraphic.getClassGraphic());}
			graphic = fGraphic.save(efGraphic.build(symbol));
			ejb.setGraphic(graphic);
			fGraphic.update(ejb);
		}
		logger.warn("NYI: Further Processing of symbol");
		
		fGraphic.update(graphic);
	}
}