package org.jeesl.web.model.module.aom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;
import org.jeesl.jsf.util.JeeslLazyListHandler;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetEventLazyModel <ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
									EVENT extends JeeslAomEvent<?,ASSET,ETYPE,ESTATUS,?,USER,?>,
									ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
									ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>,
									USER extends JeeslSimpleUser>
					extends LazyDataModel<EVENT>
{
	final static Logger logger = LoggerFactory.getLogger(AssetEventLazyModel.class);

	private static final long serialVersionUID = 1L;

	private final List<EVENT> list;
	private final JeeslLazyListHandler<EVENT> llh;

	private final Comparator<EVENT> cpEvent;
	private final ThMultiFilterHandler<ETYPE> thfEventType;
	private final SbMultiHandler<ETYPE> sbEventType;

	public AssetEventLazyModel(Comparator<EVENT> cpEvent, ThMultiFilterHandler<ETYPE> thfEventType, SbMultiHandler<ETYPE> sbEventType)
	{
		this.cpEvent=cpEvent;
		this.thfEventType=thfEventType;
		this.sbEventType=sbEventType;
        llh = new JeeslLazyListHandler<>();
        list = new ArrayList<>();
	}

	@Override public EVENT getRowData(String rowKey){return llh.getRowData(list,rowKey);}
    @Override public Object getRowKey(EVENT account) {return llh.getRowKey(account);}
    public void clear() {list.clear();}

    public void reloadScope(JeeslAssetFacade<?,?,?,?,?,ASSET,?,?,?,EVENT,ETYPE,ESTATUS,USER,?,?> fAsset, ASSET asset)
    {
		this.clear();
		list.addAll(fAsset.fAssetEvents(asset));
		Collections.sort(list,cpEvent);
		logger.info("Reloaded "+list.size());
		if(thfEventType!=null)
		{
			Set<ETYPE> set = new HashSet<>();
			for(EVENT e : list) {set.add(e.getType());}
			thfEventType.clear();
			for(ETYPE t : set)
			{
				if(sbEventType==null) {thfEventType.getList().add(t);}
				else if(!sbEventType.isSelected(t)){thfEventType.getList().add(t);}
			}
		}
    }

	@Override public List<EVENT> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		llh.clear();
		for(EVENT event : list)
		{
			boolean thfTypeMatches = true;
			boolean sbhTypeMatches = true;
			if(thfEventType!=null) {thfTypeMatches = thfEventType.isSelected(event.getType());}
			if(sbEventType!=null) {sbhTypeMatches = sbEventType.isSelected(event.getType());}
//			if(filter.matches(filters,item))
			if(thfTypeMatches || sbhTypeMatches){llh.add(event);}
		}

		if(sortField != null)
		{

		}
		else
		{
			logger.info("Default Sorting");
//			Collections.sort(llh.getTmp(),cpBeneficiary);
		}

		this.setRowCount(llh.size());

		List<EVENT> x = llh.paginator(first,pageSize);
		logger.info("Rows "+this.getRowCount()+" x:"+x.size());
		return x;
	}
}