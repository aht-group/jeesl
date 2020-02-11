package org.jeesl.interfaces.model.module.ts.data;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.with.EjbWithLangDescription;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithRecord;

public interface JeeslTsTransaction <SOURCE extends EjbWithLangDescription<?,?>, 
								DATA extends JeeslTsData<?,?,?,?>,
								USER extends EjbWithId,
								CONTAINER extends JeeslFileContainer<?,?>
								>
		extends Serializable,EjbWithId,EjbSaveable,EjbWithRecord,EjbRemoveable,EjbPersistable,
					JeeslWithFileRepositoryContainer<CONTAINER>
{
	enum Attributes{user,record}
	
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