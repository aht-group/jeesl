package org.jeesl.interfaces.model.io.cms;

import java.io.Serializable;
import java.util.Map;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisibleMigration;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;

public interface JeeslIoCmsElement<
									V extends JeeslIoCmsVisiblity,
									S extends JeeslIoCmsSection<?,S>,
									EC extends JeeslStatus<?,?,EC>,
									ET extends JeeslStatus<?,?,ET>,
									C extends JeeslIoCmsContent<V,?,?>,
									FC extends JeeslFileContainer<?,?>>
		extends Serializable,EjbWithId,EjbPersistable,EjbSaveable,EjbWithPositionParent,
				EjbWithVisibleMigration,
				JeeslWithFileRepositoryContainer<FC>
{	
	public enum Attributes{section}
	
	public enum Type{paragraph,image,
					wfProcess,
					sysStatusTable,sysStatusList}
	
	S getSection();
	void setSection(S section);
	
	ET getType();
	void setType(ET type);
	
	public Map<String,C> getContent();
	public void setContent(Map<String,C> content);
	
	String getJson();
	void setJson(String json);
}