package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyOption<L extends JeeslLang, D extends JeeslDescription>
			extends Serializable,EjbWithId,EjbWithNonUniqueCode,EjbWithPosition,//EjbSaveable,
					EjbWithLang<L>,EjbWithDescription<D>
{
	enum Units{yn,number,txt}
	enum Status{open}
	
	double getScore();
	void setScore(double score);
	
	boolean getRow();
	void setRow(boolean row);
	
	boolean getCol();
	void setCol(boolean col);
	
	boolean getCell();
	void setCell(boolean cell);
}