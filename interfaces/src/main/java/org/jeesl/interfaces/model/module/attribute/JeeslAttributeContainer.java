package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslAttributeContainer <SET extends JeeslAttributeSet<?,?,?,?,?>,
											DATA extends JeeslAttributeData<?,?,?>>
		extends Serializable,EjbRemoveable,EjbWithId,EjbSaveable
{
	public static enum Attributes{datas};
	
	SET getSet();
	void setSet(SET set);
	
	DATA getParent();
	void setParent(DATA parent);
	
	List<DATA> getDatas();
	void setDatas(List<DATA> datas);
}