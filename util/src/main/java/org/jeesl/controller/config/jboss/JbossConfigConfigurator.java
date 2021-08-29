package org.jeesl.controller.config.jboss;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.controller.config.jboss.JbossModuleConfigurator.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JDomUtil;

public class JbossConfigConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(JbossConfigConfigurator.class);
	
	private static final String srcBaseDir = "listing.aht-utils/admin/installation/jboss/config";
	
	private MultiResourceLoader mrl;
	
	private File jbossBaseDir;
	
	private Document doc;
	
	private JbossConfigConfigurator(String jbossDir)
	{
		jbossBaseDir = new File(jbossDir);
		File fStandalone = new File(jbossBaseDir,"standalone/configuration/standalone.xml");
		doc = JDomUtil.load(fStandalone);
	}
	
	private JbossConfigConfigurator(Product product, String version,String jbossDir)
	{
		jbossBaseDir = new File(jbossDir);
		mrl = new MultiResourceLoader();
		
		String reference = srcBaseDir+"/"+product+"/"+version+"/standalone.xml";
		
		if(mrl.isAvailable(reference))
		{
			try {doc = JDomUtil.load(mrl.searchIs(reference));}
			catch (FileNotFoundException e) {e.printStackTrace();}
		}
		else
		{
			try
			{
				String local = "../doc/src/main/resources/"+reference;
				doc = JDomUtil.load(mrl.searchIs(local));
			}
			catch (FileNotFoundException e){logger.warn("You need to set the base-dir to doc in IntelliJ");e.printStackTrace();}
		}	
	}
	
	@Deprecated
	public void addDs(Element element)
	{
        XPathExpression<Element> xpee = XPathFactory.instance().compile("/ns1:server/ns1:profile/ns3:subsystem/ns3:datasources", Filters.element(), null, getNamespaceList());
        Element datasources = xpee.evaluateFirst(doc);
        JDomUtil.setNameSpaceRecursive(element, datasources.getNamespace());
        datasources.addContent(0, element);
    }
	
	@Deprecated
	public void addDbDriver(Element element)
	{
        XPathExpression<Element> xpee = XPathFactory.instance().compile("/ns1:server/ns1:profile/ns3:subsystem/ns3:datasources/ns3:drivers", Filters.element(), null, getNamespaceList());
        Element drivers = xpee.evaluateFirst(doc);
        JDomUtil.setNameSpaceRecursive(element, drivers.getNamespace());
        drivers.addContent(element);
	}
	
	public void mergeDbDriver(Element element)
	{
        XPathExpression<Element> xpDrivers = XPathFactory.instance().compile("/ns1:server/ns1:profile/ns3:subsystem/ns3:datasources/ns3:drivers", Filters.element(), null, getNamespaceList());
        Element drivers = xpDrivers.evaluateFirst(doc);
        
        XPathExpression<Element> xpDriver = XPathFactory.instance().compile("ns3:*[@name='"+element.getAttributeValue("name")+"']", Filters.element(), null, getNamespaceList());
        
        Element driver = xpDriver.evaluateFirst(drivers);
        if(driver!=null){driver.detach();}
        JDomUtil.setNameSpaceRecursive(element, drivers.getNamespace());
        drivers.addContent(element);
	}
	
	public void mergeDbDatasource(Element element)
	{
        XPathExpression<Element> xpDatasources = XPathFactory.instance().compile("/ns1:server/ns1:profile/ns3:subsystem/ns3:datasources", Filters.element(), null, getNamespaceList());
        Element datasources = xpDatasources.evaluateFirst(doc);
        
        XPathExpression<Element> xpDatasource = XPathFactory.instance().compile("ns3:*[@pool-name='"+element.getAttributeValue("pool-name")+"']", Filters.element(), null, getNamespaceList());
        
        Element ds = xpDatasource.evaluateFirst(datasources);
        if(ds!=null){ds.detach();}
        JDomUtil.setNameSpaceRecursive(element, datasources.getNamespace());
        datasources.addContent(element);
	}
	
	public void changePublicInterface()
	{
        Element interfacePublic = new Element("any-address");
        XPathExpression<Element> xpe = XPathFactory.instance().compile("/ns1:server/ns1:interfaces", Filters.element(), null,getNamespaceList());
        Element ele = xpe.evaluateFirst(doc);
        for(Element e : ele.getChildren())
        {
            if (e.getAttribute("name").getValue().equals("public"))
            {
                e.getChildren().clear();
                e.addContent(interfacePublic);
                e.getChildren().get(0).setNamespace(e.getNamespace());
            }
        }
    }

    private List<Namespace> getNamespaceList()
    {
        Iterator<Content> desc = doc.getRootElement().getDescendants();
        List<Namespace> tmp = new ArrayList<Namespace>(doc.getRootElement().getNamespacesIntroduced());
        while(desc.hasNext())
        {
            tmp.addAll(desc.next().getNamespacesIntroduced());
        }
        List<Namespace> ns = new ArrayList<Namespace>();
        int i = 1;
        for(Namespace namespace : tmp)
        {
            ns.add(Namespace.getNamespace("ns" + i, namespace.getURI()));
            i++;
        }
        return ns;
    }
    
    public Document getDocument(){return doc;}

    public void changeTimeout(int second)
    {
        XPathExpression<Element> xpee = XPathFactory.instance().compile("/ns1:server/ns1:profile/ns22:subsystem/ns22:coordinator-environment", Filters.element(), null, getNamespaceList());
        Element coordinator_environment = xpee.evaluateFirst(doc);
        coordinator_environment.setAttribute("default-timeout", Integer.toString(second));
    }
    
	public void write()
	{
		File f = new File(jbossBaseDir,"/standalone/configuration/standalone.xml");
		JDomUtil.save(doc, f, Format.getPrettyFormat());
		logger.info("Writing to "+f.getAbsolutePath());
	}
}