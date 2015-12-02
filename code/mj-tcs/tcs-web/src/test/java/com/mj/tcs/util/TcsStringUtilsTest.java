package com.mj.tcs.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by xiaobai on 2015/8/23.
 */
public class TcsStringUtilsTest {

    @Test
    public void testToUnderlineString() throws Exception {
        assertEquals("iso_certified_staff", TcsStringUtils.toUnderlineString("ISOCertifiedStaff"));
    }

    @Test
    public void testToCamelCaseString() throws Exception {
        assertEquals("IsoCertifiedStaff", TcsStringUtils.toCamelCaseString("iso_certified_staff", true));
        assertEquals("isoCertifiedStaff", TcsStringUtils.toCamelCaseString("iso_certified_staff", false));

        assertEquals("siteId", TcsStringUtils.toCamelCaseString("site_Id"));
    }

    @Test
    public void testGetValidPropertyName() throws Exception {
        assertEquals("certifiedStaff", TcsStringUtils.getValidPropertyName("CertifiedStaff"));
        assertEquals("certified_Staff", TcsStringUtils.getValidPropertyName("Certified_Staff"));
    }

    @Test
    public void testGetSetterMethodName() throws Exception {
        assertEquals("setUserID",TcsStringUtils.getSetterMethodName("userID"));
    }

    @Test
    public void testGetGetterMethodName() throws Exception {
        assertEquals("getUserID", TcsStringUtils.getGetterMethodName("userID"));
    }
}