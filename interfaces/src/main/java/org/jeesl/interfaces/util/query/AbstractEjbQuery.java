package org.jeesl.interfaces.util.query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.CqBool;
import org.jeesl.model.ejb.io.db.CqDate;
import org.jeesl.model.ejb.io.db.CqFetch;
import org.jeesl.model.ejb.io.db.CqId;
import org.jeesl.model.ejb.io.db.CqInteger;
import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.primefaces.model.FilterMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEjbQuery implements JeeslQuery
{
	final static Logger logger = LoggerFactory.getLogger(AbstractEjbQuery.class);
	private static final long serialVersionUID = 1;

	private String localeCode; public String getLocaleCode() {return localeCode;}

	public AbstractEjbQuery() {this("en");}
	public AbstractEjbQuery(String localeCode)
	{
		this.localeCode=localeCode;
		distinct = false;
	}
	
	public void reset()
	{
		codeList=null;
	}
	
	private boolean tupleLoad;
	@Override public boolean isTupleLoad() {return tupleLoad;}
	public Boolean getTupleLoad() {return tupleLoad;}
	@Override public void setTupleLoad(boolean tupleLoad) {this.tupleLoad = tupleLoad;}

	private boolean distinct;
	@Override public boolean isDistinct() {return distinct;}
	public Boolean getDistinct() {return Boolean.valueOf(distinct);}
	public abstract AbstractEjbQuery distinct(boolean distinct);
	@Override public void setDistinct(boolean distinct) {this.distinct = distinct;}

	private Integer firstResult;
	@Override public Integer getFirstResult() {return firstResult;}
	@Override public void setFirstResult(Integer firstResult) {this.firstResult = firstResult;}

	private Integer maxResults;
	@Override public Integer getMaxResults() {return maxResults;}
	@Override public void setMaxResults(Integer maxResults) {this.maxResults = maxResults;}
		
	protected Long id1;
	public Long getId1() {return id1;}
	
	protected Long id2;
	public Long getId2() {return id2;}
	public void setId2(Long id2) {this.id2 = id2;}

	protected Long id3;
	public Long getId3() {return id3;}
	public void setId3(Long id3) {this.id3 = id3;}

	//Lists
	protected List<Long> idList;
	@Override public List<Long> getIdList() {return idList;}
	public abstract AbstractEjbQuery id(EjbWithId id);
	public abstract <T extends EjbWithId> AbstractEjbQuery ids(List<T> ids);
	public abstract AbstractEjbQuery idList(List<Long> list);
	
	protected List<String> codeList;
	@Override public List<String> getCodeList() {return codeList;}
	public abstract AbstractEjbQuery codeList(List<String> list);

	//Dates
	private Date fromDate1;
	public Date getFromDate1() {return fromDate1;}
	public void setFromDate1(Date fromDate1) {this.fromDate1 = fromDate1;}
	public boolean withFromDate1() {return fromDate1!=null;}

	private Date toDate1;
	public Date getToDate1() {return toDate1;}
	public void setToDate1(Date toDate1) {this.toDate1 = toDate1;}
	public boolean withToDate1() {return toDate1!=null;}
	
	private Date toDate2;
	public Date getToDate2() {return toDate2;}
	public void setToDate2(Date toDate2) {this.toDate2 = toDate2;}
	
	private Date toDate3;
	public Date getToDate3() {return toDate3;}
	public void setToDate3(Date toDate3) {this.toDate3 = toDate3;}
	
	//LocalDate
	
	
	protected LocalDate localDate1; @Override public LocalDate getLocalDate1() {return localDate1;}
	protected LocalDate localDate2; @Override public LocalDate getLocalDate2() {return localDate2;}
	
	protected LocalDate ld3; public LocalDate getLd3() {return ld3;}
	
	//JEESL-CQ
	protected List<CqId> ids; @Override public List<CqId> getIds() {return ids;}	
	protected List<CqBool> booleans; @Override public List<CqBool> getBools() {return booleans;}
	protected List<CqInteger> integers; @Override public List<CqInteger> getIntegers() {return integers;}
	protected List<JeeslCqLiteral> literals; @Override public List<JeeslCqLiteral> getCqLiterals() {return literals;}
	protected List<CqDate> localDates; @Override public List<CqDate> getLocalDates() {return localDates;}
	private List<CqOrdering> orderings; @Override public List<CqOrdering> getOrderings() {return orderings;}
	
	@Override public void addCqLiteral(JeeslCqLiteral cq) {if(Objects.isNull(literals)) {literals = new ArrayList<>();} literals.add(cq);}
	protected void addId(CqId literal) {if(Objects.isNull(ids)) {ids = new ArrayList<>();} ids.add(literal);}
	protected void addProtected(CqBool cq) {if(Objects.isNull(booleans)) {booleans = new ArrayList<>();} booleans.add(cq);}
	protected void addProtected(CqInteger i) {if(Objects.isNull(integers)) {integers = new ArrayList<>();} integers.add(i);}
	
	protected void addProtected(CqDate date) {if(Objects.isNull(localDates)) {localDates = new ArrayList<>();} localDates.add(date);}
	protected void addOrdering(CqOrdering ordering) {if(Objects.isNull(orderings)) {orderings = new ArrayList<>();} orderings.add(ordering);}
	
	//GraphFetch
	private List<CqFetch> graphFetches;
	@Override public List<CqFetch> getGraphFetches() {return graphFetches;}
	protected void addGraphFetch(CqFetch fetch) {if(Objects.isNull(graphFetches)) {graphFetches = new ArrayList<>();} graphFetches.add(fetch);}
	
	
	//Strings
	private String string1;
	@Override public String getString1() {return string1;}
	@Override public void setString1(String string1) {this.string1 = string1;}
	
	private String string2;
	@Override public String getString2() {return string2;}
	@Override public void setString2(String string2) {this.string2 = string2;}
	
	private String string3;
	@Override public String getString3() {return string3;}
	@Override public void setString3(String string3) {this.string3 = string3;}

	//Booleans
	private Boolean bool1;
	@Override public Boolean getBool1() {return bool1;}
	@Override public void setBool1(Boolean bool1) {this.bool1 = bool1;}
	

	
	private String sortBy; //@Override public String getSortBy() {return sortBy;}
	private boolean sortAscending; //@Override public boolean isSortAscending() {return sortAscending;}
//	@Override public void sort(String sortBy, boolean sortAscending) {this.sortBy=sortBy; this.sortAscending=sortAscending;}

	//Fetches
	protected List<String> rootFetches; public List<String> getRootFetches() {return rootFetches;}
	public abstract <E extends Enum<E>> AbstractEjbQuery addRootFetch(E e);
	
	@Override public void debug(boolean debug){debug(debug,0);}
	@Override public void debug(boolean debug, int ident)
	{
		if(debug)
		{
			logger.info(StringUtils.repeat("\t",ident)+"distinct: "+distinct);
			if(firstResult!=null){logger.info(StringUtils.repeat("\t",ident)+"firstResult: "+firstResult);}
			if(maxResults!=null){logger.info(StringUtils.repeat("\t",ident)+"maxResults: "+maxResults);}
			if(Objects.nonNull(tupleLoad)) {logger.info("Tuple-Load: "+tupleLoad);}
		}
	}
	
	protected List<String> debugStrings()
	{
		List<String> list = new ArrayList<>();
		if(Objects.nonNull(firstResult)){list.add("firstResult: "+firstResult);}
		if(Objects.nonNull(maxResults)){list.add("maxResults: "+maxResults);}
		if(Objects.nonNull(tupleLoad)) {list.add("Tuple-Load: "+tupleLoad);}
		if(Objects.nonNull(localDate1)) {list.add(LocalDate.class.getSimpleName()+".1: "+localDate1.toString());}
		if(Objects.nonNull(localDate2)) {list.add(LocalDate.class.getSimpleName()+".2: "+localDate2.toString());}
		if(ObjectUtils.isNotEmpty(literals)) {for(JeeslCqLiteral l : literals) {list.add(l.toString());}}
		return list;
	}
	
//	public void applyPrimefaces(Map<String,FilterMeta> filterBy)
//	{
//		if(Objects.nonNull(filterBy))
//		{
//			for(FilterMeta meta : filterBy.values().stream().filter(m -> Objects.nonNull(m.getFilterValue())).collect(Collectors.toList()))
//			{
//				query.addCqLiteral(CqLiteral.contains(meta.getFilterValue().toString(),meta.getFilterField()));
//			}
//		}
//	}
}