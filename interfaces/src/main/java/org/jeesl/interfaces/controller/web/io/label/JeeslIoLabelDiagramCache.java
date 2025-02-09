package org.jeesl.interfaces.controller.web.io.label;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;

public interface JeeslIoLabelDiagramCache <ERD extends JeeslRevisionDiagram<?,?,?>> 
		extends Serializable
{
	void cacheIoLabelDiagramPut(ERD diagram);
	ERD cacheIoLabelDiagramGet();
}