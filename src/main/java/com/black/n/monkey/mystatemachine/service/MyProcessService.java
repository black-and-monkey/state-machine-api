package com.black.n.monkey.mystatemachine.service;

import com.black.n.monkey.mystatemachine.dto.CreateNewMyProcessRequest;
import com.black.n.monkey.mystatemachine.dto.MyProcessResponse;
import com.black.n.monkey.mystatemachine.model.MyProcess;
import com.black.n.monkey.mystatemachine.model.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import unquietcode.tools.esm.StringStateMachine;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyProcessService {

    private final WorkflowService workflowRepository;
    private final Map<UUID, MyProcess> myPersistence = new HashMap<>();
    public MyProcess createNew(String tenantId, UUID workflowId, CreateNewMyProcessRequest request) {

        MyProcess o = MyProcess.builder()
                .id(UUID.randomUUID())
                .body(request.summary())
                .title(request.title())
                .user(request.user())
                .stateMachine(build(tenantId,workflowId))
                .createdAtUtc(Instant.now(Clock.systemUTC()))
                .workflowId(workflowId)
                .tenantId(tenantId)
                .build();

        myPersistence.put(o.getId(),o);
        return o;
    }

    public MyProcess next(String tenantId, UUID workflowId, UUID myObjectId, String next) {
        if (!myPersistence.containsKey(myObjectId)) {
            throw new RuntimeException("object not found, workflowId: " + myObjectId);
        }

        MyProcess o = myPersistence.get(myObjectId);
        o.getStateMachine().transition(next);
        return o;
    }

    protected StringStateMachine build(String tenantId, UUID workflowId) {

        Workflow workflow = workflowRepository.findById(tenantId, workflowId);

        StringStateMachine esm = new StringStateMachine(workflow.firstNode());

        workflow.edges().forEach(
                x -> esm.addTransition(x.source(),x.target())
        );
        esm.addTransition(workflow.lastNode(),null);

        // https://github.com/UnquietCode/JState?tab=readme-ov-file
//        StringStateMachine esm = new StringStateMachine(StateValueEnum.Ready.name());
//        esm.addTransitions(StateValueEnum.Ready, StateValueEnum.Running, StateValueEnum.Finished);
//        esm.addTransitions(StateValueEnum.Running, StateValueEnum.Paused, StateValueEnum.Stopping);
//        esm.addTransitions(StateValueEnum.Paused, StateValueEnum.Running, StateValueEnum.Stopping);
//        esm.addTransitions(StateValueEnum.Stopping, StateValueEnum.Stopped);
//        esm.addTransitions(StateValueEnum.Stopped, StateValueEnum.Finished);
//        esm.addTransitions(StateValueEnum.Finished, StateValueEnum.Ready, null);
        return esm;
    }

    public MyProcess find(String tenantId, UUID workflowId, UUID myObjectId) {
        if (!myPersistence.containsKey(myObjectId)) {
            throw new RuntimeException("object not found, workflowId: " + myObjectId);
        }
        return myPersistence.get(myObjectId);
    }

    public List<MyProcessResponse> findAllByTenantIdAndWorkflowId(String tenantId, UUID workflowId, String search) {

        return myPersistence.values().stream()
                .filter(myProcess -> myProcess.getTenantId().equals(tenantId) && myProcess.getWorkflowId().equals(workflowId))
                .filter(myProcess ->
                        StringUtils.isEmpty(search) || StringUtils.containsAnyIgnoreCase(myProcess.getBody(), search) ||
                                StringUtils.containsAnyIgnoreCase(myProcess.getTitle(), search)
                )
                .map(
                        process -> new MyProcessResponse(
                                process.getId(),
                                process.getCreatedAtUtc(),
                                process.getTitle(),
                                process.getBody(),
                                process.getUser(),
                                process.getStateMachine().currentState()))
                .sorted(Comparator.comparing(MyProcessResponse::createdAtUtc).reversed())
                .collect(Collectors.toList());
    }
}
