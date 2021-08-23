package org.jeesl.model.json.util.time;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class JsonYear implements Serializable,EjbWithId
{
	public static final long serialVersionUID=1;

	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private int year;
	public int getYear() {return year;}
	public void setYear(int year) {this.year = year;}
	
	@Override public boolean equals(Object object){return (object instanceof JsonYear) ? id == ((JsonYear) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,49).append(id).toHashCode();}
}