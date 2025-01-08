package org.jeesl.factory.xml.module.bb;

import java.util.List;

import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.model.xml.module.bb.Message;
import org.jeesl.model.xml.module.bb.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMessagesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMessagesFactory.class);
	
	public static Messages build() {return new Messages();}
	
	
	public static Messages build(List<Message> list) {Messages xml = build(); xml.getMessage().addAll(list);return xml;}
	public static Messages build(String type, Message msg) {Messages xml = build(); xml.setType(XmlTypeFactory.create(type));xml.getMessage().add(msg);return xml;}
	public static Messages build(String type, List<Message> list) {Messages xml = build(); xml.setType(XmlTypeFactory.create(type));xml.getMessage().addAll(list);return xml;}
}