package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestDtoTest {

    @Test
    void shouldSetAndGetFields() {
        LoginRequestDto dto = new LoginRequestDto();

        dto.setUsername("user1");
        dto.setPassword("Pass111111");

        assertEquals("user1", dto.getUsername());
        assertEquals("Pass111111", dto.getPassword());
    }
}