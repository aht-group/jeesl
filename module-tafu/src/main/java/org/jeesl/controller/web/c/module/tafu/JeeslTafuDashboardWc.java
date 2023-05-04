package org.jeesl.controller.web.c.module.tafu;

import org.jeesl.controller.web.g.module.tafu.JeeslTafuDashboardGwc;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeek;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.tafu.TafuScope;
import org.jeesl.model.ejb.module.tafu.TafuStatus;
import org.jeesl.model.ejb.module.tafu.TafuViewport;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslTafuDashboardWc <RREF extends EjbWithId,
    										T extends JeeslTafuTask<TenantRealm,TafuStatus,TafuScope,M>,
    										DOW extends JeeslCalendarDayOfWeek<IoLang,IoDescription,DOW,?>,
    										M extends JeeslIoMarkup<MT>,
    										MT extends JeeslIoMarkupType<IoLang,IoDescription,MT,?>
    										>
					extends JeeslTafuDashboardGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,T,TafuStatus,TafuScope,TafuViewport,DOW,M,MT>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslTafuDashboardWc(TafuFactoryBuilder<IoLang,IoDescription,TenantRealm,T,TafuStatus,TafuScope,TafuViewport,DOW,M,MT> fbTafu)
	{
		super(fbTafu);	
		
	}
}