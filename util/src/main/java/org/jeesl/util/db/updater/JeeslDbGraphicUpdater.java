package org.jeesl.util.db.updater;

import java.util.List;
import java.util.Objects;

import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;
import net.sf.exlp.util.io.StringUtil;

public class JeeslDbGraphicUpdater <G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<?,?,GT,G>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbGraphicUpdater.class);

	private JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic;
	private static boolean debugOnInfo = true;
	
	private SvgFactoryBuilder<?,?,G,GT,?,?> fbGraphic;
	private EjbGraphicFactory<?,?,G,GT,?> efGraphic;
	
	public JeeslDbGraphicUpdater(SvgFactoryBuilder<?,?,G,GT,?,?> fbGraphic)
	{
		this.fbGraphic=fbGraphic;
		efGraphic = fbGraphic.efGraphic();
	}
	
	public void setFacade(JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic){this.fGraphic=fGraphic;}
	
	
	public <W extends EjbWithCodeGraphic<G>> void update(Class<W> cStatus, List<Status> list)
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}		
		
		for(Status xml : list)
		{
			try
			{
				W ejb = fGraphic.fByCode(cStatus,xml.getCode());
				if(debugOnInfo) {logger.info(ejb.toString());}
				
				if(Objects.nonNull(xml.getGraphic()))
				{
					if(Objects.nonNull(xml.getGraphic().getType())
						&& Objects.nonNull(xml.getGraphic().getType().getCode())
						&& xml.getGraphic().getType().getCode().equals(JeeslGraphicType.Code.svg.toString())
						&& Objects.nonNull(xml.getGraphic().getFile())
						&& Objects.nonNull(xml.getGraphic().getFile().getData()))
					{
						updateSvg(cStatus,ejb,xml);
					}
					else if(Objects.nonNull(xml.getGraphic().getType()) && Objects.nonNull(xml.getGraphic().getType().getCode()) && xml.getGraphic().getType().getCode().equals(JeeslGraphicType.Code.symbol.toString()))
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
	
	public <W extends EjbWithCodeGraphic<G>> void updateSymbol(Class<W> cStatus, W ejb, Status xml) throws JeeslConstraintViolationException, JeeslLockingException
	{
		GT symbol = fGraphic.fByEnum(fbGraphic.getClassGraphicType(), JeeslGraphicType.Code.symbol);
		
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