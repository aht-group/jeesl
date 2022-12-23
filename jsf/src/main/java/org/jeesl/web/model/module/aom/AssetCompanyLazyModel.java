package org.jeesl.web.model.module.aom;

import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.util.JeeslLazyListHandler;
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

	private JeeslAssetCacheBean<?,?,REALM,RREF,COMPANY,SCOPE,?,?,?,?,?,?> cache;

	private RREF rref;

	public AssetCompanyLazyModel()
	{
		logger.info("instantiated: "+this.getClass().getSimpleName());
        llh = new JeeslLazyListHandler<>();
	}

	@Override public COMPANY getRowData(String rowKey){return llh.getRowData(cache.cachedCompany().get(rref),rowKey);}
    @Override public Object getRowKey(COMPANY account) {return llh.getRowKey(account);}

    public void setScope(JeeslAssetCacheBean<?,?,REALM,RREF,COMPANY,SCOPE,?,?,?,?,?,?> cache, RREF rref)
    {
    	this.cache=cache;
    	this.rref=rref;
    	llh.clear();
    }

    @Override
	public List<COMPANY> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		llh.clear();
		for(COMPANY item : cache.cachedCompany().get(rref))
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