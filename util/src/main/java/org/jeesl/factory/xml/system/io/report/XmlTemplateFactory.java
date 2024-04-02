package org.jeesl.factory.xml.system.io.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.report.Template;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportCellComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateFactory <L extends JeeslLang,D extends JeeslDescription,
								
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplateFactory.class);
	
	private Template q;
	
	private Comparator<CELL> comparatorCell;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlCellFactory<L,D,CELL> xfCell;
	
	public XmlTemplateFactory(Template q){this(null,q);}
	public XmlTemplateFactory(String localeCode, Template q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(ObjectUtils.isNotEmpty(q.getCell()))
		{
			comparatorCell = new IoReportCellComparator<TEMPLATE,CELL>().factory(IoReportCellComparator.Type.position);
			xfCell = new XmlCellFactory<>(q.getCell().get(0));
		}
	}
	
	public Template build(TEMPLATE template)
	{
		Template xml = XmlTemplateFactory.build();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(template.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(template.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(template.getPosition());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(template.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(template.getDescription()));}
		
		if(ObjectUtils.isNotEmpty(q.getCell()))
		{
			Collections.sort(template.getCells(),comparatorCell);
			for(CELL cell : template.getCells())
			{
				xml.getCell().add(xfCell.build(cell));
			}
		}
						
		return xml;
	}
	
	public static Template build(String code)
	{
		Template xml = build();
		xml.setCode(code);
		return xml;
	}
	public static Template build()
	{
		Template xml = new Template();
		return xml;
	}
}