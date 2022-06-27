package org.jeesl.interfaces.model.module.hydro;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.crud.EjbCrud;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslHydroRatingPoint <L extends JeeslLang, D extends JeeslDescription,
										STATION extends EjbWithId,
										UNIT extends JeeslHydroRatingUnit<L,D,UNIT,?>
								>
					extends Serializable,EjbSaveable,EjbRemoveable,
							EjbWithId,EjbCrud,
							EjbWithRecord
							
{

	public enum Attributes{decade}

	STATION getStation();
	void setStation(STATION station);
	
	UNIT getGaugeUnit();
	void setGaugeUnit(UNIT gaugeUnit);
	
	UNIT getDischargeUnit();
	void setDischargeUnit(UNIT dischargeUnit);
}