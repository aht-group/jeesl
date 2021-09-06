package org.jeesl.client.maintenance;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class DuplicateHashCodeFinder
{
	private Map<String,String> hash;	//1. String = filepath & 2. String = Arguments
	private File startDir;

	public DuplicateHashCodeFinder(File startDir)
	{
		this.startDir = startDir;
		this.hash = new HashMap<String, String>();
	}

	public Map<String,String> searchForHashCodeBuilder()
	{
		File [] files = startDir.listFiles();
		if(files != null)
		{
			fileloop:
			for(File f : files)
			{
				if(f.isDirectory())
				{
					startDir = f;
					searchForHashCodeBuilder();
				} else if(f.getName().endsWith(".java"))
				{
					try
					{
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
						String input = br.readLine();
						while(input != null)
						{
							if(input.contains("new HashCodeBuilder"))
							{
								hash.put(f.getName(), input.replaceAll("((?=[^,])\\D*)", ""));
								continue fileloop;
							}
							input = br.readLine();
						}
					} catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return hash;
	}

	public Map<String, String> compareCode()
	{
		Map<String, String> fileList = new HashMap<String, String>();
		compare:
		for(Map.Entry<String, String> entry : hash.entrySet())
		{
			enterEmptyArgs(entry,fileList);
			String temp = entry.getValue();
			for(Map.Entry<String, String> entry2 : hash.entrySet())
			{
				if(temp.equals(entry2.getValue())&& !entry.getKey().equals(entry2.getKey()))
				{
					if(fileList.size() >1)
					{
						for (Map.Entry<String, String> file : fileList.entrySet())
						{
							if(file.getKey().equals(entry2.getKey())){continue compare;}
						}
					}
					fileList.put(entry2.getKey(),temp);
				}
			}
		}
		return sortByComparator(fileList, true);
	}

	private static Map<String, String> sortByComparator(Map<String, String> unsortMap, final boolean order)
	{

		List<Entry<String, String>> list = new LinkedList<Entry<String, String>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, String>>()
		{
			public int compare(Entry<String, String> o1, Entry<String, String> o2)
			{
				if (order){return o1.getValue().compareTo(o2.getValue());}
				else{return o2.getValue().compareTo(o1.getValue());	}
			}
		});

		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Entry<String, String> entry : list){sortedMap.put(entry.getKey(), entry.getValue());}

		return sortedMap;
	}

	private void enterEmptyArgs(Map.Entry<String, String> me, Map<String, String> fileList) {if(me.getValue().isEmpty()){fileList.put(me.getKey(),me.getValue());}}

	public Map<String, String> getHash(){return hash;}


}
