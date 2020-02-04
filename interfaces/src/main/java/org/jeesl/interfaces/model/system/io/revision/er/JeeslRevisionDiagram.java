package org.jeesl.interfaces.model.system.io.revision.er;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.with.code.EjbWithCode;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.with.position.EjbWithPositionParent;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface JeeslRevisionDiagram <L extends JeeslLang, D extends JeeslDescription,
										C extends JeeslRevisionCategory<L,D,C,?>>
		extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
							EjbWithCode,EjbWithPositionParent,
							EjbWithLang<L>,EjbWithDescription<D>
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