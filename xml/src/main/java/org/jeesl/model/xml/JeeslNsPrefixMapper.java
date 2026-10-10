package org.jeesl.model.xml;

import org.exlp.interfaces.io.NsPrefixMapperInterface;
import org.jdom2.Namespace;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class JeeslNsPrefixMapper extends NamespacePrefixMapper implements NsPrefixMapperInterface
{
	public static Namespace nsReport = Namespace.getNamespace("r","http://ahtutils.aht-group.com/report");
	
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix)
    {
        if("http://ahtutils.aht-group.com".equals(namespaceUri) ){return "aht";}
        if("http://ahtutils.aht-group.com/status".equals(namespaceUri) ){return "js";}
        if("http://ahtutils.aht-group.com/symbol".equals(namespaceUri) ){return "sym";}
        if("http://ahtutils.aht-group.com/report".equals(namespaceUri) ){return "r";}
        if("http://ahtutils.aht-group.com/monitoring".equals(namespaceUri) ){return "mo";}
        if("http://ahtutils.aht-group.com/navigation".equals(namespaceUri) ){return "nav";}
        if("http://ahtutils.aht-group.com/access".equals(namespaceUri) ){return "acl";}
        if("http://ahtutils.aht-group.com/security".equals(namespaceUri) ){return "sec";}
        if("http://ahtutils.aht-group.com/project".equals(namespaceUri) ){return "p";}
        if("http://ahtutils.aht-group.com/dbseed".equals(namespaceUri) ){return "db";}
        if("http://ahtutils.aht-group.com/mail".equals(namespaceUri) ){return "m";}
        if("http://ahtutils.aht-group.com/issue".equals(namespaceUri) ){return "it";}
       
        if("http://www.jeesl.org/survey".equals(namespaceUri) ){return "survey";}
        if("http://ahtutils.aht-group.com/sync".equals(namespaceUri) ){return "sync";}
        if("http://ahtutils.aht-group.com/system".equals(namespaceUri) ){return "sys";}
        if("http://ahtutils.aht-group.com/text".equals(namespaceUri) ){return "t";}
        if("http://ahtutils.aht-group.com/qa".equals(namespaceUri) ){return "qa";}
        if("http://ahtutils.aht-group.com/utils".equals(namespaceUri) ){return "u";}
        if("http://ahtutils.aht-group.com/audit".equals(namespaceUri) ){return "at";}
        if("http://ahtutils.aht-group.com/cloud/facebook".equals(namespaceUri) ){return "fb";}
        
        if("http://www.jeesl.org".equals(namespaceUri) ){return "jeesl";}
        if("http://www.jeesl.org/revision".equals(namespaceUri) ){return "rev";}
        if("http://www.jeesl.org/text".equals(namespaceUri) ){return "text";}
        if("http://www.jeesl.org/symbol".equals(namespaceUri) ){return "symbol";}
        if("http://www.jeesl.org/finance".equals(namespaceUri) ){return "jf";}
        if("http://www.jeesl.org/io/template".equals(namespaceUri) ){return "template";}
    	if("http://www.jeesl.org/io/mail".equals(namespaceUri) ){return "mail";}
    	if("http://www.jeesl.org/timeseries".equals(namespaceUri) ){return "ts";}
    	if("http://www.jeesl.org/workflow".equals(namespaceUri) ){return "wf";}
    	if("http://www.jeesl.org/job".equals(namespaceUri) ){return "job";}
    	if("http://www.jeesl.org/calendar".equals(namespaceUri) ){return "cal";}
    	if("http://www.jeesl.org/dev/srs".equals(namespaceUri) ){return "srs";}
    	
    	if("http://www.jeesl.org/inventory".equals(namespaceUri) ){return "ipc";}
    	if("http://www.jeesl.org/inventory/pc".equals(namespaceUri) ){return "i";}
        
        if("http://www.openfuxml.org/list".equals(namespaceUri) ){return "ofxL";}
        if("http://www.openfuxml.org".equals(namespaceUri) ){return "ofx";}
        if("http://www.openfuxml.org/chart".equals(namespaceUri) ){return "chart";}    
        
        if("http://exlp.sf.net/io".equals(namespaceUri) ){return "io";}
        if("http://exlp.sf.net/identity".equals(namespaceUri) ){return "id";}
       
        return suggestion;
    }

    public String[] getPreDeclaredNamespaceUris()
    {
    	String[] result = new String[3];
    	result[2] = "http://www.openfuxml.org/chart";
    	result = new String[0];
        return result;
    }
}