package org.jeesl.interfaces.util.query.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityQuery
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityQuery.class);
	
	public EjbSecurityQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{

	}
	
	//Fetches
	public <E extends Enum<E>> EjbSecurityQuery addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbSecurityQuery distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbSecurityQuery idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbSecurityQuery codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbSecurityQuery ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbSecurityQuery ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbSecurityQuery ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	
	
}