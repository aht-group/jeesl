package org.jeesl.util.query.json;

import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;

public class JsonIotQueryProvider
{	
	public static JsonMatrixDevice device()
	{
		JsonMatrixDevice xml = new JsonMatrixDevice();
		xml.setId(0l);
		xml.setCode("");
		xml.setName("");
		xml.setRows(0);
		xml.setColumns(0);
		xml.setBrightness(0);
		return xml;
	}
}