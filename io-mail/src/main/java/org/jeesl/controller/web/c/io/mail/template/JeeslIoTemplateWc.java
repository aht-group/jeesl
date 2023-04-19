package org.jeesl.controller.web.c.io.mail.template;

import org.jeesl.controller.web.io.mail.JeeslIoTemplateController;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.mail.template.IoTemplate;
import org.jeesl.model.ejb.io.mail.template.IoTemplateCategory;
import org.jeesl.model.ejb.io.mail.template.IoTemplateChannel;
import org.jeesl.model.ejb.io.mail.template.IoTemplateDefinition;
import org.jeesl.model.ejb.io.mail.template.IoTemplateScope;
import org.jeesl.model.ejb.io.mail.template.IoTemplateToken;
import org.jeesl.model.ejb.io.mail.template.IoTemplateTokenType;

public class JeeslIoTemplateWc extends JeeslIoTemplateController<IoLang,IoDescription,IoLocale,IoTemplateCategory,IoTemplateChannel,IoTemplate,IoTemplateScope,IoTemplateDefinition,IoTemplateToken,IoTemplateTokenType>
{
	private static final long serialVersionUID = 1L;

	public JeeslIoTemplateWc(IoTemplateFactoryBuilder<IoLang,IoDescription,IoTemplateCategory,IoTemplateChannel,IoTemplate,IoTemplateScope,IoTemplateDefinition,IoTemplateToken,IoTemplateTokenType> fbTemplate)
	{
		super(fbTemplate);
	}
}