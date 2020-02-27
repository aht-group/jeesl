package org.jeesl.web.mbean.prototype.system.io.domain;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDomainFacade;
import org.jeesl.factory.builder.io.IoDomainFactoryBuilder;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainItem;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDomainBean <L extends JeeslLang, D extends JeeslDescription,
						DOMAIN extends JeeslDomain<L,ENTITY>,
						QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
						PATH extends JeeslDomainPath<L,D,QUERY,ENTITY,ATTRIBUTE>,
						ENTITY extends JeeslRevisionEntity<L,D,?,?,ATTRIBUTE,?>,
						ATTRIBUTE extends JeeslRevisionAttribute<L,D,ENTITY,?,?>,
						SET extends JeeslDomainSet<L,D,DOMAIN>,
						ITEM extends JeeslDomainItem<QUERY,SET>>
					extends AbstractAdminBean<L,D>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDomainBean.class);
	
	protected JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fDomain;
	
	protected final IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fbDomain;
	
	protected List<QUERY> queries; public List<QUERY> getQueries(){return queries;}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDomainBean(IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fbDomain)
	{
		super(fbDomain.getClassL(),fbDomain.getClassD());

		this.fbDomain=fbDomain;

	}
	
	protected void postConstructDomain(JeeslTranslationBean bTranslation, JeeslFacesMessageBean bMessage,
			JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fDomain)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fDomain=fDomain;
	}
}