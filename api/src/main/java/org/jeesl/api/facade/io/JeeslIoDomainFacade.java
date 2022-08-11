package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainItem;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface JeeslIoDomainFacade <L extends JeeslLang, D extends JeeslDescription,
									DOMAIN extends JeeslDomain<L,DENTITY>,
									QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
									PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
									DENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
									DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>,
									SET extends JeeslDomainSet<L,D,DOMAIN>,
									ITEM extends JeeslDomainItem<QUERY,SET>>
	extends JeeslFacade
{
	List<DATTRIBUTE> fDomainAttributes(DENTITY entity);
}