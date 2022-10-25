package org.jeesl.controller.handler.system.io.fr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryTreeHandler
{
	final static Logger logger = LoggerFactory.getLogger(DirectoryTreeHandler.class);

	private final Path baseDir;
	
	private final int chunks;
	private final int maxDepth;
	private String padding;
	
	public DirectoryTreeHandler(Path baseDir, int chunks, int maxDepth)
	{
		this.baseDir=baseDir;
		this.chunks=chunks;
		this.maxDepth=maxDepth;
		padding = "-";
	}
	
	public Path create(String identifier) throws IOException
	{
		Path p = this.build(identifier);
		if(!Files.exists(p.getParent()))
		{
//			logger.info("Creating: "+p.getParent());
			Files.createDirectories(p.getParent());
		}
		return p;
	}
	
	public Path build(String identifier)
	{
		String name = new String(identifier);
		while(name.length()<(chunks*maxDepth))
		{
			name = name + padding;
		}
		logger.info(name);
		return build(0,baseDir,name);
	}
	
	private Path build(int level, Path path, String name)
	{
		int offset = (level*chunks);
		String id = name.substring(offset,offset+chunks);
		
		Path p = path.resolve(id);
//		logger.info(path.toString()+" -- "+p.toString());
		
		if(level==(maxDepth-1)) {return p.resolve(name);}
		else {return build(level+1,p,name);}
	}
}