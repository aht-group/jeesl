package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.report.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCellFactory <L extends JeeslLang,D extends JeeslDescription,
								CELL extends JeeslReportCell<L,D,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCellFactory.class);
	
	private Cell q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlCellFactory(Cell q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Cell build(CELL cell)
	{
		Cell xml = new Cell();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(cell.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(cell.isVisible());}
		if(Objects.nonNull(q.getColNr())) {xml.setColNr(cell.getColNr());}
		if(Objects.nonNull(q.getRowNr())) {xml.setRowNr(cell.getRowNr());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(cell.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(cell.getDescription()));}
						
		return xml;
	}
}