package org.jeesl.controller.facade.jx.io;

import java.util.List;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.io.JeeslIoDomainFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.factory.builder.io.IoDomainFactoryBuilder;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainItem;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoDomainFacadeBean <L extends JeeslLang, D extends JeeslDescription, 
				DOMAIN extends JeeslDomain<L,DENTITY>,
				QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
				PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
				DENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
				DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>,
				SET extends JeeslDomainSet<L,D,DOMAIN>,
				ITEM extends JeeslDomainItem<QUERY,SET>>
	extends JeeslFacadeBean implements JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,SET,ITEM>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoDomainFacadeBean.class);
		
	private final IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,SET,ITEM> fbDomain;
	
	public JeeslIoDomainFacadeBean(EntityManager em,
			final IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,SET,ITEM> fbDomain)
	{
		super(em);
		this.fbDomain=fbDomain;
	}
	
	@Override
	public List<DATTRIBUTE> fDomainAttributes(DENTITY entity)
	{
		entity = em.find(fbDomain.getClassDomainEntity(), entity.getId());
		entity.getAttributes().size();
		return entity.getAttributes();
	}
}