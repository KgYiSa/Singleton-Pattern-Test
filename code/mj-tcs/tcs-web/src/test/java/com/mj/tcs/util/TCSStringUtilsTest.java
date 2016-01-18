package com.mj.tcs.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by xiaobai on 2015/8/23.
 */
public class TCSStringUtilsTest {

    @Test
    public void testToUnderlineString() throws Exception {
        assertEquals("iso_certified_staff", TCSStringUtils.toUnderlineString("ISOCertifiedStaff"));
    }

    @Test
    public void testToCamelCaseString() throws Exception {
        assertEquals("IsoCertifiedStaff", TCSStringUtils.toCamelCaseString("iso_certified_staff", true));
        assertEquals("isoCertifiedStaff", TCSStringUtils.toCamelCaseString("iso_certified_staff", false));

        assertEquals("siteId", TCSStringUtils.toCamelCaseString("site_Id"));
    }

    @Test
    public void testGetValidPropertyName() throws Exception {
        assertEquals("certifiedStaff", TCSStringUtils.getValidPropertyName("CertifiedStaff"));
        assertEquals("certified_Staff", TCSStringUtils.getValidPropertyName("Certified_Staff"));
    }

    @Test
    public void testGetSetterMethodName() throws Exception {
        assertEquals("setUserID", TCSStringUtils.getSetterMethodName("userID"));
    }

    @Test
    public void testGetGetterMethodName() throws Exception {
        assertEquals("getUserID", TCSStringUtils.getGetterMethodName("userID"));
    }
}