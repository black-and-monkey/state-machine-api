package com.black.n.monkey.mystatemachine.dto;

import java.time.Instant;
import java.util.UUID;

public record MyProcessResponse(
        UUID id,
        Instant createdAtUtc,
        String title,
        String body,
        String user,
        String currentState) {
}
