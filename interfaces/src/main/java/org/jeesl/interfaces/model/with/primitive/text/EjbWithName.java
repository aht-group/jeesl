package org.jeesl.interfaces.model.with.primitive.text;

public interface EjbWithName
{	
	public enum Attribute{name}
	
	public String getName();
	public void setName(String name);
}