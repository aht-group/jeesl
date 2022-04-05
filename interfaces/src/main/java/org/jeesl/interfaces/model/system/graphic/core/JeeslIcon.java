package org.jeesl.interfaces.model.system.graphic.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.revision.entity.JeeslRestDownloadEntityDescription;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;

public interface JeeslIcon <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslStatus<L,D,S>,
									G extends JeeslGraphic<L,D,?,?,?>>
						extends Serializable,EjbPersistable,
								JeeslRestDownloadOption,JeeslRestDownloadEntityDescription,
								JeeslStatusFixedCode,EjbWithCodeGraphic<G>,
								JeeslStatus<L,D,S>
{
	public enum Network {download,upload}
	public static enum CodeTreeFilter{tfCollapseAll,tfExpandAll,tfExpandTwo,tfExpandThree,tfExpandRelevant}
	public enum Ejb{ejbUnsaved,ejbInvisible}
	
	public static final String jeeslLibIcons = "jeesl/prototype/library-icons.xml";
}