package org.jeesl.controller.processor.pcinventory;

import org.jeesl.model.xml.module.inventory.pc.Computer;
import org.jeesl.model.xml.module.inventory.pc.Update;

public class PcInventoryPostProcessor
{
	public Computer postProcess(Computer computer)
	{
		computer.getSoftware().getUpdates();
		
		for (Update update : computer.getSoftware().getUpdates().getUpdate())
		{
			if (update.getDescription().contains("KB2267602"))
			{
				update.setVersion(update.getDescription()
						.replaceFirst("Definitionsupdate für Windows Defender – KB2267602 \\(Definition ", "")
						.replaceAll("\\)", ""));
				update.setCode("KB2267602");
				update.setDescription("Definitionsupdate für Windows Defender");
			}
			else
			{
				if (update.getDescription().contains("KB"))
				{
					update.setCode("KB" + update.getDescription().split("KB")[1].replaceAll("\\)", ""));
					update.setDescription("Windows Update");
					update.setVersion("-");
				}
				if (update.getDescription().contains("Microsoft Silverlight"))
				{
					update.setVersion(update.getDescription().replaceAll("Microsoft Silverlight ", ""));
					update.setDescription("Microsoft Silverlight");
					update.setCode("Microsoft Silverlight");
				}
				if (update.getDescription().contains("Acrobat Reader"))
				{
					update.setVersion(update.getDescription().split("\\(")[1].replaceAll("\\)", ""));
					update.setDescription("Adobe Acrobat Reader");
					update.setCode("Adobe Acrobat Reader");
				}
				if (update.getDescription().contains("12:00:00 AM"))
				{
					update.setVersion(update.getDescription().split("12:00:00 AM - ")[1]);
					update.setDescription("Driver: " + update.getDescription().split("12:00:00 AM - ")[0]);
					update.setCode(update.getDescription().split(" - ")[1]);
				}
				else
				{
					update.setVersion("-");
				}
			}
		}
		return computer;
	}
}