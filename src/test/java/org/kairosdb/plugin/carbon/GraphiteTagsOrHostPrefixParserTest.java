package org.kairosdb.plugin.carbon;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphiteTagsOrHostPrefixParserTest
{

	@Test
	public void test_parseMetricWithoutTags_usePrefixAsHostTag()
	{
		TagParser testee = new GraphiteTagsOrHostPrefixParser();

		CarbonMetric result = testee.parseMetricName("foo.bar.blabla.cpu");

		assertEquals("name",result.getName(),"bar.blabla.cpu");
		assertEquals("tags",result.getTags(), ImmutableMap.builder().put("host","foo").build());
	}

	@Test
	public void test_parseMetricWith1Tag_tagsAppended()
	{
		TagParser testee = new GraphiteTagsOrHostPrefixParser();

		CarbonMetric result = testee.parseMetricName("foo.bar.blabla.cpu;host=somehost.domain.com");

		assertEquals("name",result.getName(),"foo.bar.blabla.cpu");
		assertEquals("tags",result.getTags(), ImmutableMap.builder().put("host","somehost.domain.com").build());
	}

	@Test
	public void test_parseMetricWith2Tags_tagsAppended()
	{
		TagParser testee = new GraphiteTagsOrHostPrefixParser();

		CarbonMetric result = testee.parseMetricName("foo.bar.blabla.cpu;host=somehost.domain.com;cpuid=0");

		assertEquals("name",result.getName(),"foo.bar.blabla.cpu");
		assertEquals("tags",result.getTags(), ImmutableMap.builder().put("host","somehost.domain.com").put("cpuid","0").build());
	}
}
