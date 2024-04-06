package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoSsiQuery<CTX extends JeeslIoSsiContext<?,?>,
								STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
								ERROR extends JeeslIoSsiError<?,?,CTX,?>>
					extends JeeslCoreQuery
{
//	void x();
	
	List<CTX> getIoSsiContexts();
	List<STATUS> getIoSsiStatus();
	List<ERROR> getIoSsiErrors();
}