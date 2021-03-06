package org.jeesl.api.facade.module;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedback;
import org.jeesl.interfaces.model.module.feedback.JeeslFeedbackThread;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslFeedbackFacade <L extends JeeslLang, D extends JeeslDescription,
									THREAD extends JeeslFeedbackThread<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
									FEEDBACK extends JeeslFeedback<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
									STYLE extends JeeslStatus<L,D,STYLE>,
									TYPE extends JeeslStatus<L,D,TYPE>,
									USER extends EjbWithEmail>
			extends JeeslFacade
{	
	THREAD load(THREAD thread);
}