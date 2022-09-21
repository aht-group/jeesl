package org.jeesl.api.rest.system.io.db;

import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.exlp.xml.io.Dir;

public interface JeeslIoDbRestInterface
{	
	DataUpdate uploadDumps(Dir directory);
}