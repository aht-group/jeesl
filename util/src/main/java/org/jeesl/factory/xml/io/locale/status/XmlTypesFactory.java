package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Types;

public class XmlTypesFactory
{
	public static Types build()
	{
		Types xml = new Types();
		return xml;
	}
	
	public static Types build(String group)
	{
		Types xml = new Types();
		xml.setGroup(group);
		return xml;
	}
}