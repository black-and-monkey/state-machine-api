package com.black.n.monkey.mystatemachine.jstate.controller;

import com.black.n.monkey.mystatemachine.state.StateValueEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MyProcessJStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String TENANT_ID = UUID.randomUUID().toString();
    private final static String WORKFLOW_ID = UUID.randomUUID().toString();

 //   @Test // FIXME
    public void happyFlow() throws Exception {

        MvcResult createNewResult = this.mockMvc.perform(
                        post("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/create-my-process", TENANT_ID, WORKFLOW_ID))
                .andExpect(status().isOk())
                .andReturn();
        Map<String,String> responseBody = objectMapper.readValue(createNewResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Assertions.assertNotNull(responseBody.get("uuid"));
        Assertions.assertEquals(StateValueEnum.Ready.name(),responseBody.get("currentState"));

        MvcResult createdResult = this.mockMvc.perform(
                        get("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid")))
                .andExpect(status().isOk())
                .andReturn();

        responseBody = objectMapper.readValue(createdResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Assertions.assertEquals(StateValueEnum.Ready.name(),responseBody.get("currentState"));

        this.mockMvc.perform(
                        put("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}/next-state/{next}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid"),StateValueEnum.Running))
                .andExpect(status().isAccepted())
                .andReturn();

        this.mockMvc.perform(
                        put("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}/next-state/{next}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid"),StateValueEnum.Stopping))
                .andExpect(status().isAccepted())
                .andReturn();

        this.mockMvc.perform(
                        put("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}/next-state/{next}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid"),StateValueEnum.Stopped))
                .andExpect(status().isAccepted())
                .andReturn();

        this.mockMvc.perform(
                        put("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}/next-state/{next}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid"),StateValueEnum.Finished))
                .andExpect(status().isAccepted())
                .andReturn();

        MvcResult finishedResult = this.mockMvc.perform(
                        get("/v-jstate/tenant/{tenant_id}/workflow/{workflow_id}/my-process/{my_process_id}",
                                TENANT_ID, WORKFLOW_ID,responseBody.get("uuid")))
                .andExpect(status().isOk())
                .andReturn();

        responseBody = objectMapper.readValue(finishedResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Assertions.assertEquals(StateValueEnum.Finished.name(),responseBody.get("currentState"));

    }
}