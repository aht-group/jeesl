package org.jeesl.model.json.db.tuple;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class JsonIdTuple extends AbstractJsonTuple implements Serializable,EjbWithId
{
	public static final long serialVersionUID=1;
	
	public JsonIdTuple()
	{
		
	}
	
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private double d1;
	public double getD1() {return d1;}
	public void setD1(double d1) {this.d1 = d1;}
}