package org.jeesl.interfaces.model.io.cms.markup;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMarkup <T extends JeeslIoMarkupType<?,?,T,?>>  extends Serializable,EjbSaveable 
{
	public static String unknwonLanguage = "unknown";
	
	T getType();
	void setType(T type);
	
	String getLkey();
	public void setLkey(String lkey);
	
	String getContent();
	void setContent(String content);
	
//	boolean x();
}