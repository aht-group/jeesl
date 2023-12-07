package org.jeesl.interfaces.util.query.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslJobQuery extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(JeeslJobQuery.class);
	
	public JeeslJobQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{

	}
	
	//Fetches
	public <E extends Enum<E>> JeeslJobQuery addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public JeeslJobQuery distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public JeeslJobQuery id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> JeeslJobQuery ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public JeeslJobQuery idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public JeeslJobQuery codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public JeeslJobQuery ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public JeeslJobQuery ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public JeeslJobQuery ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
}