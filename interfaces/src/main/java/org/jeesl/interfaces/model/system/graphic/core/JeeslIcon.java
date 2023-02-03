package org.jeesl.interfaces.model.system.graphic.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
@DownloadJeeslData
public interface JeeslIcon <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslStatus<L,D,S>,
									G extends JeeslGraphic<?,?,?>>
						extends Serializable,EjbPersistable,
								EjbWithCode,EjbWithSymbol,JeeslStatusWithSymbol,
								JeeslStatusFixedCode,EjbWithCodeGraphic<G>,
								JeeslStatus<L,D,S>
{
	public enum Network {download,upload}
	public enum CodeTreeFilter{tfCollapseAll,tfExpandAll,tfExpandTwo,tfExpandThree,tfExpandRelevant}
	public enum Ejb{ejbUnsaved,jeeslInvisible}
	public enum Crud{jeeslDelete}
	public enum Arrow{jeeslArrowLeft}
	public enum Information{jeeslMegaphone}
	public enum Code{jeeslWorkflow}
	
	public static final String jeeslLibIcons = "jeesl/prototype/library-icons.xml";
}