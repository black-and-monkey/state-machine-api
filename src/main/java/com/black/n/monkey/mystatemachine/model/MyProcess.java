package com.black.n.monkey.mystatemachine.model;

import lombok.Builder;
import lombok.Getter;
import unquietcode.tools.esm.StringStateMachine;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class MyProcess {

    private final UUID id;
    private final StringStateMachine stateMachine;
    private final Instant createdAtUtc;
    private Instant updatedAtUtc;
    private String title;
    private String body;
    private String user; // TODO change a list of users / like assign, related, reporter, etc.
    private String tenantId;
    private UUID workflowId;


}
