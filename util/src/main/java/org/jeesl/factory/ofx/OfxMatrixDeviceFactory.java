package org.jeesl.factory.ofx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xls.system.io.report.XlsColumnFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlHeadFactory;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxMatrixDeviceFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxStatusTableFactory.class);
	
	private Map<String,String> map;
	private String colorOff; public OfxMatrixDeviceFactory off(String value) {colorOff=value; return this;}
	
	
	
	public static OfxMatrixDeviceFactory instance() {return new OfxMatrixDeviceFactory();}
	public OfxMatrixDeviceFactory()
	{
		map = new HashMap<>();
		colorOff = "";
	}
	
	public Table build(JsonMatrixDevice device)
	{
		map.clear();
		
		Table table = new Table();
		table.setTitle(XmlTitleFactory.build(device.getName()+" ("+device.getRows()+"x"+device.getColumns()+")"));
		
		Content content = new Content();
		content.setHead(buildHead(device));
		content.getBody().add(buildBody(device));
		table.setContent(content);
		return table;
	}
		
	private static Head buildHead(JsonMatrixDevice device)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell(""));
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
		AtomicInteger index = new AtomicInteger();
		for(List<String> partition : partitions)
		{
			body.getRow().add(buildRow(index.incrementAndGet(),partition));
		}
		return body;
	}
	
	private Row buildRow(int index, List<String> datas)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell(index));
		for(String cell : datas)
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(toCode(cell)));
		}
		return row;
	}
	
	private String toCode(String colour)
	{
		if(ObjectUtils.isEmpty(colour)) {return "";}
		if(colour.equals(colorOff)) {return "";}
		if(!map.containsKey(colour))
		{
			String code = XlsColumnFactory.index2code(map.size());
			map.put(colour,code);
		}
		return map.get(colour);
	}
}