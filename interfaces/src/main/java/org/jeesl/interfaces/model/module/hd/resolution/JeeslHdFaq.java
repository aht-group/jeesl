package org.jeesl.interfaces.model.module.hd.resolution;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.model.with.system.status.JeeslWithScope;

public interface JeeslHdFaq<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslHdCategory<L,D,R,CAT,?>,
								SCOPE extends JeeslHdScope<?,?,SCOPE,?>>
						extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithId,JeeslWithTenantSupport<R>,
								EjbWithPositionVisible,
								JeeslWithCategory<CAT>, JeeslWithScope<SCOPE>,
								EjbWithLangDescription<L,D>
{	
	public enum Attributes {category,scope}
}