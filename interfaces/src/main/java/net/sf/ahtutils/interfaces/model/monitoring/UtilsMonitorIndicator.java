package net.sf.ahtutils.interfaces.model.monitoring;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface UtilsMonitorIndicator<C extends UtilsMonitorComponent<C,I,D,V>,
										I extends UtilsMonitorIndicator<C,I,D,V>,
										D extends UtilsMonitorData<C,I,D,V>,
										V extends UtilsMonitorValue<C,I,D,V>>
		extends Serializable,EjbWithId,EjbWithName,EjbWithCode,EjbSaveable
{
//	C getComponent();
//	void setComponent(C component);
}