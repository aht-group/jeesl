package org.jeesl.controller.facade.jx.module;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.module.JeeslFeedbackFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedback;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedbackThread;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public class JeeslFeedbackFacadeBean<L extends JeeslLang, D extends JeeslDescription,
										THREAD extends JeeslFeedbackThread<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
										FEEDBACK extends JeeslFeedback<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
										STYLE extends JeeslStatus<L,D,STYLE>,
										TYPE extends JeeslStatus<L,D,TYPE>,
										USER extends EjbWithEmail>
					extends JeeslFacadeBean
					implements JeeslFeedbackFacade<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>
{
	private static final long serialVersionUID = 1L;
	private final Class<THREAD> cThread;
		
	public JeeslFeedbackFacadeBean(EntityManager em, final Class<THREAD> cThread)
	{
		super(em);
		this.cThread=cThread;
	}

	@Override public THREAD load(THREAD thread)
	{
		thread = em.find(cThread, thread.getId());
		thread.getFeedbacks().size();
		return thread;
	}
}