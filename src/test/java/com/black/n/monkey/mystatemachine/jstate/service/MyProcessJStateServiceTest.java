package com.black.n.monkey.mystatemachine.jstate.service;

import com.black.n.monkey.mystatemachine.dto.CreateNewMyProcessRequest;
import com.black.n.monkey.mystatemachine.model.MyProcess;
import com.black.n.monkey.mystatemachine.model.workflow.*;
import com.black.n.monkey.mystatemachine.service.WorkflowService;
import com.black.n.monkey.mystatemachine.service.MyProcessService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class MyProcessJStateServiceTest {

    @InjectMocks
    MyProcessService service;

    @Mock
    WorkflowService workflowRepository;

    final static UUID WORKFLOW_ID = UUID.randomUUID();
    final static String TENANT_ID = "tenant_1";

    @Test
    void basic() {
        Mockito.when(workflowRepository.findById(any(),any())).thenReturn(mockWorkflow());
        MyProcess myProcess =  service.createNew(TENANT_ID, WORKFLOW_ID, new CreateNewMyProcessRequest("title","body", "user"));
        service.next(TENANT_ID,WORKFLOW_ID,myProcess.getId(), "B");
    }

    Workflow mockWorkflow () {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        Style defaultStyle = new Style("1px solid black", 10, "lightgray");

        // Creating nodes for each state
        nodes.add(new Node("A", new Data("A"), new Position(0, 0), defaultStyle));
        nodes.add(new Node("B", new Data("B"), new Position(250, 0), defaultStyle));
        nodes.add(new Node("C", new Data("C"), new Position(500, 500), defaultStyle));

        // Creating edges for each transition
        edges.add(new Edge("e1", "A", "B",true));
        edges.add(new Edge("e2", "B", "C",true));

        return new Workflow(WORKFLOW_ID,"a-b-c","A", "C", nodes, edges);
    }
}