package org.jeesl.factory.xml.system.io.ssi.docker;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.json.io.ssi.docker.JsonDockerContainer;
import org.jeesl.model.xml.io.ssi.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlContainerFactory.class);
	
	public static Container build() {return new Container();}
	
	public static Container build(String code, String status)
	{
		Container xml = build();
		xml.setCode(code);
		xml.setStatus(status);
		return xml;
	}
	
	public static List<Container> build(List<JsonDockerContainer> list)
	{
		List<Container> xml = new ArrayList<>();
		for(JsonDockerContainer json : list) {xml.add(build(json));}
		return xml;
	}
	
	public static Container build(JsonDockerContainer json)
	{
		Container xml = build();
		xml.setCode(json.getNames()[0]);
		xml.setStatus(json.getState());
		return xml;
	}
}