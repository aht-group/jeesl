package org.jeesl.interfaces.model.system.security.util;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithTypeCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionType;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionTypeVisible;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityCategory<L extends JeeslLang, D extends JeeslDescription>
			extends Serializable,EjbWithCode,EjbRemoveable,EjbPersistable,
				EjbWithId,EjbWithTypeCode,
				EjbWithPositionTypeVisible,EjbWithPositionVisible,EjbWithPositionType,
				EjbWithLang<L>,EjbWithDescription<D>,
				EjbSaveable
{
	public enum Attributes{code,type}
	public static enum Type {role,view,usecase,action}
	
	public Boolean getDocumentation();
	public void setDocumentation(Boolean documentation);
}