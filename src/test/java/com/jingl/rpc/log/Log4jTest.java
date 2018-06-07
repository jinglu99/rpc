package com.jingl.rpc.log;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by Ben on 13/02/2018.
 */
public class Log4jTest {
    Logger logger = Logger.getLogger(Log4jTest.class);
    @Test
    public void test() {
        logger.warn("this is warn");
        logger.info("this is info");
        logger.error("this is error");
        logger.debug("this is debug");
    }
}
