package org.jeesl.controller.web.c.module.cl;

import org.jeesl.controller.web.g.module.cl.JeeslClTracklistGwc;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.cl.ClChecklist;
import org.jeesl.model.ejb.module.cl.ClChecklistItem;
import org.jeesl.model.ejb.module.cl.ClTopic;
import org.jeesl.model.ejb.module.cl.ClTracklist;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslClTracklistWc <RREF extends EjbWithId>
					extends JeeslClTracklistGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,ClChecklist,ClTopic,ClChecklistItem,ClTracklist>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslClTracklistWc(ChecklistFactoryBuilder<IoLang,IoDescription,TenantRealm,ClChecklist,ClTopic,ClChecklistItem,ClTracklist> fbCl)
	{
		super(fbCl);	
	}
}