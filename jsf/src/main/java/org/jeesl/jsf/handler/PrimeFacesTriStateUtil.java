package org.jeesl.jsf.handler;

public class PrimeFacesTriStateUtil
{
	public static String booleanToTri(Boolean b)
	{
		if(b==null){return "0";}
		else
		{
			if(b){return "1";}
			else{return "2";}
		}
	}
	
	public static Boolean triToBoolean(String tri)
	{
		if(tri==null){return null;}
		else if(tri.equals("1")){return true;}
		else if(tri.equals("2")){return false;}
		else {return null;}
	}
}