package org.jeesl.jsf.handler.ui;

public class JeeslTriStateHandler
{
	private Boolean valueBoolean; public Boolean getValueBoolean() {return valueBoolean;} public void setValueBoolean(Boolean valueBoolean) {this.valueBoolean = valueBoolean;}
	private String valueString; public String getValueString() {return valueString;} public void setValueString(String valueString) {this.valueString = valueString;}

	public static JeeslTriStateHandler instance() {return new JeeslTriStateHandler();}
	
	public void stringToBoolean() {valueBoolean = JeeslTriStateHandler.triToBoolean(valueString);}
	public void booleanToString() {valueString = JeeslTriStateHandler.booleanToTri(valueBoolean);}
	
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