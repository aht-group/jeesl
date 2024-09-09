package org.jeesl.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FilenameUtils;
import org.exlp.util.jx.JaxbUtil;
import org.exlp.util.system.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslXmlTest<T extends Object>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslXmlTest.class);

	protected static File fXml;
	
	private File xmlFile;
	
	private Class<T> cJaxb;
	
	private AbstractJeeslXmlTest(Class<T> cXml, Path pPrefix, String xmlDirSuffix)
	{
		this.cJaxb=cXml;
		if(Objects.nonNull(cXml))
		{
			try
			{
				T t = cXml.getDeclaredConstructor().newInstance();
				xmlFile = new File(getXmlDir(pPrefix,xmlDirSuffix),t.getClass().getSimpleName()+".xml");
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
			catch (IllegalArgumentException e) {e.printStackTrace();}
			catch (InvocationTargetException e) {e.printStackTrace();}
			catch (NoSuchMethodException e) {e.printStackTrace();}
			catch (SecurityException e) {e.printStackTrace();}
		}
	}
	
	private File getXmlDir(Path pPrefix, String suffix)
    {
        File f = new File(".");
        String s = FilenameUtils.normalizeNoEndSeparator(f.getAbsolutePath());

        Path path = Paths.get(s, "src","test","resources");
        path = path.resolve(pPrefix);
        if(Objects.nonNull(suffix)) {path = path.resolve(Paths.get(suffix));}
        
        logger.info("Path: "+path.toString());
        
        f = new File(s);
        return new File(f,"src"+File.separator+"test"+File.separator+"resources"+File.separator+pPrefix.toString()+File.separator+suffix);
    }
	
	protected static XMLGregorianCalendar getDefaultXmlDate()
	{
		LocalDateTime ldt = LocalDateTime.of(2011,11,11,11,11,11);
		return DateUtil.toXmlGc(ldt);
	}
	
	protected void saveReferenceXml()
	{
		Object xml = this.build(true);
		logger.debug("Saving Reference XML");
    	JaxbUtil.save(xmlFile,xml,true);
	}
	
    @Test
    public void xml() throws FileNotFoundException
    {
    	T actual = build(true);
    	T expected = JaxbUtil.loadJAXB(xmlFile.getAbsolutePath(), cJaxb);
    	Assert.assertEquals("Actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
    }
    
    //TODO declare as abstract
    protected T build(boolean withChilds){return null;}
}