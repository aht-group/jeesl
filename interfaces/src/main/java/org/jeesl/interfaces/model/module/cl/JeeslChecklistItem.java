package org.jeesl.interfaces.model.module.cl;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslChecklistItem <L extends JeeslLang,
								CL extends JeeslChecklist<L,?,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,EjbWithPositionVisible,
					EjbWithLang<L>
					
{
	public enum Attributes{id,checklist,position,visible}
	
	CL getChecklist();
	void setChecklist(CL checklist);
}