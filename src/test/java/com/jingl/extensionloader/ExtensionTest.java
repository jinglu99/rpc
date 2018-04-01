package com.jingl.extensionloader;

import com.jingl.common.exceptions.NoImplClassFoundException;
import com.jingl.common.exceptions.NotInterfaceExcetption;
import com.jingl.common.extension.ExtensionLoader;
import com.jingl.transfer.ExportTransfer;
import com.jingl.transfer.ReferTransfer;
import com.jingl.transfer.Transfer;
import org.junit.Test;

/**
 * Created by Ben on 25/03/2018.
 */
public class ExtensionTest {
    @Test
    public void getExtension() {
        Transfer clazz = null;
        clazz = (Transfer) ExtensionLoader.getExtensionLoder(ReferTransfer.class).newInstance();
        System.out.println(clazz.getClass().getName());
    }
}
