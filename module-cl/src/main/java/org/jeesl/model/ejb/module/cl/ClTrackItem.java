package org.jeesl.model.ejb.module.cl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackItem;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Table(name="ClTrackItem")
@EjbErNode(name="Track Item",category="tafu",subset="moduleCl")
@Entity
public class ClTrackItem implements JeeslClTrackItem<ClCheckItem,ClTrackList,ClTrackStatus>
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslClTrackItem.Attributes.tracklist.toString();}
	@NotNull @ManyToOne
	private ClTrackList tracklist;
	@Override public ClTrackList getTracklist() {return tracklist;}
	@Override public void setTracklist(ClTrackList tracklist) {this.tracklist = tracklist;}
	
	@NotNull @ManyToOne
	private ClCheckItem item;
	@Override public ClCheckItem getItem() {return item;}
	@Override public void setItem(ClCheckItem item) {this.item = item;}

	@NotNull @ManyToOne
	private ClTrackStatus status;
	@Override public ClTrackStatus getStatus() {return status;}
	@Override public void setStatus(ClTrackStatus status) {this.status = status;}


	@Override public boolean equals(Object object) {return (object instanceof ClTrackItem) ? id == ((ClTrackItem) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		
		return sb.toString();
	}
}