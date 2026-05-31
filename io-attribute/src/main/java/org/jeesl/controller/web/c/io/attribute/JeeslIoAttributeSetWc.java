package org.jeesl.controller.web.c.io.attribute;

import org.jeesl.controller.web.io.attribute.JeeslIoAttributeSetGwc;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.attribute.IoAttributeCategory;
import org.jeesl.model.ejb.io.attribute.IoAttributeCriteria;
import org.jeesl.model.ejb.io.attribute.IoAttributeItem;
import org.jeesl.model.ejb.io.attribute.IoAttributeSection;
import org.jeesl.model.ejb.io.attribute.IoAttributeSet;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslIoAttributeSetWc <RREF extends EjbWithId>
					extends JeeslIoAttributeSetGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,IoAttributeCategory,IoAttributeCriteria,IoAttributeSet,IoAttributeSection,IoAttributeItem>
{
	private static final long serialVersionUID = 1L;

	public JeeslIoAttributeSetWc(IoAttributeFactoryBuilder<IoLang,IoDescription,TenantRealm,IoAttributeCategory,IoAttributeCriteria,?,?,IoAttributeSet,IoAttributeSection,IoAttributeItem,?,?> fbAttribute)
	{
		super(fbAttribute);
	}
}