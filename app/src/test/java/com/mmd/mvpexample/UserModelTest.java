package com.mmd.mvpexample;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UserModelTest {

    private UserModel user;

    @Before
    public void setUp() {
        user = new UserModel("testUser", "password123");
    }

    @Test
    public void testGetUserName() {
        assertEquals("testUser", user.getUserName());
    }

    @Test
    public void testGetUserPassword() {
        assertEquals("password123", user.getUserPassword());
    }

    @Test
    public void testCheckValidity_emptyUsername() {
        user = new UserModel("", "password123");
        assertEquals(-1, user.checkValidity(user.getUserName(), user.getUserPassword()));
    }

    @Test
    public void testCheckValidity_emptyPassword() {
        user = new UserModel("testUser", "");
        assertEquals(-1, user.checkValidity(user.getUserName(), user.getUserPassword()));
    }

    @Test
    public void testCheckValidity_validInputs() {
        assertEquals(0, user.checkValidity(user.getUserName(), user.getUserPassword()));
    }
}
