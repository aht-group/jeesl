package org.jeesl.factory.java.io.fr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import javax.swing.text.DateFormatter;

import org.exlp.controller.handler.io.log.LoggedExit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslLatestFileFactory
{
	final static Logger logger = LoggerFactory.getLogger(JeeslLatestFileFactory.class);
	
	public static Path pathByDatePattern(Path pDir, DateTimeFormatter dtf)
	{
		try
		{
			for(File f : pDir.toFile().listFiles()) {logger.info(f.toString());}
			
			Stream<Path> stream = Files.list(pDir);
			Path latest = stream
	                .filter(Files::isRegularFile)
	                .map(p -> new NamedFile(dtf,p))
	                .filter(nf -> nf.ts != null)               // only valid pattern
	                .max(Comparator.comparing(nf -> nf.ts))    // latest pattern
	                .map(nf -> nf.path)
	                .orElse(null);
			stream.close();
			
			if(Objects.isNull(latest)) {logger.warn("No latest file in "+pDir.toString()); LoggedExit.exit(true);}
			return latest;
		}
		catch (IOException e) {e.printStackTrace();}
	    return null;
	 }

    private static class NamedFile
    {
        final Path path;
        LocalDateTime ts;
        
        NamedFile(DateTimeFormatter dtf, Path path)
        {
            this.path = path;
            try {this.ts = LocalDateTime.parse(path.getFileName().toString(),dtf);}
            catch (Exception e) {this.ts=null;}
        }
        
       
    }
}