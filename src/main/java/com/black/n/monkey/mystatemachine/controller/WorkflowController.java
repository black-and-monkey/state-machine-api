package com.black.n.monkey.mystatemachine.controller;

import com.black.n.monkey.mystatemachine.model.workflow.Workflow;
import com.black.n.monkey.mystatemachine.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowRepository;

    @GetMapping(value = "/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}")
    public ResponseEntity<Workflow> findByTenantIdAndWorkflowId(
            @PathVariable("tenant_id") String tenantId,
            @PathVariable("workflow_id") UUID workflowId) {

        return ResponseEntity.ok(
                workflowRepository.findById(tenantId, workflowId)
        );
    }

    @GetMapping(value = "/v-jstate/tenant/{tenant_id}/workflow")
    public ResponseEntity<Collection<Workflow>>findAllByTenant(
            @PathVariable("tenant_id") String tenantId) {

        return ResponseEntity.ok(
                workflowRepository.findAllByTenant(tenantId)
        );
    }

}
