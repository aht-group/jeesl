package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslBbFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.module.BbFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.bb.JeeslBbBoard;
import org.jeesl.interfaces.model.module.bb.post.JeeslBbPost;
import org.jeesl.interfaces.model.module.bb.post.JeeslBbThread;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslBulletinBoardFacadeBean<L extends JeeslLang,D extends JeeslDescription,
										SCOPE extends JeeslStatus<L,D,SCOPE>,
										BB extends JeeslBbBoard<L,D,SCOPE,BB,PUB,USER>,
										PUB extends JeeslStatus<L,D,PUB>,
										THREAD extends JeeslBbThread<BB>,
										POST extends JeeslBbPost<THREAD,M,USER>,
										M extends JeeslIoMarkup<MT>,
										MT extends JeeslIoMarkupType<L,D,MT,?>,
										USER extends EjbWithEmail>
					extends JeeslFacadeBean
					implements JeeslBbFacade<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslBulletinBoardFacadeBean.class);
	
	private final BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb;
	
	public JeeslBulletinBoardFacadeBean(EntityManager em, final BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb)
	{
		super(em);
		this.fbBb=fbBb;
	}
	
	@Override
	public List<BB> fBulletinBoards(SCOPE scope, long refId)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<BB> cQ = cB.createQuery(fbBb.getClassBoard());
		Root<BB> bb = cQ.from(fbBb.getClassBoard());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = bb.get(JeeslBbBoard.Attributes.refId.toString());
		Path<SCOPE> pScope = bb.get(JeeslBbBoard.Attributes.scope.toString());
		
		predicates.add(cB.equal(eRefId,refId));
		predicates.add(cB.equal(pScope,scope));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(bb);

		TypedQuery<BB> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
}