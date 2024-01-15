package org.jeesl.interfaces.model.io.ai;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoAiOpenAiThread <USER extends JeeslSimpleUser>
						extends Serializable,EjbSaveable,
												JeeslWithRecordDateTime							
{	
	public static enum Code{embeddingAda2};

	USER getUser();
	void setUser(USER user);
	
	String getTopic();
	void setTopic(String topic);
}