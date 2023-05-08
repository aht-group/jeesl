package org.jeesl.controllerl.web.c.module.cl;

import org.jeesl.controllerl.web.g.module.cl.JeeslClChecklistGwc;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.cl.ClChecklist;
import org.jeesl.model.ejb.module.cl.ClChecklistItem;
import org.jeesl.model.ejb.module.cl.ClTopic;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslClChecklistWc <RREF extends EjbWithId>
					extends JeeslClChecklistGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,ClChecklist,ClTopic,ClChecklistItem>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslClChecklistWc(ChecklistFactoryBuilder<IoLang,IoDescription,TenantRealm,ClChecklist,ClTopic,ClChecklistItem> fbCl)
	{
		super(fbCl);	
	}
}