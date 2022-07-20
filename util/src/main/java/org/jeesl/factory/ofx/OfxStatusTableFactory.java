package org.jeesl.factory.ofx;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
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

public class OfxStatusTableFactory <L extends JeeslLang, D extends JeeslDescription,
								S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(OfxStatusTableFactory.class);
	
	private final String localeCode;
	private final Class<S> c;
	
	public static <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, E extends Enum<E>>
					OfxStatusTableFactory<L,D,S> instance(Class<S> c, Enum<E> e)
	{
						return new OfxStatusTableFactory<>(e.toString(),c);
	}
	public OfxStatusTableFactory(String localeCode, Class<S> c)
	{
		this.localeCode=localeCode;
		this.c=c;
	}
	
	public Table build(JeeslFacade facade)
	{
		Table table = new Table();
		table.setTitle(XmlTitleFactory.build(c.getSimpleName()));
		
		Content content = new Content();
		content.setHead(buildHead());
		content.getBody().add(buildBody(facade.allOrderedPosition(c)));
		table.setContent(content);
		return table;
	}
		
	private static Head buildHead()
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("ID"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Position"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Code"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Label"));
		return XmlHeadFactory.build(row);
	}
	
	private Body buildBody(List<S> list)
	{
		Body body = new Body();
		for(S row : list)
		{
			body.getRow().add(buildRow(row));
		}
		return body;
	}
	
	private Row buildRow(S ejb)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell(ejb.getId()));
		row.getCell().add(XmlCellFactory.createParagraphCell(ejb.getPosition()));
		row.getCell().add(XmlCellFactory.createParagraphCell(ejb.getCode()));
		row.getCell().add(XmlCellFactory.createParagraphCell(ejb.getName().get(localeCode).getLang()));
		return row;
	}
}