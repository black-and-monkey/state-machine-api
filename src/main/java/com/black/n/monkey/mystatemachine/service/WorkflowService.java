package com.black.n.monkey.mystatemachine.service;


import com.black.n.monkey.mystatemachine.model.workflow.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final Map<String,Map<UUID,Workflow>> repository = new HashMap<>();


    public void addWorkflow (String tenantId, Workflow workflow) {
        if (!repository.containsKey(tenantId)) {
            // TODO need to check if tenant exist
            var workflowMap = new HashMap<UUID,Workflow>();
            workflowMap.put(workflow.workflowId(),workflow);
            repository.put(tenantId,workflowMap );
            return;
        }

        repository.get(tenantId).put(workflow.workflowId(),workflow);

    }

    public Workflow findById(String tenantId, UUID workflowId) {
       checkTenant(tenantId);
        return repository.get(tenantId).get(workflowId);
    }


    public Collection<Workflow> findAllByTenant(String tenantId) {
        checkTenant(tenantId);
        return repository.get(tenantId).values();
    }

    void checkTenant (String tenantId) {
        if (!repository.containsKey(tenantId)) {
            throw new RuntimeException("tenant doesn't exist "+tenantId);
        }
    }

}


