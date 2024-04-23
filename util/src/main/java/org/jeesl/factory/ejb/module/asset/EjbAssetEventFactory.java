package org.jeesl.factory.ejb.module.asset;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAssetEventFactory<COMPANY extends JeeslAomCompany<?,?>,
								ASSET extends JeeslAomAsset<?,ASSET,COMPANY,?,?>,
								EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,?,?>,
								ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
								ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAssetEventFactory.class);
	
	private final AomFactoryBuilder<?,?,?,COMPANY,?,ASSET,?,?,?,EVENT,ETYPE,ESTATUS,M,MT,?,?,?> fbAsset;
	
    public EjbAssetEventFactory(final AomFactoryBuilder<?,?,?,COMPANY,?,ASSET,?,?,?,EVENT,ETYPE,ESTATUS,M,MT,?,?,?> fbAsset)
    {
        this.fbAsset = fbAsset;
    }
	
	public EVENT build(ASSET asset, ETYPE type, MT markupType)
	{
		try
		{
			M markup = fbAsset.getClassMarkup().newInstance();
			markup.setLkey(JeeslLocale.none);
			markup.setType(markupType);
			markup.setContent("");
			
			EVENT ejb = fbAsset.getClassEvent().newInstance();
			ejb.getAssets().add(asset);
			ejb.setRecord(new Date());
			ejb.setName("");
			ejb.setRemark("");
			ejb.setType(type);
			ejb.setMarkup(markup);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public EVENT clone(EVENT event, MT markupType)
	{
		try
		{
			M markup = fbAsset.getClassMarkup().newInstance();
			if(event.getMarkup()!=null)
			{
				markup.setLkey(event.getMarkup().getLkey());
				markup.setType(event.getMarkup().getType());
				markup.setContent(event.getMarkup().getContent());
			}
			markup.setLkey(JeeslLocale.none);
			markup.setType(markupType);
			
			EVENT ejb = fbAsset.getClassEvent().newInstance();
			ejb.getAssets().addAll(event.getAssets());
			ejb.setType(event.getType());
			ejb.setStatus(event.getStatus());
			ejb.setRecord(new Date());
			ejb.setName("CLONE "+event.getName());
			ejb.setRemark(event.getRemark());
			ejb.setMarkup(markup);
			ejb.setCompany(event.getCompany());
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
	}
	
	
	public void converter(JeeslFacade facade, EVENT event)
	{
		event.setType(facade.find(fbAsset.getClassEventType(),event.getType()));
		event.setStatus(facade.find(fbAsset.getClassEventStatus(),event.getStatus()));
		if(Objects.nonNull(event.getCompany())) {event.setCompany(facade.find(fbAsset.getClassCompany(),event.getCompany()));}
	}
	
	public void ejb2nnb(EVENT event, NullNumberBinder nnb)
	{
		nnb.doubleToA(event.getAmount());
	}
	public void nnb2ejb(EVENT event, NullNumberBinder nnb)
	{
		event.setAmount(nnb.aToDouble());
	}
	
	public Map<LocalDate,List<EVENT>> toMapDate(List<EVENT> list)
	{
		Map<LocalDate,List<EVENT>> map = new HashMap<>();
		for(EVENT e : list)
		{
			LocalDate ld = DateUtil.toLocalDate(e.getRecord());
			if(!map.containsKey(ld)) {map.put(ld,new ArrayList<>());}
			map.get(ld).add(e);
		}
		return map;
	}
}