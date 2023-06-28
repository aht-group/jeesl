package org.jeesl.controller.web.c.module.cl;

import org.jeesl.controller.web.g.module.cl.JeeslClTracklistGwc;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.cl.ClCheckItem;
import org.jeesl.model.ejb.module.cl.ClCheckList;
import org.jeesl.model.ejb.module.cl.ClTopic;
import org.jeesl.model.ejb.module.cl.ClTrackItem;
import org.jeesl.model.ejb.module.cl.ClTrackStatus;
import org.jeesl.model.ejb.module.cl.ClTrackList;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslClTracklistWc <RREF extends EjbWithId>
					extends JeeslClTracklistGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,ClTopic,ClCheckList,ClCheckItem,ClTrackList,ClTrackItem,ClTrackStatus>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslClTracklistWc(ChecklistFactoryBuilder<IoLang,IoDescription,TenantRealm,ClTopic,ClCheckList,ClCheckItem,ClTrackList,ClTrackItem,ClTrackStatus,IoMarkup,IoMarkupType> fbCl)
	{
		super(fbCl);	
	}
}