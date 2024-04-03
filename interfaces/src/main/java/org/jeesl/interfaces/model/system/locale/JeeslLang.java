package org.jeesl.interfaces.model.system.locale;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslLang extends Serializable,EjbWithId,EjbRemoveable,EjbSaveable
{	
	public static String attributeLang = "lang";
	public static String en="en";
	
	public enum Att {lang}
	
	String getLkey();
	void setLkey(String lkey);
	
	String getLang();
	void setLang(String lang);
}