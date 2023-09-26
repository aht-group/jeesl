package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateDefinitionFactory;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateFactory;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateTokenFactory;
import org.jeesl.factory.ftl.FtlIoTemplateTokenFactory;
import org.jeesl.factory.txt.system.io.mail.template.TxtIoTemplateFactory;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoTemplateFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
									TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
									SCOPE extends JeeslStatus<L,D,SCOPE>,
									DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
									TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
									TOKENTYPE extends JeeslTemplateTokenType<L,D,TOKENTYPE,?>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoTemplateFactoryBuilder.class);
	
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory(){return cCategory;}
	private final Class<CHANNEL> cType; public Class<CHANNEL> getClassType(){return cType;}
	private final Class<TEMPLATE> cTemplate; public Class<TEMPLATE> getClassTemplate(){return cTemplate;}
	private final Class<SCOPE> cScope; public Class<SCOPE> getClassScope(){return cScope;}
	private final Class<DEFINITION> cDefinition; public Class<DEFINITION> getClassDefinition(){return cDefinition;}
	private final Class<TOKEN> cToken; public Class<TOKEN> getClassToken(){return cToken;}
	private final Class<TOKENTYPE> cTokenType; public Class<TOKENTYPE> getClassTokenType(){return cTokenType;}
	
	public IoTemplateFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<CATEGORY> cCategory,
									final Class<CHANNEL> cType,
									final Class<TEMPLATE> cTemplate,
									final Class<SCOPE> cScope,
									final Class<DEFINITION> cDefinition,
									final Class<TOKEN> cToken,
									final Class<TOKENTYPE> cTokenType)
	{
		super(cL,cD);
		this.cCategory=cCategory;
		this.cType=cType;
		this.cTemplate=cTemplate;
		this.cScope=cScope;
		this.cDefinition=cDefinition;
		this.cToken=cToken;
		this.cTokenType=cTokenType;
	}
	
	public EjbIoTemplateFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> ejbTemplate() {return new EjbIoTemplateFactory<>(cL,cD,cTemplate);}
	public EjbIoTemplateDefinitionFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> ejbDefinition() {return new EjbIoTemplateDefinitionFactory<>(cD,cDefinition);}
	public EjbIoTemplateTokenFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> ejbTtoken() {return new EjbIoTemplateTokenFactory<>(cL,cD,cToken);}
	public FtlIoTemplateTokenFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> txtToken() {return new FtlIoTemplateTokenFactory<>();}
	public TxtIoTemplateFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> txtTemplate() {return new TxtIoTemplateFactory<>();}
}