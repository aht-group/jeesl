package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeEdition;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenModule <MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,TYPE,EE,G>,
										STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,G>,
										TYPE extends JeeslMavenType<?,?,TYPE,G>,
										EE extends JeeslIoMavenEeEdition<?,?,EE,G>,
										G extends JeeslGraphic<?,?,?>>
								extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
										EjbWithCode,EjbWithPosition,EjbWithParentAttributeResolver,
										JeeslWithType<TYPE>,EjbWithGraphic<G>
									
{	
	
	public static enum Attributes{id,parent,code,enterpriseEditions};
	
	MODULE getParent();
	void setParent(MODULE parent);
	
	String getLabel();
	void setLabel(String label);
	
	String getAbbreviation();
	void setAbbreviation(String abbreviation);
	
	STRUCTURE getStructure();
	void setStructure(STRUCTURE structure);
	
	List<EE> getEnterpriseEditions();
	void setEnterpriseEditions(List<EE> enterpriseEditions);
}