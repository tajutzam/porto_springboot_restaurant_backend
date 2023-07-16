package com.zam.dev.food_order.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationPropertiesTest {


    @Autowired
    private ApplicationProperties properties;

    @Test
    void testGetLocalPort(){
        assertEquals(8081, properties.getPort());
    }

}