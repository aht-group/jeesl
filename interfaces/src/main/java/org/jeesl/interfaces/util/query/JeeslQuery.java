package org.jeesl.interfaces.util.query;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.io.db.CqDate;
import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.model.ejb.io.db.CqLiteral;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslQuery extends Serializable
{
	public String getLocaleCode();
	
	void reset();
	
	boolean isTupleLoad();
	void setTupleLoad(boolean tupleLoad);
	
	boolean isDistinct();
	void setDistinct(boolean distinct);

	Integer getFirstResult();
	void setFirstResult(Integer firstResult);

	Integer getMaxResults();
	void setMaxResults(Integer maxResults);
	
	List<String> getRootFetches();
	
	List<CqOrdering> getOrderings();
	String getSortBy();
	boolean isSortAscending();
	
	void sort(String sortBy, boolean sortAscending);
//	void noSort();
	
	void debug(boolean debug);
	void debug(boolean debug, int ident);
	
	List<Long> getIdList();
	List<String> getCodeList();
	
	Boolean getBool1();
	void setBool1(Boolean bool1);
	
	CqLiteral getLiteral1();
	CqLiteral getLiteral2();
	CqLiteral getLiteral3();
	CqLiteral getLiteral4();
	CqLiteral getLiteral5();
	
	String getString1();
	void setString1(String string1);
	
	String getString2();
	void setString2(String string2);
	
	String getString3();
	void setString3(String string3);
	
//	CqDate getDate1();
//	CqDate getDate2();
	
	List<CqDate> getLocalDates();
	
	LocalDate getLocalDate1();
	LocalDate getLocalDate2();
	LocalDate getLd3();
}