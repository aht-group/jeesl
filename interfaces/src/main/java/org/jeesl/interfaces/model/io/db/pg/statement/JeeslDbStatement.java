package org.jeesl.interfaces.model.io.db.pg.statement;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithRefId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbStatement <HOST extends JeeslIoSsiHost<?,?,?>,
									SG extends JeeslDbStatementGroup<?>>
							extends EjbWithId,EjbSaveable,EjbRemoveable,
									EjbWithCode,JeeslWithRecordDateTime,EjbWithRefId,EjbWithParentAttributeResolver
{
	public static enum Attributes{group};
	
	HOST getHost();
	void setHost(HOST host);
	
	SG getGroup();
	void setGroup(SG group);
	
	int getRows();
	public void setRows(int rows);

	int getCalls();
	void setCalls(int calls);

	double getAverage();
	void setAverage(double average);

	double getTotal();
	void setTotal(double total);

	String getSql();
	void setSql(String sql);
	
	String getRemark();
	void setRemark(String remark);
}