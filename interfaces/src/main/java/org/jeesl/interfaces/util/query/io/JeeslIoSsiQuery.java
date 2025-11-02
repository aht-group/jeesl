package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoSsiQuery<SYSTEM extends JeeslIoSsiSystem<?,?>,
								CRED extends JeeslIoSsiCredential<SYSTEM>,
								CTX extends JeeslIoSsiContext<?,?>,
								STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
								ERROR extends JeeslIoSsiError<?,?,CTX,?>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
					extends JeeslCoreQuery
{
//	void x();
	
	List<SYSTEM> getIoSsiSystems();
	List<CRED> getIoSsiCredentials();
	List<CTX> getIoSsiContexts();
	List<STATUS> getIoSsiStatus();
	List<ERROR> getIoSsiErrors();
}