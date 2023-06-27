package org.jeesl.interfaces.model.module.cl;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslClTrackItem <CI extends JeeslClCheckItem<?,?,?>,
									TL extends JeeslClTracklist<?,?,?>,
									TS extends JeeslClTrackStatus<?,?,TS,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver
					
{
	public enum Attributes{id,tracklist}
	
	TL getTracklist();
	void setTracklist(TL tracklist);
	
	CI getItem();
	void setItem(CI item);
}