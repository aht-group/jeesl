package org.jeesl.controller.facade.io;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMavenFactoryBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenFacadeBean <L extends JeeslLang,D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,?,?>,
									DEVELOPMENT extends JeeslIoMavenModule>
	extends JeeslFacadeBean implements JeeslIoMavenFacade<L,D,GROUP,ARTIFACT,VERSION>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	private final IoMavenFactoryBuilder<L,D,GROUP,ARTIFACT,VERSION,DEVELOPMENT> fbMaven;
	
	public JeeslIoMavenFacadeBean(EntityManager em, final IoMavenFactoryBuilder<L,D,GROUP,ARTIFACT,VERSION,DEVELOPMENT> fbMaven)
	{
		super(em);
		this.fbMaven=fbMaven;
	}

	@Override public ARTIFACT fIoMavenArtifact(GROUP group, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ARTIFACT> cQ = cB.createQuery(fbMaven.getClassArtifact());
		Root<ARTIFACT> root = cQ.from(fbMaven.getClassArtifact());
		
		Path<GROUP> pGroup = root.get(JeeslIoMavenArtifact.Attributes.group.toString());
		Path<String> eCode = root.get(JeeslIoMavenArtifact.Attributes.code.toString());
		predicates.add(cB.equal(pGroup,group));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<ARTIFACT> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbMaven.getClassArtifact().getSimpleName()+" found for group="+group.getCode()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbMaven.getClassArtifact().getSimpleName()+" not unique for group:"+group.getCode()+" and code="+code);}
	}

	@Override
	public VERSION fIoMavenVersion(ARTIFACT artifact, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VERSION> cQ = cB.createQuery(fbMaven.getClassVersion());
		Root<VERSION> root = cQ.from(fbMaven.getClassVersion());
		
		Path<ARTIFACT> pArtifact = root.get(JeeslIoMavenVersion.Attributes.artifact.toString());
		Path<String> eCode = root.get(JeeslIoMavenVersion.Attributes.code.toString());
		predicates.add(cB.equal(pArtifact,artifact));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<VERSION> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbMaven.getClassVersion().getSimpleName()+" found for artifact="+artifact.getCode()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbMaven.getClassVersion().getSimpleName()+" not unique for artifact:"+artifact.getCode()+" and code="+code);}
	}
}