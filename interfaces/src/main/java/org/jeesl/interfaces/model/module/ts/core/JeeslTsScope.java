package org.jeesl.interfaces.model.module.ts.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTsScope <L extends JeeslLang, D extends JeeslDescription,
									CAT extends JeeslTsCategory<L,D,CAT,?>,
									ST extends JeeslTsType<L,D,ST,?>,
									UNIT extends JeeslStatus<L,D,UNIT>,
									EC extends JeeslTsEntityClass<L,D,CAT,?>,
									INT extends JeeslTsInterval<L,D,INT,?>
//									,AS extends JeeslAttributeSet<L,D,?,?>
									>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithCode,
				EjbWithPositionVisibleParent,EjbWithParentAttributeResolver,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>,JeeslWithCategory<CAT>
{
	public enum Attributes{code,category,multiPoints}
	public enum Statistic{none}
	
	CAT getCategory();
	void setCategory(CAT category);
	
	UNIT getUnit();
	void setUnit(UNIT unit);
	
	ST getType();
	void setType(ST type);
	
	String getCode();
	void setCode(String code);
	
	List<INT> getIntervals();
	void setIntervals(List<INT> intervals);
	
	List<EC> getClasses();
	void setClasses(List<EC> classes);
}