package net.sf.ahtutils.controller.facade;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.core.JeeslSyncFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.system.sync.EjbSyncFactory;
import org.jeesl.interfaces.model.io.db.JeeslSync;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public class UtilsSyncFacadeBean <L extends JeeslLang, D extends JeeslDescription,
									STATUS extends JeeslStatus<L,D,STATUS>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									SYNC extends JeeslSync<L,D,STATUS,CATEGORY>>  
	extends JeeslFacadeBean implements JeeslSyncFacade<L,D,STATUS,CATEGORY,SYNC>
{	
	private static final long serialVersionUID = 1L;

	public UtilsSyncFacadeBean(EntityManager em)
	{
		super(em);
	}

	@Override
	public SYNC fSync(Class<SYNC> cSync, CATEGORY category, String code) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<SYNC> cQ = cB.createQuery(cSync);
        Root<SYNC> root = cQ.from(cSync);
        
        Path<CATEGORY> pathCategory = root.get("category");
        Path<String> pathCode = root.get("code");
        
        Predicate pCategory = cB.equal(pathCategory,category);
        Predicate pCode = cB.equal(pathCode,code);
        
        cQ.where(cB.and(pCategory,pCode));
        cQ.select(root);
        
		TypedQuery<SYNC> q = em.createQuery(cQ); 
		try	{return q.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found category:"+category.getCode()+" for code="+code);}
	}

	@Override
	public SYNC fcSync(Class<SYNC> cSync, Class<STATUS> cStatus, CATEGORY category, String code)
	{
		SYNC sync = null;
		
		try
		{
			sync = fSync(cSync,category,code);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				STATUS status = this.fByCode(cStatus,JeeslSync.Code.never.toString());
				
				EjbSyncFactory<L,D,STATUS,CATEGORY,SYNC> ef = EjbSyncFactory.factory(cSync);
				sync = ef.build(category,status,code);
				em.persist(sync);
			}
			catch (JeeslNotFoundException e1) {e1.printStackTrace();}
		}
		return sync;
	}

	@Override
	public boolean checkValid(Class<SYNC> cSync, SYNC sync, long seconds)
	{
		sync = em.find(cSync,sync.getId());
		
//		DateTime dtSync = new DateTime(sync.getRecord());
//		DateTime dtNow = new DateTime(sync.getRecord());
		
		LocalDateTime ldtSync = DateUtil.toLocalDateTime(sync.getRecord());
		LocalDateTime ldtNow = LocalDateTime.now();
		
//		boolean diffOk1 = Seconds.secondsBetween(dtSync,dtNow).getSeconds()<seconds;
		boolean diffOk = ChronoUnit.SECONDS.between(ldtSync, ldtNow) < seconds;
		boolean statusOk = sync.getStatus().getCode().equals(JeeslSync.Code.success.toString());
		return (diffOk && statusOk);
	}

	
}
