package org.jeesl.interfaces.model.module.ts.config;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.download.JeeslRestDownloadEntityDescription;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;

public interface JeeslTsInputMethod <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslStatus<L,D,S>,
									G extends JeeslGraphic<L,D,?,?,?>>
					extends Serializable,EjbPersistable,
							EjbWithCode,JeeslStatusFixedCode,
							JeeslRestDownloadEntityDescription,JeeslRestDownloadOption,
							EjbWithCodeGraphic<G>,
							JeeslStatus<L,D,S>
{	
	public enum Code{single,multi,manual}
}