package org.jeesl.interfaces.util.query;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.io.db.CqBool;
import org.jeesl.model.ejb.io.db.CqDate;
import org.jeesl.model.ejb.io.db.CqFetch;
import org.jeesl.model.ejb.io.db.CqId;
import org.jeesl.model.ejb.io.db.CqInteger;
import org.jeesl.model.ejb.io.db.CqLiteral;
import org.jeesl.model.ejb.io.db.CqOrdering;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslQuery extends Serializable,JeeslCoreQuery
{
	void reset();
	void debug(boolean debug);
	void debug(boolean debug, int ident);
	
	public String getLocaleCode();
	
	boolean isTupleLoad();
	void setTupleLoad(boolean tupleLoad);
	
	boolean isDistinct();
	void setDistinct(boolean distinct);

	void setFirstResult(Integer firstResult);

	void setMaxResults(Integer maxResults);
	
	List<String> getRootFetches();
	List<CqFetch> getGraphFetches();
	
	List<CqOrdering> getOrderings();
	List<CqLiteral> getLiterals();
	List<CqDate> getLocalDates();
	List<CqId> getIds();
	List<CqBool> getBools();
	List<CqInteger> getIntegers();
	
//	boolean isSortAscending();
//	void sort(String sortBy, boolean sortAscending);
	
	List<Long> getIdList();
	List<String> getCodeList();
	
	Boolean getBool1();
	void setBool1(Boolean bool1);
	
	String getString1();
	void setString1(String string1);
	
	String getString2();
	void setString2(String string2);
	
	String getString3();
	void setString3(String string3);
	
	LocalDate getLocalDate1();
	LocalDate getLocalDate2();
	LocalDate getLd3();
}