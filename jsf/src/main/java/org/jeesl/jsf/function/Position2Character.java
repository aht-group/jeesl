package org.jeesl.jsf.function;

public final class Position2Character
{
	public Position2Character()
    {

    }
    
    public static String toChar(Integer value)
    {
    	return Character.toString((char) (value+64));
    }
}