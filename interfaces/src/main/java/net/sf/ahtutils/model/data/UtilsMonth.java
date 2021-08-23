package net.sf.ahtutils.model.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsMonth implements Serializable,Comparable<UtilsMonth>
{
	final static Logger logger = LoggerFactory.getLogger(UtilsMonth.class);
	public static final long serialVersionUID=1;


	private int year;
	public int getYear(){return year;}
	public void setYear(int year){this.year = year;}

	private int month;
	public int getMonth(){return month;}
	public void setMonth(int month){this.month = month;}


	public UtilsMonth(int year, int month)
	{
		this.year=year;
		this.month=month;
	}


	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(UtilsMonth.class.getSimpleName());
		sb.append(" y:").append(year);
		sb.append(" m:").append(month);
		return sb.toString();
	}

	public boolean equals(Object obj)
	{
		if (obj == null) {return false;}
		if (obj == this) {return true;}
		if (obj.getClass() != getClass()) {return false;}
		UtilsMonth rhs = (UtilsMonth) obj;
		return new EqualsBuilder().append(year, rhs.year).append(month, rhs.month).isEquals();
	}

	public int hashCode() {return new HashCodeBuilder(17,51).append(year).append(month).toHashCode();}
	
	public int compareTo(UtilsMonth other)
	{
	    return new CompareToBuilder().append(year, other.getYear()).append(month, other.getMonth()).toComparison();
	}
}
