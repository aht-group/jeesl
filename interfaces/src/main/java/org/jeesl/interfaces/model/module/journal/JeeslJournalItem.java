package org.jeesl.interfaces.model.module.journal;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.w.JeeslWithMarkupMulti;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslJournalItem <L extends JeeslLang, D extends JeeslDescription,
								M extends JeeslIoMarkup<MT>, MT extends JeeslIoMarkupType<L,D,MT,?>,
								LOG extends JeeslJournalBook<?,?>,
								IMPACT extends JeeslJournalImpact<L,D,IMPACT,?>,
								CONF extends JeeslJournalScope<L,D,CONF,?>,
								USER extends EjbWithId
								>
		extends Serializable,EjbWithId,
				EjbSaveable,EjbPersistable,EjbRemoveable,
				EjbWithLang<L>,EjbWithRecord,JeeslWithMarkupMulti<M>
{
	public enum Attributes{log,record,confidentialities}
	
	LOG getLog();
	void setLog(LOG log);
	
	USER getAuthor();
	void setAuthor(USER author);
	
	IMPACT getImpact();
	void setImpact(IMPACT impact);
	
	List<CONF> getConfidentialities();
	void setConfidentialities(List<CONF> confidentialities);
}