package org.jeesl.controller.mail.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.interfaces.model.system.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.system.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.system.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.system.io.mail.template.JeeslTemplateChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.InvalidReferenceException;
import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import net.sf.ahtutils.exception.processing.UtilsProcessingException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class FreemarkerIoTemplateEngine<L extends UtilsLang,D extends UtilsDescription,
										CATEGORY extends UtilsStatus<CATEGORY,L,D>,
										CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
										TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
										SCOPE extends UtilsStatus<SCOPE,L,D>,
										DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
										TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
										TOKENTYPE extends UtilsStatus<TOKENTYPE,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(FreemarkerIoTemplateEngine.class);

	private IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate;
	
	private StringTemplateLoader fmStringTemplates;
	private Configuration fmConfiguration;
	
	protected final Map<String,TEMPLATE> mapTemplate;
	
	public FreemarkerIoTemplateEngine(IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate)
	{
		this.fbTemplate=fbTemplate;
		fmConfiguration = new Configuration(Configuration.getVersion());
		fmStringTemplates = new StringTemplateLoader();
		fmConfiguration.setTemplateLoader(fmStringTemplates);
		fmConfiguration.setLogTemplateExceptions(false);
		fmConfiguration.setDefaultEncoding("UTF-8");
		fmConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		mapTemplate = new HashMap<String,TEMPLATE>();
	}
	
	public void addTemplates(List<TEMPLATE> templates)
	{
		for(TEMPLATE template : templates)
		{
			addTemplate(template);
		}
		mapTemplate.putAll(fbTemplate.ejbTemplate().buildMap(templates));
	}
		
	public void addTemplate(TEMPLATE template)
	{
		for(DEFINITION definition : template.getDefinitions())
		{
			addTemplate(definition);
		}
	}
	
	public void addTemplate(DEFINITION definition)
	{
		for(String localeCode : definition.getDescription().keySet())
		{
			D description = definition.getDescription().get(localeCode);
			String code = fbTemplate.txtTemplate().buildCode(definition.getTemplate(),definition,localeCode);
			fmStringTemplates.removeTemplate(code);
			fmStringTemplates.putTemplate(code,description.getLang());
		}
	}
	
	public TEMPLATE resolveTemplate(String templateCode) throws UtilsProcessingException
	{
		if(!mapTemplate.containsKey(templateCode)){throw new UtilsProcessingException("Template with code "+templateCode+" not available");}
		return mapTemplate.get(templateCode);
	}
	
	public String process(String code, Map<String,Object> model) throws IOException, TemplateException, InvalidReferenceException
	{
		Template ftl = fmConfiguration.getTemplate(code);
		StringWriter sw = new StringWriter();
		ftl.process(model, sw);
		sw.flush();
		return sw.toString();
	}
	
	public String process(String code, Element root) throws IOException, TemplateException, InvalidReferenceException
	{
		NodeModel.simplify(root);
		
		Template ftl = fmConfiguration.getTemplate(code);
		StringWriter sw = new StringWriter();
		ftl.process(NodeModel.wrap(root), sw);
		
		sw.flush();
		return sw.toString();
	}
}