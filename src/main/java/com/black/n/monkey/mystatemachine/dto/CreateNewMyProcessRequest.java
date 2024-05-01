package com.black.n.monkey.mystatemachine.dto;

public record CreateNewMyProcessRequest (
         String title,
         String body,
         String user) {
}
