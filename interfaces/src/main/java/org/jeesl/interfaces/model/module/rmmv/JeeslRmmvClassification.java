package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.util.JeeslTreeElement;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvClassification<L extends JeeslLang,
										R extends JeeslTenantRealm<L,?,R,?>,
										C extends JeeslRmmvClassification<L,R,C,G>,
										G extends JeeslGraphic<L,?,?,?,?>>
						extends Serializable,EjbSaveable,
								EjbWithId,EjbWithNonUniqueCode,
								EjbWithPositionVisible,
								JeeslWithTenantSupport<R>,EjbWithLang<L>,JeeslTreeElement<C>,
								EjbWithCodeGraphic<G>								
{	

}