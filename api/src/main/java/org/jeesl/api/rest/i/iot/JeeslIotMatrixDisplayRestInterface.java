package org.jeesl.api.rest.i.iot;

import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevices;

public interface JeeslIotMatrixDisplayRestInterface
{
	JsonMatrixDevices devices();
	JsonMatrixDevice deviceJson(String code);
}