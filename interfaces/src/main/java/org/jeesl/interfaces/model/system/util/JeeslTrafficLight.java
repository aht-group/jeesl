package org.jeesl.interfaces.model.system.util;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithScope;

public interface JeeslTrafficLight<L extends JeeslLang, D extends JeeslDescription,
								SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
			extends EjbWithId,
					EjbSaveable,EjbRemoveable,
					JeeslWithScope<SCOPE>,
					EjbWithLangDescription<L,D>
{
	public enum Attributes{threshold}
	
	double getThreshold();
	void setThreshold(double threshold);
	
	String getColorBackground();
	void setColorBackground(String colorBackground);
	
	String getColorText();
	void setColorText(String colorText);
}