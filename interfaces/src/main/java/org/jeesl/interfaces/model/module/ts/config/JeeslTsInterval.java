package org.jeesl.interfaces.model.module.ts.config;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
@DownloadJeeslData
public interface JeeslTsInterval <L extends JeeslLang, D extends JeeslDescription,
								S extends JeeslStatus<L,D,S>,
								G extends JeeslGraphic<?,?,?>>
							extends Serializable,EjbSaveable,
										EjbWithCode,JeeslStatusFixedCode,
										EjbWithCodeGraphic<G>,
										JeeslStatus<L,D,S>
{	
	public enum Code{inst,irregular,rt,
						minute,minute10,
						hour,
						day,monthly,weekly,quarterly,yearly}
	public enum Aggregation {hour,day}
}