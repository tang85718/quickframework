package com.x.dauglas.quickframework;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.x.dauglas.quickframework.extend.ConfigUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    @SmallTest
    public void testConfigToolClass_Normal() {

        ConfigUtils config = ConfigUtils.create(getSystemContext());
        assertNotNull(config);

        ConfigUtils cfg1 = ConfigUtils.getInstance();
        assertNotNull(cfg1);

        ConfigUtils cfg2 = ConfigUtils.getInstance("AppConfig");
        assertNotNull(cfg2);

        // int
        config.config("val1", 100);
        assertEquals(config.getInt("val1"), 100);

        config.config("val1", 200);
        config.config("val1", 201);
        config.config("val1", 202);

        assertEquals(config.getInt("val1"), 202);

        // string
        config.config("val2", "fuck!");
        assertEquals(config.getString("val2"), "fuck!");

        config.config("val2", "s1");
        config.config("val2", "s2");
        config.config("val2", "s3");
        config.config("val2", "s4");
        assertEquals(config.getString("val2"), "s4");

        // float
        config.config("float", 25.123f);
        assertEquals(config.getFloat("float"), 25.123f);

        config.config("float", 123.587f);
        config.config("float", 1253.5112f);
        config.config("float", 4023.8412f);
        config.config("float", 9823.7452f);

        assertEquals(config.getFloat("float"), 9823.7452f);

        // test boolean
        config.config("boolean", true);
        assertEquals(config.getBoolean("boolean"), true);

        config.config("boolean", false);
        config.config("boolean", true);
        config.config("boolean", false);
        config.config("boolean", true);
        assertEquals(config.getBoolean("boolean"), true);

        // TEST Long
        config.config("long", 100L);
        assertEquals(config.getLong("long"), 100L);
        config.config("long", 200L);
        config.config("long", 203L);

        assertEquals(config.getLong("long"), 203L);
    }
}

