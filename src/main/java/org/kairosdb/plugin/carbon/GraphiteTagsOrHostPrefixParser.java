package org.kairosdb.plugin.carbon;

import com.google.inject.Inject;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use graphite metrics (@see GraphiteTagsParser) but if no tags are available treat first token as host tag.
 */
public class GraphiteTagsOrHostPrefixParser implements TagParser {
  private static final Logger logger = LoggerFactory.getLogger(GraphiteTagsOrHostPrefixParser.class);
  private static Pattern hostNamePattern = Pattern.compile("^([\\w-]*)\\.(.*)$");
  private TagParser graphiteTagsParser = new GraphiteTagsParser();

  @Override
  public CarbonMetric parseMetricName(String metricName) {
    if(metricName.contains(";")){
      return graphiteTagsParser.parseMetricName(metricName);
    }
    else {
      Matcher m = hostNamePattern.matcher(metricName);
      if(m.matches()){
        CarbonMetric metric = new CarbonMetric(m.group(2));
        metric.addTag("host", m.group(1));
        return metric;
      }
      else {
        logger.warn("unparseable metric {} - ignoring metric", metricName);
        return null;
      }
    }
  }
}