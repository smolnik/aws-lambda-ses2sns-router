package cloud.developing.router;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * @author asmolnik
 *
 */
public class Ses2SnsRouter {

	private final AmazonSNS sns = new AmazonSNSClient();

	public void route(Map<String, Object> eventMap, Context context) {
		LambdaLogger log = context.getLogger();
		List<Map<String, Object>> records = getListAsValue("Records", eventMap);
		log.log(eventMap.toString());
		records.forEach(m -> {
			String source = getValue("source", getMap(m, "ses", "mail"));
			ZonedDateTime now = Instant.now().atZone(ZoneId.systemDefault());
			String message = "Mail has just been received from: " + source + ", on " + now.toLocalDate() + " at " + now.toLocalTime();
			String targetTopicArn = System.getenv("targetTopicArn");
			sns.publish(targetTopicArn, message);
			log.log("message \"" + message + "\" has been published to " + targetTopicArn);
		});
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getMap(Map<String, Object> map, String... keys) {
		for (String key : keys) {
			map = Map.class.cast(map.get(key));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getListAsValue(String key, Map<String, Object> map) {
		return List.class.cast(map.get(key));
	}

	private String getValue(String key, Map<String, Object> map) {
		return String.class.cast(map.get(key));
	}

}
