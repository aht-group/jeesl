package org.jeesl.interfaces.model.module.hd.resolution;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslTenant;

@DownloadJeeslTenant
@DownloadJeeslData
public interface JeeslHdLevel <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,G>,
									S extends JeeslMcsStatus<L,D,R,S,G>,
									G extends JeeslGraphic<L,D,?,?,?>>
					extends JeeslMcsStatus<L,D,R,S,G>
{	

}