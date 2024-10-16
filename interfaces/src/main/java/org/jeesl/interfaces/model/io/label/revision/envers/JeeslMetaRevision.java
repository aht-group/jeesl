package org.jeesl.interfaces.model.io.label.revision.envers;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMetaRevision implements Serializable,EjbWithId
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMetaRevision.class);

	public enum Type{ADD,MOD,DEL}
	
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}
	
	private long revision;
	public long getRevision() {return revision;}
	public void setRevision(long revision) {this.revision = revision;}

	private Type type;
	public Type getType() {return type;}
	public void setType(Type type) {this.type = type;}
	
	private LocalDateTime record;
	public LocalDateTime getRecord() {return record;}
	public void setRecord(LocalDateTime record) {this.record = record;}
}