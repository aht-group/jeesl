package org.jeesl.interfaces.model.io.label.entity;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.label.download.JeeslRestDownloadEntityAttributes;
import org.jeesl.interfaces.model.io.label.download.JeeslRestDownloadEntityDescription;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslRevisionEntity<L extends JeeslLang, D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									REM extends JeeslRevisionEntityMapping<?,?,?>,
									RA extends JeeslRevisionAttribute<L,D,?,?,?>,
									ERD extends JeeslRevisionDiagram<L,D,RC>>
		extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
				EjbWithCode,EjbWithPositionVisible,EjbWithParentAttributeResolver,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>,EjbWithRevisionAttributes<RA>,
				JeeslRestDownloadEntityDescription,JeeslRestDownloadEntityAttributes
{
	public enum Attributes {category,diagram,attributes,jscn}

	String getJscn();
	void setJscn(String jscn);
	
	RC getCategory();
	void setCategory(RC category);

	ERD getDiagram();
	void setDiagram(ERD diagram);
	
	Boolean getTimeseries();
	void setTimeseries(Boolean timeseries);

	Boolean getDocumentation();
	void setDocumentation(Boolean documentation);

	List<REM> getMaps();
	void setMaps(List<REM> maps);

	String getDeveloperInfo();
	void setDeveloperInfo(String developerInfo);


}