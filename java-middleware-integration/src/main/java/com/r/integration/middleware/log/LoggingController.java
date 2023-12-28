package com.r.integration.middleware.log;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LoggingController {

    private Log log = LogFactory.getLog(LoggingController.class);
    @GetMapping
    public String log(){
        log.trace("this is a Trace level message");
        log.debug("this is a debug level message");
        log.info("this is a info level message");
        log.warn("this is a warn level message");
        log.error("this is a error level message");
        return "See the log for details";
    }
}
