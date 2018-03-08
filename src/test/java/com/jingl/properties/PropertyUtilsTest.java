package com.jingl.properties;

import com.jingl.common.Constants;
import com.jingl.utils.PropertyUtils;
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
