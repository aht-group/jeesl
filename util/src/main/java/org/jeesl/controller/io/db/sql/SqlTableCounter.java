package org.jeesl.controller.io.db.sql;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.text.OfxTextRenderer;

public class SqlTableCounter
{
	private Map<String, String> dbmap1 = new LinkedHashMap<String, String>();
	private Map<String, String> dbmap2 = new LinkedHashMap<String, String>();
	
	private File f1;
	private File f2;

	public SqlTableCounter(File f1, File f2)
	{
		this.f1=f1;
		this.f2=f2;
	}
	
    public void readData(File f, Map<String,String> map) throws IOException
    {
        LineIterator it = FileUtils.lineIterator(f, "UTF-8");
        try
        {
            while (it.hasNext())
            {
                String line = it.nextLine();
                String[] s = line.split("\\|");
                map.put(s[0], s[1]);

            }
        }
        finally {it.close();}
    }

	public List<Object[]> data() throws IOException
	{
		readData(f1,dbmap1);
		readData(f2, dbmap2);

		Map<String,String> done = new HashMap<String, String>();
		
		for(Map.Entry<String,String> me : dbmap1.entrySet())
		{
			if(!dbmap2.containsKey(me.getKey()))
            {
                done.put(me.getKey(), me.getKey() + "|X| |" + me.getValue() + "| | ") ;
            }
			for (Map.Entry<String, String> me2 : dbmap2.entrySet())
			{
                if(me.getKey().equals(me2.getKey()))
                {
                    if(me.getValue().equals(me2.getValue()))
                    {
                        done.put(me.getKey(), me.getKey() + "|X|X|" + me.getValue() + "|" + me2.getValue() + "|X") ;
                    }
                    else
                    {
                        done.put(me.getKey(), me.getKey() + "|X|X|" + me.getValue()+ "|" + me2.getValue() + "| ") ;
                    }
                }
                else if (!dbmap1.containsKey(me2.getKey()) && !done.containsKey(me2.getKey()) )
                {
                    done.put(me2.getKey(), me2.getKey() + "| |X| |" + me2.getValue() + "| ") ;
                }
            }
        }
        done = sort(done);
        List<Object[]> result = new ArrayList<Object[]>();
        for(Map.Entry<String,String> me : done.entrySet())
        {
            Object tmp [] = me.getValue().split("\\|");
            result.add(tmp);
        }
        return result;
    }

	private List<String> header()
	{
		List<String> header =  new ArrayList<String>();
		header.add("table");
		header.add("db1");
		header.add("db2");
		header.add("c1");
		header.add("c2");
		header.add("equal");
		return header;
	}

	public void output() throws Exception
	{
		Table t = XmlTableFactory.build(header(), data());
		OfxTextRenderer.table(t, System.out);
	}

    public Map<String, String> sort(Map<String, String> data)
    {
        List<Map.Entry<String,String>> unsort = new ArrayList<Map.Entry<String, String>>(data.entrySet());
        Collections.sort(unsort, new Comparator<Map.Entry<String, String>>()
        {
            @Override
            public int compare(Map.Entry<String, String> a, Map.Entry<String, String> b)
            {
                return a.getKey().compareTo(b.getKey());
            }
        });
        Map<String, String> sort = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : unsort)
        {
            sort.put(entry.getKey(), entry.getValue());
        }
        return sort;
    }


}
