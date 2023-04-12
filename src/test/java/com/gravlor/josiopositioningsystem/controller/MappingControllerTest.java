package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.*;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import com.gravlor.josiopositioningsystem.service.GateService;
import com.gravlor.josiopositioningsystem.service.MapService;
import com.gravlor.josiopositioningsystem.service.MappingService;
import com.gravlor.josiopositioningsystem.service.model.Node;
import com.gravlor.josiopositioningsystem.service.model.Tree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
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

    @Autowired
    private MappingService mappingService;

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
    void testExploreTree() throws MapAlreadyExistsException, MapNotFoundException, GateAlreadyExistsException, SameMapForGateException, UnknowNodeException {

        createLine(4);
        Optional<MapEntity> optStart = mapRepository.findByName("map0");
        if (optStart.isEmpty()) {
            fail();
        }

        Optional<MapEntity> optEnd = mapRepository.findByName("map3");
        if (optEnd.isEmpty()) {
            fail();
        }

        MapEntity start = optStart.get();
        MapEntity end = optEnd.get();

        List<MapEntity> allMaps = mapService.findAllMaps();
        List<GateEntity> allGates = gateService.findAllGates();

        Tree tree = mappingService.buildTree(allMaps, allGates);
        mappingService.exploreTree(tree, start, end);

        tree.getAllNodes().forEach(node -> {
            if (node.getDistance() == null) {
                fail();
            }
        });

        List<Node> result = mappingService.getResult(tree, start, end);
        if (result == null || result.size() == 0) {
            fail();
        }

        gateRepository.deleteAll();
        mapRepository.deleteAll();
    }

    @Test
    void testSimpleMapWithTwoPath() throws MapAlreadyExistsException, MapNotFoundException, GateAlreadyExistsException, SameMapForGateException, UnknowNodeException {
        createLine(4);
        Optional<MapEntity> optStart = mapRepository.findByName("map0");
        if (optStart.isEmpty()) {
            fail();
        }

        Optional<MapEntity> optEnd = mapRepository.findByName("map3");
        if (optEnd.isEmpty()) {
            fail();
        }

        mapService.createNewMap("map4", MapType.BLUE);
        gateService.createNewGate("map0", "map4");
        gateService.createNewGate("map4", "map1");

        MapEntity start = optStart.get();
        MapEntity end = optEnd.get();

        List<MapEntity> allMaps = mapService.findAllMaps();
        List<GateEntity> allGates = gateService.findAllGates();

        Tree tree = mappingService.buildTree(allMaps, allGates);
        mappingService.exploreTree(tree, start, end);

        tree.getAllNodes().forEach(node -> {
            if (node.getDistance() == null) {
                fail();
            }
        });

        List<Node> result = mappingService.getResult(tree, start, end);
        if (result == null || result.size() != 4) {
            fail();
        }

        gateRepository.deleteAll();
        mapRepository.deleteAll();
    }

    @Test
    void testStackOverFlow() throws Exception {

        int nb = 1000;

        createLine(nb);

        AddGateRequest addGateRequest = new AddGateRequest("map0", "map" + (nb - 1));

        mvc.perform(post(Constants.PATH_API_MAPPING)
                        .content(TestsUtils.asJsonString(addGateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        gateRepository.deleteAll();
        mapRepository.deleteAll();
    }

    void createLine(int nb) throws MapAlreadyExistsException, MapNotFoundException, GateAlreadyExistsException, SameMapForGateException {
        int i = 0;
        while (i < nb) {
            mapService.createNewMap("map" + i, MapType.BLUE);
            i++;
        }

        i = 1;
        while (i < nb) {
            gateService.createNewGate("map" + (i - 1), "map" + i);
            i++;
        }
    }
}
