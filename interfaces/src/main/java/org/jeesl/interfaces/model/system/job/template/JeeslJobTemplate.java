package org.jeesl.interfaces.model.system.job.template;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslJobTemplate<L extends JeeslLang, D extends JeeslDescription,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>
									>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,//EjbWithPosition,
				EjbWithCode,EjbWithLang<L>,EjbWithDescription<D>
{
	public static enum Attributes{category,type,code};
	public static enum Code{surveyAnalysis}
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	TYPE getType();
	void setType(TYPE type);
	
	PRIORITY getPriority();
	void setPriority(PRIORITY priority);
	
	int getTimeout();
	void setTimeout(int timeout);
	
	EXPIRE getExpiration();
	void setExpiration(EXPIRE expiration);
}