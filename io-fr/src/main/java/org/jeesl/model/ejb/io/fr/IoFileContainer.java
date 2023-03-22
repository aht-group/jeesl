package org.jeesl.model.ejb.io.fr;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoFileContainer")
@EjbErNode(name="FR Container",category="elib",level=3,subset="ioFr")
public class IoFileContainer implements JeeslFileContainer<IoFileStorage,IoFileMeta>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslFileContainer.Attributes.storage.toString();}
	@NotNull @ManyToOne
	private IoFileStorage storage;
	@Override public IoFileStorage getStorage() {return storage;}
	@Override public void setStorage(IoFileStorage storage) {this.storage = storage;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="container")
	private List<IoFileMeta> metas;
	@Override public List<IoFileMeta> getMetas() {return metas;}
	@Override public void setMetas(List<IoFileMeta> metas) {this.metas = metas;}


	@Override public boolean equals(Object object){return (object instanceof IoFileContainer) ? id == ((IoFileContainer) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}