package org.jeesl.interfaces.util.query;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqLong;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslQuery extends JeeslCoreQuery
{
	void reset();
	void debug(boolean debug);
	void debug(boolean debug, int ident);
	
	public String getLocaleCode();
	
	boolean isTupleLoad();
	void setTupleLoad(boolean tupleLoad);
	
	Boolean getOnlyUnreferenced();
	void setOnlyUnreferenced(Boolean onlyUnreferenced);
	
	boolean isDistinct();
	void setDistinct(boolean distinct);
	
	List<String> getRootFetches();
	List<CqGraphFetch> getGraphFetches();
//	List<JeeslCqOrdering> getCqOrderings();
	
	List<JeeslCqLiteral> getCqLiterals();
	List<JeeslCqLong> getCqLongs();
	
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