package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslAomQuery;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAomQuery<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
							ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>,
							ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>
							
>
			extends AbstractEjbQuery
			implements JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbAomQuery.class);
	
	public EjbAomQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		assets = null;
	}
	
	//Pagination
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> maxResults(Integer max) {super.setMaxResults(max); return this;}
	
	public void apply(JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> other)
	{
		if(Objects.nonNull(other))
		{
			if(Objects.nonNull(other.getTenantIdentifier())) {this.tenantIdentifier = other.getTenantIdentifier();}
			if(ObjectUtils.isNotEmpty(other.getAomAssetTypes())) {this.aomAssetTypes = other.getAomAssetTypes();}
		}
	}
	
	
	//Fetches
	public <E extends Enum<E>> EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> addRootFetch(E e) {if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> add(JeeslCqOrdering cq) {super.addOrdering(cq); return this;}
	@Override public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}
	
	private TenantIdentifier<REALM> tenantIdentifier;
	@Override public TenantIdentifier<REALM> getTenantIdentifier() {return tenantIdentifier;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> tenant(TenantIdentifier<REALM> identifier) {this.tenantIdentifier=identifier; return this;}
	
	//LocalDate
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	// AOM
	private List<SCOPE> aomCompanyScopes;
	@Override public List<SCOPE> getAomCompanyScopes() {return aomCompanyScopes;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> add(SCOPE scope) {if(Objects.isNull(aomCompanyScopes)) {aomCompanyScopes = new ArrayList<>();} aomCompanyScopes.add(scope); return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> addAomCompanyScopes(List<SCOPE> list) {if(Objects.isNull(aomCompanyScopes)) {aomCompanyScopes = new ArrayList<>();} aomCompanyScopes.addAll(list); return this;}
	
	private List<ASSET> assets; 
	@Override public List<ASSET> getAssets() {return assets;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> add(ASSET asset) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.add(asset); return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> addAssets(List<ASSET> list) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(list); return this;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> addAssets(Collection<ASSET> collection) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(collection); return this;}

	private List<ATYPE> aomAssetTypes; @Override public List<ATYPE> getAomAssetTypes() {return aomAssetTypes;}
	
	private List<ESTATUS> aomEventStatus;
	@Override public List<ESTATUS> getAomEventStatus() {return aomEventStatus;}
	public EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> addAomEventStatus(Collection<ESTATUS> collection) {if(Objects.isNull(aomEventStatus)) {aomEventStatus = new ArrayList<>();} aomEventStatus.addAll(collection); return this;}
}