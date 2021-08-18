package org.jeesl.doc.word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.words.Document;
import com.aspose.words.Style;
import com.aspose.words.StyleCollection;

public class TemplateStylesHelper
{

    final static Logger LOGGER = LoggerFactory.getLogger(TemplateStylesHelper.class);
    
    public TemplateStylesHelper() {}
    
//  first get all styles in source document and print their names
    public static StyleCollection getAllStylesFromDocument(Document doc) 
    {
        StyleCollection styles = doc.getStyles();
        for (Style s: styles) {LOGGER.info("StyleName: " + s.getName() + "    BaseStyleName: " + s.getBaseStyleName() + "    LinkedStyleName" + s.getLinkedStyleName());}
        return styles;
    }
    
    public static StyleCollection getStylesFromSourceDocument(StyleCollection styles,Document destDoc, String prefix) throws Exception 
    {
        StyleCollection destStyles = destDoc.getStyles();
        for (Style s: styles) 
        {
            s.setName(prefix+s.getName());
            if (s.getLinkedStyleName()!="")
            {
                Style linked = styles.get(s.getLinkedStyleName());
                if(linked!=null) 
                {
                    linked.setName(prefix+s.getLinkedStyleName());
                    destStyles.addCopy(linked);
                }
            }
            destStyles.addCopy(s);
            LOGGER.info("add style: " + s.getName() + "    if exists add linked style: " + s.getLinkedStyleName());
        }
        return destStyles;        
    }
}
