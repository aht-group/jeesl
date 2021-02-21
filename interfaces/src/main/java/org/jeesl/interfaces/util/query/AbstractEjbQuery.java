package org.jeesl.interfaces.util.query;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEjbQuery implements Serializable,JeeslQuery
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
	
	private boolean tupleLoad;
	@Override public boolean isTupleLoad() {return tupleLoad;}
	@Override public void setTupleLoad(boolean tupleLoad) {this.tupleLoad = tupleLoad;}

	private boolean distinct;
	@Override public boolean isDistinct() {return distinct;}
	@Override public void setDistinct(boolean distinct) {this.distinct = distinct;}

	private Integer firstResult;
	@Override public Integer getFirstResult() {return firstResult;}
	@Override public void setFirstResult(Integer firstResult) {this.firstResult = firstResult;}

	private Integer maxResults;
	@Override public Integer getMaxResults() {return maxResults;}
	@Override public void setMaxResults(Integer maxResults) {this.maxResults = maxResults;}
	
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
	
	//Sorting
	private String sortBy;
	@Override public String getSortBy() {return sortBy;}

	private boolean sortAscending;
	@Override public boolean isSortAscending() {return sortAscending;}
	
	@Override public void sort(String sortBy, boolean sortAscending)
	{
		this.sortBy=sortBy;
		this.sortAscending=sortAscending;
	}
	@Override public void noSort()
	{
		sortBy=null;
	}
	@Override public boolean withSort(){return (sortBy!=null && sortBy.trim().length()>0);}


	@Override public void debug(boolean debug){debug(debug,0);}
	@Override public void debug(boolean debug, int ident)
	{
		if(debug)
		{
			logger.info(StringUtils.repeat("\t",ident)+"distinct: "+distinct);
			if(firstResult!=null){logger.info(StringUtils.repeat("\t",ident)+"firstResult: "+firstResult);}
			if(maxResults!=null){logger.info(StringUtils.repeat("\t",ident)+"maxResults: "+maxResults);}
		}
	}
}