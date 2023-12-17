package org.jeesl.interfaces.model.io.db.dump;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbBackupArchive<SYSTEM extends JeeslIoSsiSystem<?,?>,
							FILE extends JeeslDbBackupFile<?,?,?>
							>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithRecord,EjbWithName
{
	public static enum Attributes{record};
	
	public enum Tag {dbBackupTag,dbDumpTag,dbRestoreTag};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	long getSize();
	void setSize(long size);
	
	List<FILE> getFiles();
	void setFiles(List<FILE> files);
}