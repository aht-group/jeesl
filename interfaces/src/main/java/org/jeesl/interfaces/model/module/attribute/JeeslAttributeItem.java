package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;

public interface JeeslAttributeItem <CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,?,?>,
									SET extends JeeslAttributeSet<?,?,?,?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbWithPositionVisibleParent,EjbRemoveable,EjbWithNonUniqueCode
{
	public enum Attributes{itemSet,criteria}
	
	SET getItemSet();
	void setItemSet(SET itemSet);
	
	CRITERIA getCriteria();
	void setCriteria(CRITERIA criteria);
	
	Boolean getTableHeader();
	void setTableHeader(Boolean tableHeader);
	
	String getRemark();
	void setRemark(String remark);
}