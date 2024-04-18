package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslAomQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAomQuery<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>
>
			extends AbstractEjbQuery
			implements JeeslAomQuery<REALM,ASSET,EVENT>
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
	
	//Fetches
	public <E extends Enum<E>> EjbAomQuery<REALM,ASSET,EVENT> addRootFetch(E e) {if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbAomQuery<REALM,ASSET,EVENT> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbAomQuery<REALM,ASSET,EVENT> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbAomQuery<REALM,ASSET,EVENT> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbAomQuery<REALM,ASSET,EVENT> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbAomQuery<REALM,ASSET,EVENT> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}
	
	//LocalDate
	public EjbAomQuery<REALM,ASSET,EVENT> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbAomQuery<REALM,ASSET,EVENT> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbAomQuery<REALM,ASSET,EVENT> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<ASSET> assets; public List<ASSET> getAssets() {return assets;}
	public EjbAomQuery<REALM,ASSET,EVENT> add(ASSET asset) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.add(asset); return this;}
	public EjbAomQuery<REALM,ASSET,EVENT> addAssets(List<ASSET> list) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(list); return this;}
	public EjbAomQuery<REALM,ASSET,EVENT> addAssets(Collection<ASSET> collection) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(collection); return this;}
	
}