package org.jeesl.client.model.ejb.system.graphic;

import java.util.List;
import java.util.Map;

import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@EjbErNode(name="Type",category="symbol",subset="symbol",level=3)
public class GraphicType implements JeeslGraphicType<Lang,Description,GraphicType,Graphic>
{
	public enum Code {welcome}
	public static final long serialVersionUID=1;
	
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	protected String symbol;
	public String getSymbol(){return symbol;}
	public void setSymbol(String symbol){this.symbol = symbol;}
	
	@Override public String getCode() {return null;}
	@Override public void setCode(String code) {}
	
	@Override public int getPosition() {return 0;}
	@Override public void setPosition(int position) {}
	
	@Override public boolean isVisible() {return false;}
	@Override public void setVisible(boolean visible) {}
	
	@Override public Map<String, Lang> getName() {return null;}
	@Override public void setName(Map<String, Lang> name) {}
	
	@Override public Map<String, Description> getDescription() {return null;}
	@Override public void setDescription(Map<String, Description> description) {}
	
	@Override public String getStyle() {return null;}
	@Override public void setStyle(String style) {}
	
	@Override public String getImage() {return null;}
	@Override public void setImage(String image) {}
	
	@Override public String getImageAlt() {return null;}
	@Override public void setImageAlt(String image) {}
	
	@Override public <P extends EjbWithCode> P getParent() {return null;}
	@Override public <P extends EjbWithCode> void setParent(P parent) {}
	
	
	public boolean equals(Object object)
	{
        return (object instanceof GraphicType) ? id == ((GraphicType) object).getId() : (object == this);
    }
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
			sb.append(id);
		return sb.toString();
	}
	
	@Override
	public List<String> getFixedCodes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Graphic getGraphic() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setGraphic(Graphic graphic) {
		// TODO Auto-generated method stub
		
	}
}