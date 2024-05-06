package com.black.n.monkey.mystatemachine.controller;

import com.black.n.monkey.mystatemachine.dto.CreateNewMyProcessRequest;
import com.black.n.monkey.mystatemachine.model.MyProcess;
import com.black.n.monkey.mystatemachine.service.MyProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MyProcessController {

    private final MyProcessService service;

    @PostMapping("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process")
    public ResponseEntity<Map<String,?>> createNew(@PathVariable("tenant_id") String tenantId,
                                                   @PathVariable("workflow_id") UUID workflowId,
                                                   @RequestBody CreateNewMyProcessRequest request) {

        MyProcess o = service.createNew(tenantId, workflowId, request);
        return ResponseEntity.ok(
                Map.of(
                        "uuid",o.getId(),
                        "currentState",o.getStateMachine().currentState()
                )
        );
    }

    @PutMapping("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}/next-state/{next}")
    public ResponseEntity<?> next(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("workflow_id") UUID workflowId,
                                  @PathVariable("my_process_id") UUID myObjectId,
                                  @PathVariable("next") String next) {

        MyProcess o = service.next(tenantId, workflowId, myObjectId, next);
        return ResponseEntity.accepted().build();

    }

    @GetMapping("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}")
    public ResponseEntity<?> find(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("workflow_id") UUID workflowId,
                                  @PathVariable("my_process_id") UUID myProcessId) {

        MyProcess o = service.find(tenantId, workflowId, myProcessId);

        return ResponseEntity.ok(  Map.of(
                "uuid",o.getId(),
                "currentState",o.getStateMachine().currentState()
        ));
    }

    @DeleteMapping("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}")
    public ResponseEntity<?> delete(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("workflow_id") UUID workflowId,
                                  @PathVariable("my_process_id") UUID myProcessId) {

        service.delete(tenantId, workflowId, myProcessId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process")
    public ResponseEntity<?> findAllByTenantIdAndWorkflowId (
            @PathVariable("tenant_id") String tenantId,
            @PathVariable("workflow_id") UUID workflowId,
            @RequestParam(name = "search", defaultValue = "", required = false) String search) {

        return ResponseEntity.ok(service.findAllByTenantIdAndWorkflowId(tenantId, workflowId, search));
    }


}
