package com.zam.dev.food_order.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BCryptTest {

    @Test
    void testHashPw() {
        String pw = "rahasia";
        String hashPw = BCrypt.hashpw(pw, BCrypt.gensalt());
        assertNotNull(hashPw);
    }

    @Test
    void testCheckPw() {
        String pw = "rahasia";
        String hashPw = BCrypt.hashpw(pw, BCrypt.gensalt());
        assertNotNull(hashPw);
        assertTrue( BCrypt.checkpw(pw , hashPw));
    }
}