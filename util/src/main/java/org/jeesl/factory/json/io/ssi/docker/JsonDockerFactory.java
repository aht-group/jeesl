package org.jeesl.factory.json.io.ssi.docker;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.json.io.ssi.docker.JsonDocker;
import org.jeesl.model.json.io.ssi.docker.JsonDockerContainer;

public class JsonDockerFactory
{
	public static JsonDocker build(List<JsonDockerContainer> list)
	{
		JsonDocker json = new JsonDocker();
		json.setContainers(new ArrayList<JsonDockerContainer>());
		json.getContainers().addAll(list);
		return json;
	}
}