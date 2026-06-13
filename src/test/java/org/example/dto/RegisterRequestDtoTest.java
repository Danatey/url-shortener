package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestDtoTest {

    @Test
    void shouldSetAndGetFields() {
        RegisterRequestDto dto = new RegisterRequestDto();

        dto.setUsername("user1");
        dto.setPassword("Password123");

        assertEquals("user1", dto.getUsername());
        assertEquals("Password123", dto.getPassword());
    }
}