package org.jeesl.interfaces.model.io.mail.newsletter;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;

public interface JeeslNewsletterTopic <L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										C extends JeeslNewsletterCategory<L,D,R,C,?>
										>
									extends Serializable,EjbSaveable,EjbRemoveable,
									JeeslWithTenantSupport<R>,
									JeeslWithCategory<C>, EjbWithNonUniqueCode
									,EjbWithLangDescription<L,D>,EjbWithPosition
{
	public enum Attributes{x}
	
}