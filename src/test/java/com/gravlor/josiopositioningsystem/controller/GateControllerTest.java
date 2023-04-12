package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.controller.model.GateCreatedResponse;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.GateKey;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = JosioPositioningSystemApplication.class)
@AutoConfigureMockMvc
class GateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private GateRepository gateRepository;

    @Test
    void testAddGateFromErrorValues() throws Exception {
        AddGateRequest addGateRequest = new AddGateRequest(null, "test", 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'from' : Value must not be null")));

        addGateRequest.setFrom("yolo");
        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages", Matchers.contains("Map 'yolo' does not exists")));
    }

    @Test
    void testAddGateToErrorValues() throws Exception {
        AddGateRequest addGateRequest = new AddGateRequest("test", null, 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'to' : Value must not be null")));
    }

    @Test
    void testAddGateHoursLeftNotValid() throws Exception {
        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2", -1, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'hoursLeft' : Value must be positive")));
    }

    @Test
    void testAddGateMinutesLeftNotValid() throws Exception {
        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2", 1, 60);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'minutesLeft' : Value must be between 0 and 59")));

        addGateRequest.setMinutesLeft(-1);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'minutesLeft' : Value must be between 0 and 59")));
    }

    @Test
    void testAddGate() throws Exception {

        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2", 0, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Duration is not valid")));

        addGateRequest.setHoursLeft(1);
        MvcResult mvcResult = mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        GateCreatedResponse objectCreated = TestsUtils.convertJsonToObject(mvcResult.getResponse().getContentAsString(), GateCreatedResponse.class);

        if (objectCreated == null) {
            fail();
        }

        assert "map1".equals(objectCreated.getFrom());
        assert "map2".equals(objectCreated.getTo());

        // clear DB

        Optional<GateEntity> optGateEntity = gateRepository.findById(new GateKey(map1, map2));
        if (optGateEntity.isEmpty()) {
            fail();
        }

        gateRepository.delete(optGateEntity.get());
        assert gateRepository.findAll().size() == 0;

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }

    @Test
    void testAddDuplicateGate() throws Exception {

        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2", 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("A gate already exists from 'map1' to 'map2'")));

        addGateRequest.setFrom("map2");
        addGateRequest.setTo("map1");

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("A gate already exists from 'map2' to 'map1'")));
    }
}
