package org.jeesl.controller.handler.lazy.module.aom;

import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.util.query.module.JeeslAomQuery;
import org.jeesl.jsf.util.PrimefacesPredicateBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.module.aom.asset.AomAsset;
import org.jeesl.model.ejb.module.aom.asset.AomAssetStatus;
import org.jeesl.model.ejb.module.aom.asset.AomAssetType;
import org.jeesl.model.ejb.module.aom.asset.AomView;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.module.aom.company.AomCompanyScope;
import org.jeesl.model.ejb.module.aom.event.AomEvent;
import org.jeesl.model.ejb.module.aom.event.AomEventStatus;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.util.query.ejb.module.EjbAomQuery;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAomAssetLazyModel extends LazyDataModel<AomAsset>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAomAssetLazyModel.class);
	private static final long serialVersionUID = 1L;

	private JeeslAomFacade<IoLang,IoDescription,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventStatus> fAsset;

	private List<AomAsset> data;
	private JeeslAomQuery<TenantRealm,AomCompanyScope,AomAsset,AomAssetType,AomEvent,AomEventStatus> filter; public EjbAomAssetLazyModel filter(JeeslAomQuery<TenantRealm,AomCompanyScope,AomAsset,AomAssetType,AomEvent,AomEventStatus> filter) {this.filter = filter; return this;}

	public static EjbAomAssetLazyModel instance(JeeslAomFacade<IoLang,IoDescription,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventStatus> fAsset) {return new EjbAomAssetLazyModel(fAsset);}
	private EjbAomAssetLazyModel(JeeslAomFacade<IoLang,IoDescription,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventStatus> fAsset)
	{
//		super(new AomAssetConverter());
		this.fAsset=fAsset;
	}

	private JeeslAomQuery<TenantRealm,AomCompanyScope,AomAsset,AomAssetType,AomEvent,AomEventStatus> query(Map<String,FilterMeta> filterBy)
	{
		EjbAomQuery<TenantRealm,AomCompanyScope,AomAsset,AomAssetType,AomEvent,AomEventStatus> query = new EjbAomQuery<>();
		query.apply(filter);
		PrimefacesPredicateBuilder.apply(filterBy,query);		
		return query;
	}
	
	public int count(Map<String,FilterMeta> filterBy) {return fAsset.fAomAssets(query(filterBy)).size();}
	@Override public List<AomAsset> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filterBy)
	{
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
		
		JeeslAomQuery<TenantRealm,AomCompanyScope,AomAsset,AomAssetType,AomEvent,AomEventStatus> q = query(filterBy);
		q.setFirstResult(first);
		q.maxResults(pageSize);
		
//		if(ObjectUtils.isEmpty(sortBy))
		{
//			q.orderBy(CqOrdering.ascending(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenArtifact.Attributes.group,JeeslIoMavenGroup.Attributes.code));
//			q.orderBy(CqOrdering.ascending(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenArtifact.Attributes.code));
//			q.orderBy(CqOrdering.ascending(JeeslIoMavenVersion.Attributes.position));
		}
		super.setRowCount(count(filterBy));
		data = fAsset.fAomAssets(q);
		logger.info("PageSize:"+pageSize+" page:"+first+" results:"+data.size() +" rows:" +getRowCount()+" time:"+ptt.toTotalTime());
		return data;
	}
	
	@Override public Object getRowKey(AomAsset item) {return item.getId();}
	@Override public AomAsset getRowData(String rowKey)
	{
		Long id = Long.valueOf(rowKey);
		for (AomAsset u : data)
		{
			if(u.getId()==id){return u;}
		}
		try {return fAsset.find(AomAsset.class,id);}
		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		return null;
	}
}