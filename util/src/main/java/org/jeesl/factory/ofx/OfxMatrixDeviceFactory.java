package org.jeesl.factory.ofx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.factory.xls.system.io.report.XlsColumnFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Table;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlHeadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxMatrixDeviceFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxStatusTableFactory.class);
	
	private Map<String,String> map;
	
	public static OfxMatrixDeviceFactory instance()
	{
		return new OfxMatrixDeviceFactory();
	}
	public OfxMatrixDeviceFactory()
	{
		map = new HashMap<>();
	}
	
	public Table build(JsonMatrixDevice device)
	{
		map.clear();
		
		Table table = new Table();
		table.setTitle(XmlTitleFactory.build(device.getName()));
		
		Content content = new Content();
		content.setHead(buildHead(device));
		content.getBody().add(buildBody(device));
		table.setContent(content);
		return table;
	}
		
	private static Head buildHead(JsonMatrixDevice device)
	{
		Row row = new Row();
		for(int i=0; i<device.getColumns(); i++)
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(""+(i+1)));
		}
		return XmlHeadFactory.build(row);
	}
	
	private Body buildBody(JsonMatrixDevice device)
	{
		Body body = new Body();
		List<List<String>> partitions = ListUtils.partition(device.getData(),device.getColumns());
		for(List<String> partition : partitions)
		{
			body.getRow().add(buildRow(partition));
		}
		return body;
	}
	
	private Row buildRow(List<String> datas)
	{		
		Row row = new Row();
		for(String cell : datas)
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(toCode(cell)));
		}
		return row;
	}
	
	private String toCode(String colour)
	{
		if(!map.containsKey(colour))
		{
			String code = XlsColumnFactory.index2code(map.size());
			map.put(colour,code);
		}
		return map.get(colour);
	}
}