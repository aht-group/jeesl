package org.jeesl.interfaces.util.query.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbQuery<SYSTEM extends JeeslIoSsiSystem<?,?>
>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbQuery.class);
	
	public EjbIoDbQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		systems=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbIoDbQuery<SYSTEM> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoDbQuery<SYSTEM> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbIoDbQuery<SYSTEM> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoDbQuery<SYSTEM> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoDbQuery<SYSTEM> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbIoDbQuery<SYSTEM> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbIoDbQuery<SYSTEM> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	public EjbIoDbQuery<SYSTEM> add(SYSTEM system) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.add(system); return this;}
	public EjbIoDbQuery<SYSTEM> addCalendars(List<SYSTEM> list) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.addAll(list); return this;}
	public EjbIoDbQuery<SYSTEM> addCalendars(Collection<SYSTEM> collection) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.addAll(collection); return this;}
}