package cloud.developing.router;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * @author asmolnik
 *
 */
public class Ses2SnsRouter {

	public void route(String event, Context context) {
		LambdaLogger log = context.getLogger();
		log.log(event);
	}

}
