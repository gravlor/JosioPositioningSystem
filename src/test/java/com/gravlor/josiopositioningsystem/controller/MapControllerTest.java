package com.gravlor.josiopositioningsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddStaticMapRequest;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = JosioPositioningSystemApplication.class)
@AutoConfigureMockMvc
class MapControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private MapRepository mapRepository;

    @Test
    void testAddAvalonMap() throws Exception {

        mvc.perform(post(Constants.PATH_API_MAP)
                        .content("test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<MapEntity> optMap = mapRepository.findByName("test");
        if (optMap.isEmpty()) {
            fail();
        }

        mvc.perform(post(Constants.PATH_API_MAP)
                        .content("test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddStaticMap() throws Exception {

        mvc.perform(post(Constants.PATH_API_MAP_STATIC)
                        .content(TestsUtils.asJsonString(new AddStaticMapRequest("static", MapType.BLUE.name())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<MapEntity> optMap = mapRepository.findByName("static");
        if (optMap.isEmpty()) {
            fail();
        }

        mvc.perform(post(Constants.PATH_API_MAP_STATIC)
                        .content(TestsUtils.asJsonString(new AddStaticMapRequest("static", MapType.BLUE.name())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllMaps() throws Exception {

        mapRepository.save(new MapEntity("allMaps", MapType.BLACK));

        mvc.perform(get(Constants.PATH_API_MAP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<MapEntity> allMaps = mapRepository.findAll();
        assert allMaps.size()  > 0;
    }
    // add test map name
}
