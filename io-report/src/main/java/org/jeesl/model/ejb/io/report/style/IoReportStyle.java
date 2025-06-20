package org.jeesl.model.ejb.io.report.style;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoReportStyle",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="style",category="system",subset="report")
public class IoReportStyle implements JeeslReportStyle<IoLang,IoDescription,IoReportAlignment>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoReportStyleJtLang",joinColumns={@JoinColumn(name="style_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String, IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoReportStyleJtDescription",joinColumns={@JoinColumn(name="style_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private String colorBackground;
	@Override public String getColorBackground() {return colorBackground;}
	@Override public void setColorBackground(String colorBackground) {this.colorBackground = colorBackground;}


	private String font;
	public String getFont() {return font;}
	public void setFont(String font) {this.font = font;}

	private boolean fontBold;
	public boolean isFontBold() {return fontBold;}
	public void setFontBold(boolean fontBold) {this.fontBold = fontBold;}

	private boolean fontItalic;
	public boolean isFontItalic() {return fontItalic;}
	public void setFontItalic(boolean fontItalic) {this.fontItalic = fontItalic;}

	private int sizeFont;
	public int getSizeFont() {return sizeFont;}
	public void setSizeFont(int sizeFont) {this.sizeFont = sizeFont;}

	private String colorFont;
	public String getColorFont() {return colorFont;}
	public void setColorFont(String colorFont) {this.colorFont = colorFont;}


	private boolean borderTop;
	public boolean isBorderTop() {return borderTop;}
	public void setBorderTop(boolean borderTop) {this.borderTop = borderTop;}

	private boolean borderLeft;
	public boolean isBorderLeft() {return borderLeft;}
	public void setBorderLeft(boolean borderLeft) {this.borderLeft = borderLeft;}

	private boolean borderRight;
	public boolean isBorderRight() {return borderRight;}
	public void setBorderRight(boolean borderRight) {this.borderRight = borderRight;}

	private boolean borderBottom;
	public boolean isBorderBottom() {return borderBottom;}
	public void setBorderBottom(boolean borderBottom) {this.borderBottom = borderBottom;}


	private String colorBorderTop;
	public String getColorBorderTop() {return colorBorderTop;}
	public void setColorBorderTop(String colorBorderTop) {this.colorBorderTop = colorBorderTop;}

	private String colorBorderLeft;
	public String getColorBorderLeft() {return colorBorderLeft;}
	public void setColorBorderLeft(String colorBorderLeft) {this.colorBorderLeft = colorBorderLeft;}

	private String colorBorderRight;
	public String getColorBorderRight() {return colorBorderRight;}
	public void setColorBorderRight(String colorBorderRight) {this.colorBorderRight = colorBorderRight;}

	private String colorBorderBottom;
	public String getColorBorderBottom() {return colorBorderBottom;}
	public void setColorBorderBottom(String colorBorderBottom) {this.colorBorderBottom = colorBorderBottom;}


	int sizeBorderTop;
	public int getSizeBorderTop() {return sizeBorderTop;}
	public void setSizeBorderTop(int sizeBorderTop) {this.sizeBorderTop = sizeBorderTop;}

	int sizeBorderLeft;
	public int getSizeBorderLeft() {return sizeBorderLeft;}
	public void setSizeBorderLeft(int sizeBorderLeft) {this.sizeBorderLeft = sizeBorderLeft;}

	int sizeBorderRight;
	public int getSizeBorderRight() {return sizeBorderRight;}
	public void setSizeBorderRight(int sizeBorderRight) {this.sizeBorderRight = sizeBorderRight;}

	int sizeBorderBottom;
	public int getSizeBorderBottom() {return sizeBorderBottom;}
	public void setSizeBorderBottom(int sizeBorderBottom) {this.sizeBorderBottom = sizeBorderBottom;}

	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoReportStyle_alignment"))
	private IoReportAlignment alignment;
	public IoReportAlignment getAlignment() {return alignment;}
	public void setAlignment(IoReportAlignment alignment) {this.alignment = alignment;}


	@Override public boolean equals(Object object){return (object instanceof IoReportStyle) ? id == ((IoReportStyle) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}