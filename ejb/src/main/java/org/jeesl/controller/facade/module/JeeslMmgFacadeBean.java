package org.jeesl.controller.facade.module;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslMmgFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.MmgFactoryBuilder;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.module.mmg.JeeslWithMmgGallery;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMmgFacadeBean<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>, 
								MG extends JeeslMmgGallery<L>,
								MI extends JeeslMmgItem<L,MG,?,USER>,
								MC extends JeeslMmgClassification<L,R,MC,?>,
								MQ extends JeeslMmgQuality<L,D,MQ,?>,
								USER extends JeeslSimpleUser>
					extends JeeslFacadeBean
					implements JeeslMmgFacade<L,D,R,MG,MI,MC,MQ,USER>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMmgFacadeBean.class);

	private final MmgFactoryBuilder<L,D,?,R,MG,MI,MC,MQ,USER> fbMmg;
	
	public JeeslMmgFacadeBean(EntityManager em, MmgFactoryBuilder<L,D,?,R,MG,MI,MC,MQ,USER> fbMmg)
	{
		super(em);
		this.fbMmg=fbMmg;
	}
	
	@Override public <OWNER extends JeeslWithMmgGallery<MG>> MG fMmgGallery(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MG> cQ = cB.createQuery(fbMmg.getClassGallery());
		Root<OWNER> root = cQ.from(cOwner);
		
		Path<MG> pathCalendar = root.get(JeeslWithMmgGallery.Attributes.mmgGallery.toString());
		Path<Long> pId = root.get(EjbWithId.attribute);
		
		cQ.where(cB.equal(pId,owner.getId()));
		cQ.select(pathCalendar);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbMmg.getClassGallery()+" found for owner "+owner);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+fbMmg.getClassGallery()+" found for owner "+owner);}
	}
}