package org.jeesl.interfaces.model.module.mmg;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.util.tree.JeeslTreeClassification;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslMmgClassification<L extends JeeslLang,
										R extends JeeslTenantRealm<L,?,R,?>,
										C extends JeeslMmgClassification<L,R,C,G>,
										G extends JeeslGraphic<?,?,?>>
							extends JeeslTreeClassification<L,R,C,G>					
{	

}