package net.sf.ahtutils.doc;

import java.io.FileNotFoundException;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.exlp.util.jx.JaxbUtil;
import org.openfuxml.factory.xml.list.XmlListFactory2;
import org.openfuxml.factory.xml.list.XmlListItemFactory;
import org.openfuxml.model.xml.core.list.List;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.filter.OfxLangFilter;
import org.openfuxml.util.query.XmlSectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxInstallationRequirementFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxInstallationRequirementFactory.class);

	
	private static String idHardware ="installation.requirement.hardware";
    private OfxLangFilter multiLangFilter;

    private Section section;

	public OfxInstallationRequirementFactory(String lang)
	{
        multiLangFilter = new OfxLangFilter(lang);

        try
        {
            section = JaxbUtil.loadJAXB("ofx.aht-utils/installation/requirements.xml", Section.class);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
	}

    public Section hardware(int ram)
    {
        section = multiLangFilter.filterLang(section);
        
        try
        {
			Section sHardware = XmlSectionQuery.getRenderer(section, idHardware);
			
			List list = XmlListFactory2.build(XmlListFactory2.Ordering.unordered.toString());
			list.getItem().add(XmlListItemFactory.build(ram+" GB RAM"));
			
			sHardware.getContent().add(list);
			
			JaxbUtil.info(sHardware);
		}
        catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
        catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
        
        
        return section;
    }
}