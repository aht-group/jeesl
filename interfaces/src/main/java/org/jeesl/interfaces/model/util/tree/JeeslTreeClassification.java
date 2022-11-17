package org.jeesl.interfaces.model.util.tree;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslTreeClassification<L extends JeeslLang,
										R extends JeeslTenantRealm<L,?,R,?>,
										C extends JeeslTreeClassification<L,R,C,G>,
										G extends JeeslGraphic<?,?,?>>
						extends Serializable,EjbSaveable,
								EjbWithId,EjbWithNonUniqueCode,EjbWithPositionVisible,
								JeeslWithTenantSupport<R>,EjbWithLang<L>,JeeslTreeElement<C>,
								EjbWithCodeGraphic<G>								
{	

}