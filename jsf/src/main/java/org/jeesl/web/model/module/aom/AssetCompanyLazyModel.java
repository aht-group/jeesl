package org.jeesl.web.model.module.aom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAssetCacheBean;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.util.JeeslLazyListHandler;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetCompanyLazyModel <REALM extends JeeslTenantRealm<?,?,REALM,?>, RREF extends EjbWithId,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<?,?,SCOPE,?>>
					extends LazyDataModel<COMPANY>
{
	final static Logger logger = LoggerFactory.getLogger(AssetCompanyLazyModel.class);

	private static final long serialVersionUID = 1L;

	private final JeeslLazyListHandler<COMPANY> llh;
//	private final JeeslEjbFilter<COMPANY> filter;

	private JeeslAomCompanyCache<REALM,COMPANY,SCOPE> cache; public void setCache(JeeslAomCompanyCache<REALM,COMPANY,SCOPE> cache) {this.cache = cache;}

	private RREF rref;
	private TenantIdentifier<REALM> identifier;

	public AssetCompanyLazyModel()
	{
		logger.info("instantiated: "+this.getClass().getSimpleName());
        llh = new JeeslLazyListHandler<>();
	}
	
//    public void setScope(JeeslAssetCacheBean<REALM,RREF,COMPANY,SCOPE,?,?,?,?,?,?> bCache, RREF rref)
//    {
//    	this.bCache=bCache;
//    	this.rref=rref;
//    	llh.clear();
//    }
    public void updateIdentifier(TenantIdentifier<REALM> identifier)
    {
    	this.identifier=identifier;
    	llh.clear();
    }

	@Override public Object getRowKey(COMPANY account) {return llh.getRowKey(account);}
	@Override public COMPANY getRowData(String rowKey)
	{
		return llh.getRowData(cache.getCachedAllCompanies().get(identifier), rowKey);
	}
   
    @Override
	public List<COMPANY> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		llh.clear();
		List<COMPANY> list = new ArrayList<>();
		list.addAll(cache.getCachedAllCompanies().get(identifier));
		
		for(COMPANY item : list)
		{
//			if(filter.matches(filters,item))
			{llh.add(item);}
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

		return llh.paginator(first,pageSize);
	}
}