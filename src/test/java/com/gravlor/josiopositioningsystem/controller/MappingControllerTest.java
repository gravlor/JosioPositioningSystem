package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import com.gravlor.josiopositioningsystem.service.GateService;
import com.gravlor.josiopositioningsystem.service.MapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = JosioPositioningSystemApplication.class)
@AutoConfigureMockMvc
class MappingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private MapService mapService;

    @Autowired
    private GateService gateService;

    @Test
    void basicTestMapping() throws Exception {

        mapService.createNewMap("map1", MapType.BLUE);
        mapService.createNewMap("map2", MapType.BLUE);
        gateService.createNewGate("map1", "map2");

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2");

        mvc.perform(post(Constants.PATH_API_MAPPING)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
