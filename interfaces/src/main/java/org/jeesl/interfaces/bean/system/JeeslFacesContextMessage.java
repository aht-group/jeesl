package org.jeesl.interfaces.bean.system;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslFacesContextMessage
{
	public enum Summary {summarySuccess}
	public enum Detail {detailSaved};
	
	void info(String summary, String detail);
	void info(String id, String summary, String detail);
	
	void warn(String summary, String detail);
	void warn(String id, String summary, String detail);
	
	void error(String summary, String detail);
	void error(String id,String summary, String detail);
}