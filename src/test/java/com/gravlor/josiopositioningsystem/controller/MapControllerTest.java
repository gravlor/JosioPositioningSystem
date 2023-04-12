package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.TestsUtils;
import com.gravlor.josiopositioningsystem.config.JosioPositioningSystemApplication;
import com.gravlor.josiopositioningsystem.controller.model.AddAvalonMapRequest;
import com.gravlor.josiopositioningsystem.controller.model.AddMapRequest;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                        .content(TestsUtils.asJsonString(new AddAvalonMapRequest("test")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<MapEntity> optMap = mapRepository.findByName("test");
        if (optMap.isEmpty()) {
            fail();
        }

        mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                        .content(TestsUtils.asJsonString(new AddAvalonMapRequest("test")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mapRepository.delete(optMap.get());
        assert mapRepository.findAll().size() == 0;
    }

   @Test
    void testAddAvalonMapName() throws Exception {
        mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                        .content(TestsUtils.asJsonString(new AddAvalonMapRequest(null)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'name' : Name must not be empty")));

       mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                       .content(TestsUtils.asJsonString(new AddAvalonMapRequest("  ")))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'name' : Name must not be empty")));

       mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                       .content(TestsUtils.asJsonString(new AddAvalonMapRequest("yolo$")))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'name' : Name contains invalid characters")));

       mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                       .content(TestsUtils.asJsonString(new AddAvalonMapRequest("yolo2")))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.messages", Matchers.contains("Error for field 'name' : Name contains invalid characters")));

       mvc.perform(post(Constants.PATH_API_MAP_AVALON)
                       .content(TestsUtils.asJsonString(new AddAvalonMapRequest("   yolo  test   ")))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());

       Optional<MapEntity> optMap = mapRepository.findByName("yolo test");
       if (optMap.isEmpty()) {
           fail();
       }

       mapRepository.delete(optMap.get());
       assert mapRepository.findAll().size() == 0;
   }

    @Test
    void testAddMap() throws Exception {
        mvc.perform(post(Constants.PATH_API_MAP)
                        .content(TestsUtils.asJsonString(new AddMapRequest("static", MapType.BLUE.name())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<MapEntity> optMap = mapRepository.findByName("static");
        if (optMap.isEmpty()) {
            fail();
        }

        mapRepository.delete(optMap.get());
        assert mapRepository.findAll().size() == 0;
    }

    @Test
    void testGetAllMaps() throws Exception {
        MapEntity map = new MapEntity("allMaps", MapType.BLACK);
        mapRepository.save(map);

        mvc.perform(get(Constants.PATH_API_MAP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<MapEntity> allMaps = mapRepository.findAll();
        assert allMaps.size()  == 1;

        mapRepository.delete(map);
        assert mapRepository.findAll().size() == 0;
    }
}
