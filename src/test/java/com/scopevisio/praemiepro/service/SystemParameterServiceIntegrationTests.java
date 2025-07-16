package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.SystemParameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SystemParameterServiceIntegrationTests {

    @Autowired
    private SystemParameterService systemParameterService;

    @Test
    void testGetSystemParameter() {
        // Act
        final SystemParameter systemParameter = systemParameterService.getSystemParameter();

        // Assert
        assertNotNull(systemParameter);
        assertEquals(1, systemParameter.getId());
    }
}
