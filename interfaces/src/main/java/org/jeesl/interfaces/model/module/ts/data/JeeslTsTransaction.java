package org.jeesl.interfaces.model.module.ts.data;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTsTransaction <SOURCE extends EjbWithLangDescription<?,?>, 
								DATA extends JeeslTsData<?,?,?,?,?>,
								USER extends EjbWithId,
								CONTAINER extends JeeslFileContainer<?,?>
								>
		extends Serializable,EjbWithId,EjbSaveable,EjbWithRecord,EjbRemoveable,EjbPersistable,
					JeeslWithFileRepositoryContainer<CONTAINER>
{
	enum Attributes{user,record,source,datas}
	
	USER getUser();
	void setUser(USER user);

	String getRemark();
	void setRemark(String remark);
	
	SOURCE getSource();
	void setSource(SOURCE source);
	
	List<DATA> getDatas();
	void setDatas(List<DATA> datas);
	
	String getReference();
	void setReference(String reference);
}