package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.feedback.EjbFeedbackFactory;
import org.jeesl.factory.ejb.module.feedback.EjbFeedbackThreadFactory;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedback;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedbackThread;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									THREAD extends JeeslFeedbackThread<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
									FEEDBACK extends JeeslFeedback<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
									STYLE extends JeeslStatus<L,D,STYLE>,
									TYPE extends JeeslStatus<L,D,TYPE>,
									USER extends EjbWithEmail>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(FeedbackFactoryBuilder.class);
	
	private final Class<THREAD> cThread;
	private final Class<FEEDBACK> cFeedback;
    
	public FeedbackFactoryBuilder(final Class<L> cL,final Class<D> cD,final Class<THREAD> cThread, final Class<FEEDBACK> cFeedback)
	{       
		super(cL,cD);
		this.cThread = cThread;
        this.cFeedback = cFeedback;
	}
		
	public EjbFeedbackThreadFactory<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER> thread()
	{
		return new EjbFeedbackThreadFactory<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>(cThread);
	}
	
	public EjbFeedbackFactory<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER> feedback()
	{
		return new EjbFeedbackFactory<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>(cFeedback);
	}

}