package net.sf.ahtutils.controller.interfaces.r;

import java.util.List;

public interface RengineCommand
{
	void execute() throws Exception;
	List<String> render();
	void debug();
}
