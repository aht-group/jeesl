package org.jeesl.api.rest.i.io;

import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.exlp.xml.io.Dir;

public interface JeeslIoDbRestInterface
{	
	DataUpdate uploadDumps(Dir directory);
	
//	JsonSsiUpdate uploadMetaSnapshot(JsonPostgresMetaSnapshot snapshot);
}