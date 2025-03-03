package org.jeesl.doc.er;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.interfaces.model.marker.qualifier.EjbErAttributes;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.admin.er.OfxClassAttributesTableFactory;
import net.sf.exlp.util.io.ClassUtil;
import net.sf.exlp.util.io.dir.RecursiveFileFinder;

public class ErAttributesProcessor
{
	final static Logger logger = LoggerFactory.getLogger(ErAttributesProcessor.class);
	
	private File fBase;
	private OfxClassAttributesTableFactory ofAttributes;
	private OfxMultiLangLatexWriter ofxWriter;
	
	private final String[] langs = {"en"};
	
	public ErAttributesProcessor(OfxMultiLangLatexWriter ofxWriter, Configuration config, File fBase)
	{
		this.fBase=fBase;
		this.ofxWriter=ofxWriter;
		ofAttributes = new OfxClassAttributesTableFactory(config,langs,null);
	}

	public void addPackages(String sEjbPackage) throws IOException, ClassNotFoundException
	{		
		File fPackage = new File(fBase,sEjbPackage);
		RecursiveFileFinder finder = new RecursiveFileFinder(FileFilterUtils.suffixFileFilter(".java"));
    	List<File> list = finder.find(fPackage);
		for(File f : list)
		{
			logger.info(f.getAbsolutePath());
			Class<?> c = ClassUtil.forFile(fBase, f);
			cl(c);
		}
	}
	
	private void cl(Class<?> c)
	{
		Annotation aClass = c.getAnnotation(EjbErAttributes.class);
		Annotation aNode = c.getAnnotation(EjbErNode.class);
		if(aClass!=null && aNode!=null)
		{
			Table table = ofAttributes.table(c);
			JaxbUtil.trace(table);
			try
			{
				ofxWriter.table("admin/development/er/"+c.getName(), table);
			}
			catch (OfxAuthoringException e) {e.printStackTrace();}
			catch (IOException e) {e.printStackTrace();}
		}
	}
}