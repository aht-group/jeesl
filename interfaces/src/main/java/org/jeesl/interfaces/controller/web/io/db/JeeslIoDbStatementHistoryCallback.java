package org.jeesl.interfaces.controller.web.io.db;

import java.io.Serializable;

import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;

public interface JeeslIoDbStatementHistoryCallback extends Serializable
{
	void upload(JsonPostgresStatementGroup json);
}