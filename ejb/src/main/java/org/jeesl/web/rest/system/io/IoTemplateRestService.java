package org.jeesl.web.rest.system.io;

import org.jeesl.api.facade.io.JeeslIoTemplateFacade;
import org.jeesl.api.rest.rs.io.template.JeeslIoTemplateRestExport;
import org.jeesl.api.rest.rs.io.template.JeeslIoTemplateRestImport;
import org.jeesl.factory.xml.system.io.template.XmlTemplatesFactory;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.template.Templates;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.sync.DataUpdate;

public class IoTemplateRestService <L extends JeeslLang,D extends JeeslDescription,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
									TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
									SCOPE extends JeeslStatus<L,D,SCOPE>,
									DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
									TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
									TOKENTYPE extends JeeslStatus<L,D,TOKENTYPE>>
		extends AbstractJeeslRestHandler<L,D>
		implements JeeslIoTemplateRestExport,JeeslIoTemplateRestImport
{
	final static Logger logger = LoggerFactory.getLogger(IoTemplateRestService.class);
	
	private JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate;
	
	private final Class<CATEGORY> cCategory;
	private final Class<CHANNEL> cType;
	@SuppressWarnings("unused") private final Class<TEMPLATE> cTemplate;
	private final Class<SCOPE> cScope;
	@SuppressWarnings("unused") private final Class<DEFINITION> cDefinition;
	@SuppressWarnings("unused") private final Class<TOKEN> cToken;

//	private XmlTemplateFactory<L,D,CATEGORY,TYPE,TEMPLATE,SCOPE,DEFINITION,TOKEN> xfTemplate;
	
	private IoTemplateRestService(JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate,final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<CHANNEL> cType, final Class<TEMPLATE> cTemplate, final Class<SCOPE> cScope, final Class<DEFINITION> cDefinition, final Class<TOKEN> cToken)
	{
		super(fTemplate,cL,cD);
		this.fTemplate=fTemplate;
		
		this.cCategory=cCategory;
		this.cType=cType;
		this.cTemplate=cTemplate;
		this.cScope=cScope;
		this.cDefinition=cDefinition;
		this.cToken=cToken;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					CATEGORY extends JeeslStatus<L,D,CATEGORY>,
					CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
					TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
					SCOPE extends JeeslStatus<L,D,SCOPE>,
					DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
					TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
					TOKENTYPE extends JeeslStatus<L,D,TOKENTYPE>>
		IoTemplateRestService<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE>
		factory(JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate,final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<CHANNEL> cType, final Class<TEMPLATE> cTemplate, final Class<SCOPE> cScope, final Class<DEFINITION> cDefinition, final Class<TOKEN> cToken)
	{
		return new IoTemplateRestService<>(fTemplate,cL,cD,cCategory,cType,cTemplate,cScope,cDefinition,cToken);
	}
	
	@Override
	public Templates exportSystemIoTemplates()
	{
		Templates xml = XmlTemplatesFactory.build();
		
		return xml;
	}
	
	@Override public Container exportSystemIoTemplateCategories() {return xfContainer.build(fTemplate.allOrderedPosition(cCategory));}
	@Override public Container exportSystemIoTemplateTypes() {return xfContainer.build(fTemplate.allOrderedPosition(cType));}
	@Override public Container exportSystemIoTemplateScopes() {return xfContainer.build(fTemplate.allOrderedPosition(cScope));}
	
	
	@Override public DataUpdate importSystemIoTemplateCategories(org.jeesl.model.xml.xsd.Container categories){return importStatus(cCategory,categories,null);}
	@Override public DataUpdate importSystemIoTemplateTypes(org.jeesl.model.xml.xsd.Container types){return importStatus(cType,types,null);}
	@Override public DataUpdate importSystemIoTemplateScopes(Container scopes){return importStatus(cScope,scopes,cCategory);}
}