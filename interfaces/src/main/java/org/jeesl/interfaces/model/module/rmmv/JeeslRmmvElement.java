package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.util.tree.JeeslTreeElement;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvElement<L extends JeeslLang,
										R extends JeeslTenantRealm<L,?,R,?>,
										TE extends JeeslRmmvElement<L,R,TE,EC>,
										EC extends JeeslRmmvClassification<L,R,EC,?>>
						extends Serializable,EjbSaveable,
								EjbWithId,EjbWithNonUniqueCode,
								EjbWithPositionVisible,
								JeeslTreeElement<TE>,EjbWithLang<L>,
								JeeslWithTenantSupport<R>
								
{	
	EC getClassification1();
	void setClassification1(EC classification1);
}