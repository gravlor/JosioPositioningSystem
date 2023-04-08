package com.gravlor.josiopositioningsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = JosioPositioningSystemApplication.class)
@AutoConfigureMockMvc
public class GateControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private GateRepository gateRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddGate() throws Exception {

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2", 0, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mapRepository.save(new MapEntity("map1", MapType.BLUE));
        mapRepository.save(new MapEntity("map2", MapType.BLUE));


        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        addGateRequest.setHoursLeft(1);
        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<GateEntity> allGates = gateRepository.findAll();
        assert allGates.size() > 0;
    }
}
