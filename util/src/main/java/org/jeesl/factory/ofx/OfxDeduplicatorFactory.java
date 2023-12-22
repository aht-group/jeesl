package org.jeesl.factory.ofx;

import java.util.List;

import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.processor.JeeslDeduplicator;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlHeadFactory;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Table;

public class OfxDeduplicatorFactory<RE extends JeeslRevisionEntity<?,?,?,?,?,?>, ICON extends JeeslStatus<?,?,ICON>, EJB extends EjbWithId>
{
	public static <RE extends JeeslRevisionEntity<?,?,?,?,?,?>, ICON extends JeeslStatus<?,?,ICON>, EJB extends EjbWithId> OfxDeduplicatorFactory<RE,ICON,EJB> instance()
	{
		return new OfxDeduplicatorFactory<>();
	}
	
	public Table toOfx(String localeCode, JeeslDeduplicator<RE,ICON,EJB> deduplicator)
	{
		Table table = new Table();
		table.setTitle(XmlTitleFactory.build(deduplicator.getClass().getSimpleName()));
		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Class"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Entity"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Original: "+deduplicator.getEjbOriginal().getId()));
		row.getCell().add(XmlCellFactory.createParagraphCell("Duplicate: "+deduplicator.getEjbDuplicate().getId()));
		row.getCell().add(XmlCellFactory.createParagraphCell("Action"));

		Content content = new Content();
		content.setHead(XmlHeadFactory.build(row));
		content.getBody().add(buildBody(localeCode,deduplicator));
		table.setContent(content);
//		JaxbUtil.info(table);
		return table;
	}
	
	private Body buildBody(String localeCode, JeeslDeduplicator<RE,ICON,EJB> deduplicator)
	{
		Body body = new Body();
		
		for(RE entity : deduplicator.getEntities())
		{
			body.getRow().add(buildRow(localeCode,deduplicator,entity));
		}
		return body;
	}
	
	private Row buildRow(String localeCode, JeeslDeduplicator<RE,ICON,EJB> deduplicator, RE entity)
	{		
		Row row = new Row();
		if(entity.getJscn()==null || entity.getJscn().isEmpty()){row.getCell().add(XmlCellFactory.createParagraphCell("--"));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell(entity.getJscn()));}
		row.getCell().add(XmlCellFactory.createParagraphCell(entity.getName().get(localeCode).getLang()));
		
		if(deduplicator.getMapOriginal().containsKey(entity)) {row.getCell().add(XmlCellFactory.createParagraphCell(""+deduplicator.getMapOriginal().get(entity)));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell("--"));}
		
		if(deduplicator.getMapDuplicate().containsKey(entity)) {row.getCell().add(XmlCellFactory.createParagraphCell(""+deduplicator.getMapDuplicate().get(entity)));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell("--"));}
		
		if(deduplicator.getMapAction().containsKey(entity))
		{
			List<String> codes = TxtStatusFactory.toCodes(deduplicator.getMapAction().get(entity));
			row.getCell().add(XmlCellFactory.createParagraphCell(String.join(", ", codes)));
		}
		else {row.getCell().add(XmlCellFactory.createParagraphCell("--"));}
		return row;
	}
}