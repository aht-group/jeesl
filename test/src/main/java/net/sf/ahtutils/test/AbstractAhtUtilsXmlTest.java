package net.sf.ahtutils.test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.exlp.test.AbstractXmlTest;
import org.exlp.util.jx.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JEESL variant of the shared EXLP base class {@link org.exlp.test.AbstractXmlTest}.
 *
 * <p>The XML test logic is provided by EXLP: the reference file lookup below
 * {@code src/test/resources}, the default XML date, {@code saveReferenceXml} and the
 * {@code xml()} test. This class only adds the JEESL reference directory
 * {@code src/test/resources/data/xml} and the legacy helpers that are still referenced by the
 * JEESL XML tests.</p>
 *
 * @deprecated use {@link org.exlp.test.AbstractXmlTest} together with the JEESL prefix directly.
 */
@Deprecated
public class AbstractAhtUtilsXmlTest <T extends Object> extends AbstractXmlTest<T>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtUtilsXmlTest.class);

	/** Directory below {@code src/test/resources} that holds the JEESL reference XML files. */
	protected static final Path XML_DIR = Paths.get("data","xml");

	protected static File fXml;

	public AbstractAhtUtilsXmlTest(){this(null,null);}
	public AbstractAhtUtilsXmlTest(Class<T> cXml,String xmlDirSuffix)
	{
		super(cXml,XML_DIR,(xmlDirSuffix==null ? null : Paths.get(xmlDirSuffix)));
	}

	protected void save(Object xml, File f){save(xml,f,true);}
	protected void save(Object xml, File f, boolean formatted)
	{
		logger.debug("Saving Reference XML to "+f.getAbsolutePath());
		JaxbUtil.info(xml);
		JaxbUtil.save(f, xml, true);
	}
}
