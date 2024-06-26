package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslChecklistQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbChecklistQuery<CL extends JeeslClChecklist<?,?,?>,
								CI extends JeeslClCheckItem<?,CL,?>,
								TL extends JeeslClTracklist<?,?,CL>
								>
			extends AbstractEjbQuery
			implements JeeslChecklistQuery<CL,CI,TL>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbChecklistQuery.class);
	
	public EjbChecklistQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		checkLists=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbChecklistQuery<CL,CI,TL> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbChecklistQuery<CL,CI,TL> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//List
	@Override public EjbChecklistQuery<CL,CI,TL> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbChecklistQuery<CL,CI,TL> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbChecklistQuery<CL,CI,TL> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbChecklistQuery<CL,CI,TL> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbChecklistQuery<CL,CI,TL> ld1(LocalDate ld1) {this.localDate1=ld1; return this;}
	public EjbChecklistQuery<CL,CI,TL> ld2(LocalDate ld2) {this.localDate2=ld2; return this;}
	public EjbChecklistQuery<CL,CI,TL> ld3(LocalDate ld3) {this.ld3=ld3; return this;}
	
	private List<CL> checkLists; public List<CL> getCheckLists() {return checkLists;}
	public EjbChecklistQuery<CL,CI,TL> add(CL ejb) {if(Objects.isNull(checkLists)) {checkLists = new ArrayList<>();} checkLists.add(ejb); return this;}
	public EjbChecklistQuery<CL,CI,TL> addCalendars(List<CL> list) {if(Objects.isNull(checkLists)) {checkLists = new ArrayList<>();} checkLists.addAll(list); return this;}
	public EjbChecklistQuery<CL,CI,TL> addCalendars(Collection<CL> collection) {if(Objects.isNull(checkLists)) {checkLists = new ArrayList<>();} checkLists.addAll(collection); return this;}
	
}