package org.jeesl.interfaces.model.io.ssi.util;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiSimilarity <SYSTEM extends JeeslIoSsiSystem<?,?>,
									JOB extends JeeslJobStatus<?,?,JOB,?>>
		extends EjbWithId,EjbSaveable,EjbRemoveable
{	
	public enum Attributes{id}
	
	SYSTEM getSystemA();
	void setSystemA(SYSTEM systemA);
	
	SYSTEM getSystemB();
	void setSystemB(SYSTEM systemB);
	
	String getTextA();
	void setTextA(String textA);
	
	String getTextB();
	void setTextB(String textB);
	
	double getLevenshtein();
	void setLevenshtein(double levenshtein);
	
	JOB getFeedback();
	void setFeedback(JOB feedback);
	
}