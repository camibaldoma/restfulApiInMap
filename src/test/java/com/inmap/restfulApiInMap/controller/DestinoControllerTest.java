package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.service.DestinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DestinoController.class)
class DestinoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DestinoService destinoService;

    private DestinoReducido destinoReducido;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {
        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);

        destinoReducido = new DestinoReducido(aula5.getNombreDestino(), aula5.getGeometria());
    }

    @Test
    void findDestino() throws Exception {
        String id= "D50";
        Mockito.when(destinoService.findDestino(id)).thenReturn(List.of(destinoReducido));
        mockMvc.perform(get("/destinos/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreDestino").value("Aula 5"));
    }
}