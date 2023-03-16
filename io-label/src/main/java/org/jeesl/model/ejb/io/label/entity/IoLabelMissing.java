package org.jeesl.model.ejb.io.label.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoLabelMissing")
@EjbErNode(name="Missing Label",category="revision",subset="revision",level=2)
public class IoLabelMissing implements JeeslRevisionMissingLabel
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private String missingEntity;
	@Override public String getMissingEntity() {return missingEntity;}
	@Override public void setMissingEntity(String missingEntity) {this.missingEntity = missingEntity;}

	private String missingCode;
	@Override public String getMissingCode() {return missingCode;}
	@Override public void setMissingCode(String missingCode) {this.missingCode = missingCode;}

	private String missingLocal;
	@Override public String getMissingLocal() {return missingLocal;}
	@Override public void setMissingLocal(String missingLocal) {this.missingLocal = missingLocal;}
}