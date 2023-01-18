package org.jeesl.interfaces.model.with.primitive.position;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithPosition extends EjbWithId
{
	public enum Attribute{position}
	
	public static String attributePosition = "position";
	
	public int getPosition();
	public void setPosition(int position);
}