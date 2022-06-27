package org.jeesl.interfaces.model.module.feedback;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslFeedback<L extends JeeslLang, D extends JeeslDescription,
								THREAD extends JeeslFeedbackThread<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
								FEEDBACK extends JeeslFeedback<L,D,THREAD,FEEDBACK,STYLE,TYPE,USER>,
								STYLE extends JeeslStatus<L,D,STYLE>,
								TYPE extends JeeslStatus<L,D,TYPE>,
								USER extends EjbWithEmail>
						extends EjbWithId,
								EjbSaveable,
								EjbWithRecord
{	
	THREAD getThread();
	void setThread(THREAD thread);
	
	STYLE getStyle();
	void setStyle(STYLE style);
	
	TYPE getType();
	void setType(TYPE type);
	
	USER getUser();
	void setUser(USER user);
	
	String getTxt();
	void setTxt(String txt);
}