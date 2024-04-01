package org.jeesl.util.query.ejb.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoAttributeQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoAttributeQuery<CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,?,?>,
								CONTAINER extends JeeslAttributeContainer<?,DATA>,
								DATA extends JeeslAttributeData<CRITERIA,?,CONTAINER>>
			extends AbstractEjbQuery
			implements JeeslIoAttributeQuery<CRITERIA,CONTAINER,DATA>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbIoAttributeQuery.class);
	
	public EjbIoAttributeQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		criterias=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	
	private List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> add(CRITERIA calendar) {if(Objects.isNull(criterias)) {criterias = new ArrayList<>();} criterias.add(calendar); return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> addCriterias(List<CRITERIA> list) {if(Objects.isNull(criterias)) {criterias = new ArrayList<>();} criterias.addAll(list); return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> addCriterias(Collection<CRITERIA> collection) {if(Objects.isNull(criterias)) {criterias = new ArrayList<>();} criterias.addAll(collection); return this;}
	
	private List<CONTAINER> containers; public List<CONTAINER> getContainers() {return containers;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> add(CONTAINER calendar) {if(Objects.isNull(containers)) {containers = new ArrayList<>();} containers.add(calendar); return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> addContainers(List<CONTAINER> list) {if(Objects.isNull(containers)) {containers = new ArrayList<>();} containers.addAll(list); return this;}
	public EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> addContainers(Collection<CONTAINER> collection) {if(Objects.isNull(containers)) {containers = new ArrayList<>();} containers.addAll(collection); return this;}

}