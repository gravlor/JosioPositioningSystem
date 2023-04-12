package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.GateAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.exception.SameMapForGateException;
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

        MapEntity map1 = mapService.createNewMap("map1", MapType.BLUE);
        MapEntity map2 = mapService.createNewMap("map2", MapType.BLUE);
        GateEntity gate = gateService.createNewGate("map1", "map2");

        AddGateRequest addGateRequest = new AddGateRequest("map1", "map2");

        mvc.perform(post(Constants.PATH_API_MAPPING)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        gateRepository.delete(gate);
        assert gateRepository.findAll().size() == 0;

        mapRepository.delete(map1);
        mapRepository.delete(map2);
        assert mapRepository.findAll().size() == 0;
    }

    @Test
    void testStackOverFlow() throws Exception {

        int nb = 1000;

        createBigLine(nb);

        AddGateRequest addGateRequest = new AddGateRequest("map0", "map" + (nb - 1));

        mvc.perform(post(Constants.PATH_API_MAPPING)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        gateRepository.deleteAll();
        mapRepository.deleteAll();
    }

    void createBigLine(int nb) throws MapAlreadyExistsException, MapNotFoundException, GateAlreadyExistsException, SameMapForGateException {
        int i = 0;
        while (i < nb) {
            mapService.createNewMap("map" + i, MapType.BLUE);
            i++;
        }

        i = 1;
        while (i < nb - 1) {
            gateService.createNewGate("map" + (i - 1), "map" + i);
            i++;
        }
    }
}
