package org.jeesl.interfaces.factory.json;

import org.jeesl.model.pojo.iot.MatrixProcessorCursor;

public interface JeeslJsonMatrixDeviceFactory
{
	String[][] getCells();
	void apply(MatrixProcessorCursor cursor, int row, int column, String color);
}