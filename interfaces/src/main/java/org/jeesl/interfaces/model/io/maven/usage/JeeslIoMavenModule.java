package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionMigration;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenModule <MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,G>,
										STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,G>,
										G extends JeeslGraphic<?,?,?>>
								extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
										EjbWithCode,EjbWithPositionMigration,EjbWithParentAttributeResolver,
										EjbWithGraphic<G>
									
{	
	
	public static enum Attributes{parent,code};
	
	MODULE getParent();
	void setParent(MODULE parent);
	
	String getLabel();
	void setLabel(String label);
	
	STRUCTURE getStructure();
	void setStructure(STRUCTURE structure);
}