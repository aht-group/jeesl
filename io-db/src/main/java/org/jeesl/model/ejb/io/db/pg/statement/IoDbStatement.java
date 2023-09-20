package org.jeesl.model.ejb.io.db.pg.statement;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;

@Entity
@Table(name="IoDbStatement",uniqueConstraints={@UniqueConstraint(columnNames={"host_id","group_id","record"}),@UniqueConstraint(columnNames={"code"})})
public class IoDbStatement implements JeeslDbStatement<IoSsiHost,IoDbStatementGroup>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoSsiHost host;
	@Override public IoSsiHost getHost() {return host;}
	@Override public void setHost(IoSsiHost host) {this.host = host;}
	
	@Override public String resolveParentAttribute() {return JeeslDbStatement.Attributes.group.toString();}
	@NotNull @ManyToOne
	private IoDbStatementGroup group;
	@Override public IoDbStatementGroup getGroup() {return group;}
	@Override public void setGroup(IoDbStatementGroup group) {this.group = group;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}

	private int rows;
	@Override public int getRows() {return rows;}
	@Override public void setRows(int rows) {this.rows = rows;}

	private int calls;
	@Override public int getCalls() {return calls;}
	@Override public void setCalls(int calls) {this.calls = calls;}

	private double average;
	@Override public double getAverage() {return average;}
	@Override public void setAverage(double average) {this.average = average;}

	private double total;
	@Override public double getTotal() {return total;}
	@Override public void setTotal(double total) {this.total = total;}

	@Basic @Column(columnDefinition="text")
	private String sql;
	@Override public String getSql() {return sql;}
	@Override public void setSql(String sql) {this.sql = sql;}

	@Basic @Column(columnDefinition="text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}


	@Override public boolean equals(Object object){return (object instanceof IoDbStatement) ? id == ((IoDbStatement) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("|");
		return sb.toString();
	}
}