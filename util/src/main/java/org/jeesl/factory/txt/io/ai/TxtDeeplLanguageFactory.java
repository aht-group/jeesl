package org.jeesl.factory.txt.io.ai;

public class TxtDeeplLanguageFactory
{
	public static String toDeepl (String erp)
	{
		if(erp.contains("en")) {return "en-GB";}
		else {return erp;}
	}
}