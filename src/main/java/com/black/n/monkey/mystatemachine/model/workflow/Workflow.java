package com.black.n.monkey.mystatemachine.model.workflow;

import java.util.List;
import java.util.UUID;

public record Workflow(UUID workflowId, String name, String firstNode, String lastNode, List<Node> nodes, List<Edge> edges) {

}


//const nodeStyles = { border: '1px solid #777', padding: 10, backgroundColor: 'white' };
//// Define the nodes based on the states
//const nodes = [
//  { workflowId: 'STATE_DONE', data: { label: 'STATE_DONE' }, position: { x: 250, y: 5 }, style: nodeStyles },
//  { workflowId: 'STATE_IN_DEVELOP', data: { label: 'STATE_IN_DEVELOP' }, position: { x: 100, y: 100 }, style: nodeStyles },
//  { workflowId: 'STATE_NEW', data: { label: 'STATE_NEW' }, position: { x: 400, y: 100 }, style: nodeStyles },
//  { workflowId: 'STATE_IN_QA', data: { label: 'STATE_IN_QA' }, position: { x: 250, y: 200 }, style: nodeStyles },
//];
//
//// Define the edges based on the transitions
//const edges = [
//  { workflowId: 'e1', source: 'STATE_DONE', target: 'STATE_NEW', animated: false },
//  { workflowId: 'e2', source: 'STATE_IN_DEVELOP', target: 'STATE_DONE', animated: true },
//  { workflowId: 'e3', source: 'STATE_IN_DEVELOP', target: 'STATE_IN_QA', animated: true },
//  { workflowId: 'e4', source: 'STATE_NEW', target: 'STATE_DONE', animated: true },
//  { workflowId: 'e5', source: 'STATE_NEW', target: 'STATE_IN_DEVELOP', animated: true },
//  { workflowId: 'e6', source: 'STATE_IN_QA', target: 'STATE_DONE', animated: true },
//  { workflowId: 'e7', source: 'STATE_IN_QA', target: 'STATE_IN_DEVELOP', animated: true },
//];