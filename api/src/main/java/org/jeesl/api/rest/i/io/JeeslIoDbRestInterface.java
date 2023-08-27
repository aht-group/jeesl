package org.jeesl.api.rest.i.io;

import org.exlp.model.xml.io.Dir;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

import net.sf.ahtutils.xml.sync.DataUpdate;

public interface JeeslIoDbRestInterface
{	
//	void x();
	DataUpdate uploadDumps(Dir directory);
	
	JsonSsiUpdate uploadMetaSnapshot(JsonPostgresMetaSnapshot snapshot);
}