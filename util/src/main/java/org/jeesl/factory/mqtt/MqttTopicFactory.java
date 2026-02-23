package org.jeesl.factory.mqtt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttTopicFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttTopicFactory.class);
		
	public static String build(Serializable...attributes)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<attributes.length;i++)
		{
			sb.append(attributes[i].toString());
			if(i<(attributes.length-1)) {sb.append("/");}
		}
		return sb.toString();
	}
	
	public static String append(String topic, Serializable suffix, int index)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(topic);
		sb.append("/").append(suffix).append(index);
		return sb.toString();
	}
	
	public static Optional<List<String>> extractPlusValues(String topic, String filter)
	{
        if (ObjectUtils.anyNull(topic,filter)) {return Optional.empty();}

        String[] topicLevels = topic.split("/", -1);
        String[] filterLevels = filter.split("/", -1);

        List<String> plusValues = new ArrayList<>();

        int topicIndex = 0;

        for (int filterIndex = 0; filterIndex < filterLevels.length; filterIndex++)
        {
            String filterLevel = filterLevels[filterIndex];

            if (filterLevel.equals("#"))
            {
                boolean isLastFilterLevel = filterIndex == filterLevels.length - 1;

                if (!isLastFilterLevel) {
                    return Optional.empty();
                }

                return Optional.of(Collections.unmodifiableList(plusValues));
            }

            if (topicIndex >= topicLevels.length) {
                return Optional.empty();
            }

            String topicLevel = topicLevels[topicIndex];

            if (filterLevel.equals("+")) {
                if (topicLevel.isEmpty()) {
                    return Optional.empty();
                }

                plusValues.add(topicLevel);

                topicIndex++;
                continue;
            }

            if (!filterLevel.equals(topicLevel)) {
                return Optional.empty();
            }

            topicIndex++;
        }

        if (topicIndex != topicLevels.length) {
            return Optional.empty();
        }

        return Optional.of(Collections.unmodifiableList(plusValues));
    }

    public static Optional<String> toPlusWildcard(String topic, String filter)
    {
        Optional<List<String>> result = extractPlusValues(topic, filter);

        if (!result.isPresent())
        {
            return Optional.empty();
        }

        List<String> values = result.get();

        if (values.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(values.get(0));
    }
}