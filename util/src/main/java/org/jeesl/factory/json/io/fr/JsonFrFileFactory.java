package org.jeesl.factory.json.io.fr;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jeesl.model.json.io.fr.JsonFrFile;

public class JsonFrFileFactory
{
	public static JsonFrFile build(String name, String category)
	{
		JsonFrFile json = new JsonFrFile();
		json.setName(name);
		json.setCategory(category);
		return json;
	}
	
	public static JsonFrFile build(String name, String category, InputStream is) throws IOException
	{
		JsonFrFile json = JsonFrFileFactory.build(name, category);
		json.setContent(IOUtils.toByteArray(is));
		return json;
	}
}