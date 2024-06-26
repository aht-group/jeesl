package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslAttributeItem <CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,?,SET>,
									SET extends JeeslAttributeSet<?,?,?,?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbWithPositionVisibleParent,EjbRemoveable,EjbWithNonUniqueCode
{
	public enum Attributes{set,criteria,visible,tableHeader}
	
	SET getSet();
	void setSet(SET set);
	
	CRITERIA getCriteria();
	void setCriteria(CRITERIA criteria);
	
	Boolean getTableHeader();
	void setTableHeader(Boolean tableHeader);
	
	String getRemark();
	void setRemark(String remark);
}