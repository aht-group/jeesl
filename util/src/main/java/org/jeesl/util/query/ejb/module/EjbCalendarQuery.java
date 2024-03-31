package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalendarQuery<CAL extends JeeslCalendar<?,?>,
								TYPE extends JeeslCalendarItemType<?,?,?,TYPE,?>
>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbCalendarQuery.class);
	
	public EjbCalendarQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		calendars=null;
		types=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbCalendarQuery<CAL,TYPE> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbCalendarQuery<CAL,TYPE> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbCalendarQuery<CAL,TYPE> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbCalendarQuery<CAL,TYPE> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbCalendarQuery<CAL,TYPE> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbCalendarQuery<CAL,TYPE> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbCalendarQuery<CAL,TYPE> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbCalendarQuery<CAL,TYPE> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbCalendarQuery<CAL,TYPE> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CAL> calendars; public List<CAL> getCalendars() {return calendars;}
	public EjbCalendarQuery<CAL,TYPE> add(CAL calendar) {if(Objects.isNull(calendars)) {calendars = new ArrayList<>();} calendars.add(calendar); return this;}
	public EjbCalendarQuery<CAL,TYPE> addCalendars(List<CAL> list) {if(Objects.isNull(calendars)) {calendars = new ArrayList<>();} calendars.addAll(list); return this;}
	public EjbCalendarQuery<CAL,TYPE> addCalendars(Collection<CAL> collection) {if(Objects.isNull(calendars)) {calendars = new ArrayList<>();} calendars.addAll(collection); return this;}
	
	private List<TYPE> types; public List<TYPE> getTypes() {return types;}
	public EjbCalendarQuery<CAL,TYPE> add(TYPE type) {if(Objects.isNull(types)) {types = new ArrayList<>();} types.add(type); return this;}
}