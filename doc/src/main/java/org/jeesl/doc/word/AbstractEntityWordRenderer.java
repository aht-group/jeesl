package org.jeesl.doc.word;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Diagram;
import org.jeesl.model.xml.system.revision.Diagrams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Shape;
import com.aspose.words.WrapType;

import net.sf.ahtutils.xml.status.Status;

public abstract class AbstractEntityWordRenderer
{
    final static Logger logger = LoggerFactory.getLogger(AbstractEntityWordRenderer.class);
    
    protected String categoryForCode(Container c, String code)
    {
        for (Status s : c.getStatus())
        {
            if(Objects.nonNull(s.getCode()) && s.getCode() != "" && s.getCode().equals(code))
            {
                return s.getLangs().getLang().get(0).getTranslation();
            }
        }
        return "";
    }

    protected String diagramForCode(Diagrams diagrams, String code)
    {
        for (Diagram d : diagrams.getDiagram())
        {
            if (d.isSetCode() && d.getCode() != "" && d.getCode().equals(code))
            {
                return d.getLangs().getLang().get(0).getTranslation();
            }
        }
        return "";
    }

    
    protected String packageShrinker(String original, int numberOfWordsToShrink)
    {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> splitString = new ArrayList<>();
        int i = 0;
        for (String s : StringUtils.splitPreserveAllTokens(original, "."))
        {
            if (i <= numberOfWordsToShrink)
            {
                splitString.add(StringUtils.left(s, 1));
                i++;
            } else
            {
                splitString.add(s);
            }
        }
        int helper = 1;
        for (String s : splitString)
        {
            if (helper != splitString.size())
            {
                sb.append(s.toString() + ".");
            } else if (helper == splitString.size())
            {
                sb.append(s.toString());
            }
            helper++;
        }
        return sb.toString();
    }

    protected String relationTypeForCode(Container c, String code)
    {
        for (Status s : c.getStatus())
        {
            if(Objects.nonNull(s.getCode()) && s.getCode() != "" && s.getCode().equals(code))
            {
                return s.getSymbol();
            }
        }
        return "";
    }

    protected DocumentBuilder renderStatusSvg(DocumentBuilder docBuilder, byte[] b)
    {
        DocumentBuilder builder = docBuilder;
        try
        {
            Shape shape = builder.insertImage(b);
            shape.setWrapType(WrapType.TIGHT);
            shape.setBehindText(false);  
            shape.setHeight(10);
        } 
        catch (Exception e){e.printStackTrace();}
        return builder;
    }
    
    private Document renderStatusSymbol(Document doc)
    {
        
        
        
        return doc;
    }
    
}
