package org.jeesl.interfaces.model.module.lf.value;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicator;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeElement;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeGroup;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;

public interface JeeslLfValue<I extends JeeslLfIndicator<?,?,?,?,?,TG,?>,
								VT extends JeeslLfValueType<?,?,VT,?>,
						TG extends JeeslLfTimeGroup<?,?>,
						TE extends JeeslLfTimeElement<?,TG>>
				extends  Serializable,EjbSaveable,EjbRemoveable,EjbWithId,EjbWithParentAttributeResolver,
						JeeslWithType<VT>
{
	public enum Attributes{indicator}

	I getIndicator();
	void setIndicator(I indicator);

	TE getTimeElement();
	void setTimeElement(TE timeElement);

	double getValue();
	void setValue(double value);
}
