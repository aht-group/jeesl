package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.domain.EjbDomainItemFactory;
import org.jeesl.factory.ejb.io.domain.EjbDomainSetFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainPathFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainQueryFactory;
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

public class IoDomainFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
				DOMAIN extends JeeslDomain<L,ENTITY>,
				QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
				PATH extends JeeslDomainPath<L,D,QUERY,ENTITY,DATTRIBUTE>,
				ENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
				DATTRIBUTE extends JeeslRevisionAttribute<L,D,ENTITY,?,?>,
				SET extends JeeslDomainSet<L,D,DOMAIN>,
				ITEM extends JeeslDomainItem<QUERY,SET>>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoDomainFactoryBuilder.class);
	
	private final Class<DOMAIN> cDomain; public Class<DOMAIN> getClassDomain() {return cDomain;}
	private final Class<QUERY> cDomainQuery; public Class<QUERY> getClassDomainQuery() {return cDomainQuery;}
	private final Class<PATH> cDomainPath; public Class<PATH> getClassDomainPath() {return cDomainPath;}
	private final Class<ENTITY> cDomainEntity; public Class<ENTITY> getClassDomainEntity() {return cDomainEntity;}
	private final Class<DATTRIBUTE> cDomainAttribute; public Class<DATTRIBUTE> getClassDomainAttribute() {return cDomainAttribute;}
	private final Class<SET> cDomainSet; public Class<SET> getClassDomainSet() {return cDomainSet;}
	private final Class<ITEM> cDomainItem; public Class<ITEM> getClassDomainItem() {return cDomainItem;}
	
	public IoDomainFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<DOMAIN> cDomain, final Class<QUERY> cDomainQuery, final Class<PATH> cDomainPath, final Class<ENTITY> cDomainEntity, final Class<DATTRIBUTE> cDomainAttribute, final Class<SET> cDomainSet, final Class<ITEM> cDomainItem)
	{
		super(cL,cD);
		this.cDomain=cDomain;
		this.cDomainQuery=cDomainQuery;
		this.cDomainPath=cDomainPath;
		this.cDomainEntity=cDomainEntity;
		this.cDomainAttribute=cDomainAttribute;
		this.cDomainSet=cDomainSet;
		this.cDomainItem=cDomainItem;
	}
	
	public EjbSurveyDomainFactory<L,D,DOMAIN,ENTITY> ejbDomain()
	{
		return new EjbSurveyDomainFactory<L,D,DOMAIN,ENTITY>(cDomain);
	}
	
	public EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH> ejbDomainQuery()
	{
		return new EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH>(cDomainQuery);
	}
	
	public EjbSurveyDomainPathFactory<L,D,QUERY,PATH,ENTITY,DATTRIBUTE> ejbDomainPath()
	{
		return new EjbSurveyDomainPathFactory<L,D,QUERY,PATH,ENTITY,DATTRIBUTE>(cDomainPath);
	}
	
	public EjbDomainSetFactory<L,D,DOMAIN,SET> ejbSet()
	{
		return new EjbDomainSetFactory<L,D,DOMAIN,SET>(cDomainSet);
	}
	
	public EjbDomainItemFactory<QUERY,SET,ITEM> ejbItem()
	{
		return new EjbDomainItemFactory<QUERY,SET,ITEM>(cDomainItem);
	}
}