package org.jeesl.interfaces.model.module.bb.post;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.bb.JeeslBbBoard;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslBbThread<BB extends JeeslBbBoard<?,?,?,BB,?,?>>
						extends EjbWithParentAttributeResolver,
								EjbSaveable,
								EjbWithRecord,EjbWithName
{
	public enum Attributes{board}
	
	BB getBoard();
	void setBoard(BB board);
}