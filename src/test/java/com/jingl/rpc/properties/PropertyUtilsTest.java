package com.jingl.rpc.properties;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.utils.PropertyUtils;
import org.junit.Test;

/**
 * Created by Ben on 07/03/2018.
 */
public class PropertyUtilsTest {

    @Test
    public void testGetProperty() {
        System.out.println(PropertyUtils.getProperty("rpc.name"));
    }

    @Test
    public void testChangeDefaultPath() {
        System.setProperty(Constants.PROPERTY_FILE_NAME, "/Resources/rpc1.properties");

        System.out.println(PropertyUtils.getProperty("rpc.name"));
    }
}
