package org.jeesl.interfaces.util.query.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAomQuery<ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>
>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbAomQuery.class);
	
	public static <ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
					EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>>
				EjbAomQuery<ASSET,EVENT> instance(){return new EjbAomQuery<>();} 
	public EjbAomQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		assets = null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbAomQuery<ASSET,EVENT> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbAomQuery<ASSET,EVENT> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbAomQuery<ASSET,EVENT> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public EjbAomQuery<ASSET,EVENT> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbAomQuery<ASSET,EVENT> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}
	
	//LocalDate
	public EjbAomQuery<ASSET,EVENT> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbAomQuery<ASSET,EVENT> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbAomQuery<ASSET,EVENT> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<ASSET> assets; public List<ASSET> getAssets() {return assets;}
	public EjbAomQuery<ASSET,EVENT> add(ASSET asset) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.add(asset); return this;}
	public EjbAomQuery<ASSET,EVENT> addAssets(List<ASSET> list) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(list); return this;}
	public EjbAomQuery<ASSET,EVENT> addAssets(Collection<ASSET> collection) {if(Objects.isNull(assets)) {assets = new ArrayList<>();} assets.addAll(collection); return this;}
	

}