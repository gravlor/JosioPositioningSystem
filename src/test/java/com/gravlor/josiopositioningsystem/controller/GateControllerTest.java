package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddAvalonGateRequest;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
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
    void testAddAvalonGateFromErrorValues() throws Exception {
        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest(null, "test", 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'from' : Value must not be null")));

        addGateRequest.setFrom("yolo");
        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages", Matchers.contains("Map 'yolo' does not exists")));
    }

    @Test
    void testAddAvalonGateToErrorValues() throws Exception {
        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest("test", null, 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'to' : Value must not be null")));
    }

    @Test
    void testAddAvalonGateHoursLeftNotValid() throws Exception {
        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest("map1", "map2", -1, 0);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'hoursLeft' : Value must be positive")));
    }

    @Test
    void testAddAvalonGateMinutesLeftNotValid() throws Exception {
        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest("map1", "map2", 1, 60);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'minutesLeft' : Value must be between 0 and 59")));

        addGateRequest.setMinutesLeft(-1);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'minutesLeft' : Value must be between 0 and 59")));
    }

    @Test
    void testAddAvalonGate() throws Exception {
        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest("map1", "map2", 0, 0);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Duration is not valid")));

        addGateRequest.setHoursLeft(1);
        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<GateEntity> optGateEntity = gateRepository.findById(new GateKey(map1, map2));
        if (optGateEntity.isEmpty()) {
            fail();
        }

        GateEntity gateEntity = optGateEntity.get();

        assert "map1".equals(gateEntity.getKey().getFrom().getName());
        assert "map2".equals(gateEntity.getKey().getTo().getName());

        gateRepository.delete(gateEntity);
        assert gateRepository.findAll().size() == 0;

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }

    @Test
    void testAddAvalonGateNumValues() throws Exception {
        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(getAddAvalonGateNumValuesRequest())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Invalid request body")));

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }

    private String getAddAvalonGateNumValuesRequest() {
        return "{" +
                "    \"from\" : \"map1\"," +
                "    \"to\" : \"map2\"," +
                "    \"hoursLeft\" : 1.5," +
                "    \"minutesLeft\" : 0" +
                "}";
    }

    @Test
    void testAddAvalonGateDuplicate() throws Exception {
        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        AddAvalonGateRequest addGateRequest = new AddAvalonGateRequest("map1", "map2", 1, 0);

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<GateEntity> gateCreated = gateRepository.findById(new GateKey(map1, map2));
        if (gateCreated.isEmpty()) {
            fail();
        }

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("A gate already exists from 'map1' to 'map2'")));

        addGateRequest.setFrom("map2");
        addGateRequest.setTo("map1");

        mvc.perform(post(Constants.PATH_API_GATE_AVALON)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("A gate already exists from 'map2' to 'map1'")));

        gateRepository.delete(gateCreated.get());
        assert gateRepository.findAll().size() == 0;

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }

    @Test
    void testAddGate() throws Exception {
        MapEntity map1 = new MapEntity("map1", MapType.BLUE);
        MapEntity map2 = new MapEntity("map2", MapType.BLUE);
        mapRepository.save(map1);
        mapRepository.save(map2);

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2");

        mvc.perform(post(Constants.PATH_API_GATE)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<GateEntity> optGateEntity = gateRepository.findById(new GateKey(map1, map2));
        if (optGateEntity.isEmpty()) {
            fail();
        }

        GateEntity gateEntity = optGateEntity.get();

        assert "map1".equals(gateEntity.getKey().getFrom().getName());
        assert "map2".equals(gateEntity.getKey().getTo().getName());

        gateRepository.delete(gateEntity);
        assert gateRepository.findAll().size() == 0;

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }
}
