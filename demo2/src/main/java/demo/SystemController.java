package demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SystemController.BASE_URL)
public class SystemController {
	private static final Log logger = LogFactory.getLog(SystemController.class);
	public static final String PING = "/ping";
	public static final String BASE_URL = "/system";

	@Autowired
	ApplicationContext appCtx;

	@RequestMapping(method = RequestMethod.GET, value = PING)
	public @ResponseBody
	String ping() {
		logger.debug("In ping");
		return "pong";
	}
}
