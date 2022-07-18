package org.jeesl.interfaces.model.io.revision.er;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRestDownloadEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslRevisionDiagram <L extends JeeslLang, D extends JeeslDescription,
										C extends JeeslRevisionCategory<L,D,C,?>>
		extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
							EjbWithCode,EjbWithPositionParent,
							EjbWithLang<L>,EjbWithDescription<D>,
							JeeslRestDownloadEntity
{
	public enum Attributes {category}
	
	C getCategory();
	void setCategory(C category);

	String getDotGraph();
	void setDotGraph(String value);

	boolean isDocumentation();
	void setDocumentation(boolean documentation);

	boolean isDotManual();
	void setDotManual(boolean dotManual);
}