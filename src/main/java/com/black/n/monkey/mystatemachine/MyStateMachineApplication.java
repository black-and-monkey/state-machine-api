package com.black.n.monkey.mystatemachine;

import com.black.n.monkey.mystatemachine.dto.CreateNewMyProcessRequest;
import com.black.n.monkey.mystatemachine.model.MyProcess;
import com.black.n.monkey.mystatemachine.model.Tenant;
import com.black.n.monkey.mystatemachine.model.workflow.*;
import com.black.n.monkey.mystatemachine.service.WorkflowService;
import com.black.n.monkey.mystatemachine.service.MyProcessService;
import com.black.n.monkey.mystatemachine.service.TenantService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@SpringBootApplication
public class MyStateMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyStateMachineApplication.class, args);
	}

}

@Component
@RequiredArgsConstructor
class ZMyStartUpData {

	public static final String GESTION_SOLIDAR = "gestion_solidar";
	
	private final MyProcessService myProcessService;
	private final TenantService tenantService;
	private final WorkflowService workflowService;

	private final UUID OS_PROCESS_WORKFLOW_ID = UUID.randomUUID();
	private final UUID GESTION_SOLIDAR_FALTAS_WORKFLOW_ID = UUID.randomUUID();

	Faker faker = new Faker();;
	@EventListener(ApplicationReadyEvent.class)
	public void initMyTestData() {

		var tenants = mockTenants();
		tenants.forEach(tenantService::addTenant);

		initWorkflows();

		tenants.forEach( tenant -> initProcess(tenant.getTenantId(),OS_PROCESS_WORKFLOW_ID));
	}

	Set<Tenant> mockTenants () {
		var tenants = new HashSet<Tenant>();

		for (int i=0; i< 1; i++) {
			String name = faker.company().name();
			tenants.add(Tenant.builder()
					.tenantId(name.toLowerCase()
							.replace(" ","_")
							.replace(",","")
							.replace("-","_")
					)
					.name(name)
					.build());
		}
		tenants.add(Tenant.builder().name("Gestion Solidar").tenantId(GESTION_SOLIDAR).build());
		return tenants;
	}


	void initWorkflows () {
		var osProcessWorkflow = buildWorkflowProcessStates();
		tenantService.findTenants().forEach( x-> workflowService.addWorkflow(x.getTenantId(), osProcessWorkflow));
		workflowService.addWorkflow(GESTION_SOLIDAR, buildWorkflowSolidarPersonalFaltas());
	}

	void initProcess(String tenantId, UUID workflowId) {
		for (int i=0 ; i< 15 ; i++) {

			CreateNewMyProcessRequest newProcess = null;

			if (tenantId.equals(GESTION_SOLIDAR)) {

				if (workflowId.equals(OS_PROCESS_WORKFLOW_ID)) {
					var backToTheFuture = faker.backToTheFuture();
					newProcess = new CreateNewMyProcessRequest(
							"BACK TO THE FUTURE: " + backToTheFuture.date(),
							backToTheFuture.quote(),
							backToTheFuture.character());
				} else {
					var lebowski = faker.lebowski();
					newProcess = new CreateNewMyProcessRequest(
							"LEBOWSKI : " + lebowski.actor(),
							lebowski.quote(),
							lebowski.character());
				}

			} else {

				if (workflowId.equals(OS_PROCESS_WORKFLOW_ID)) {
					newProcess = new CreateNewMyProcessRequest(
							"CHUCK NORRIS FACTS: " + faker.lorem().character(),
							faker.chuckNorris().fact(),
							"Chuk Norris");
				} else {
					var rickAndMorty = faker.rickAndMorty();
					newProcess = new CreateNewMyProcessRequest(
							"Rick and Morty: " + rickAndMorty.location(),
							rickAndMorty.quote(),
							rickAndMorty.character());
				}
			}

			MyProcess o = myProcessService.createNew(tenantId, workflowId, newProcess);

			if (i % 2 ==0 )
				myProcessService.next(tenantId, workflowId, o.getId(),"running");

		}
	}

	Workflow buildWorkflowProcessStates() {
		List<Node> nodes = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();

		Style defaultStyle = new Style("1px solid black", 10, "lightgray");

		// Creating nodes for each state
		nodes.add(new Node("ready", new Data("ready"), new Position(0, 0), defaultStyle));
		nodes.add(new Node("running", new Data("running"), new Position(250, 0), defaultStyle));
		nodes.add(new Node("paused", new Data("paused"), new Position(500, 0), defaultStyle));
		nodes.add(new Node("stopping", new Data("stopping"), new Position(250, 250), defaultStyle));
		nodes.add(new Node("stopped", new Data("stopped"), new Position(0, 500), defaultStyle));
		nodes.add(new Node("finished", new Data("finished"), new Position(500, 500), defaultStyle));

		// Creating edges for each transition
		edges.add(new Edge("e1", "ready", "running",true));
		edges.add(new Edge("e2", "ready", "finished",true));
		edges.add(new Edge("e3", "running", "paused",true));
		edges.add(new Edge("e4", "running", "stopping",true));
		edges.add(new Edge("e5", "paused", "running",true));
		edges.add(new Edge("e6", "paused", "stopping",true));
		edges.add(new Edge("e7", "stopping", "stopped",true));
		edges.add(new Edge("e8", "stopped", "finished",true));

		return new Workflow(OS_PROCESS_WORKFLOW_ID, "OS process workflow", "ready", "finished", nodes, edges);
	}

	//Notificado
	//Solucionado
	//Sin Solución
	//Vencido
	Workflow buildWorkflowSolidarPersonalFaltas () {
		List<Node> nodes = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();

		Style defaultStyle = new Style("1px solid black", 10, "lightgray");

		// Creating nodes for each state
		nodes.add(new Node("notificado", new Data("notificado"), new Position(0, 0), defaultStyle));
		nodes.add(new Node("solucionado", new Data("solucionado"), new Position(250, 0), defaultStyle));
		nodes.add(new Node("sin_solucion", new Data("Sin Solución"), new Position(250, 250), defaultStyle));
		nodes.add(new Node("vencido", new Data("vencido"), new Position(0, 500), defaultStyle));
		nodes.add(new Node("cerrado", new Data("cerrado"), new Position(0, 500), defaultStyle));

		// Creating edges for each transition
		edges.add(new Edge("e1", "notificado", "solucionado",true));
		edges.add(new Edge("e2", "notificado", "sin_solucion",true));
		edges.add(new Edge("e3", "notificado", "vencido",true));

		edges.add(new Edge("e4", "notificado", "cerrado",true));
		edges.add(new Edge("e5", "solucionado", "cerrado",true));
		edges.add(new Edge("e6", "sin_solucion", "cerrado",true));
		edges.add(new Edge("e7", "vencido", "cerrado",true));

		return new  Workflow(GESTION_SOLIDAR_FALTAS_WORKFLOW_ID, "Faltas Personal Workflow", "notificado", "cerrado", nodes, edges);
	}

}