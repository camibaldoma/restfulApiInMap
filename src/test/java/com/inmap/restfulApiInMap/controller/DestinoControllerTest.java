package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.service.DestinoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DestinoController.class)
class DestinoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //conversor de Java a JSON

    @MockBean
    private DestinoService destinoService;

    private DestinoReducidoDTO destinoReducido;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Destino aulaTest;
    private Destino aulaNueva;
    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JtsModule());
        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        this.aulaTest = new Destino();
        this.aulaTest.setIdDestino("D100");
        this.aulaTest.setNombreDestino("Aula Test");
        this.aulaTest.setGeometria(puntoAula);

        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        this.aulaNueva = new Destino();
        this.aulaNueva.setIdDestino("D200");
        this.aulaNueva.setNombreDestino("Aula Nueva");
        this.aulaNueva.setGeometria(puntoAula1);

        destinoReducido = new DestinoReducidoDTO(aulaTest.getNombreDestino(), aulaTest.getGeometria());
        DestinoReducidoDTO destinoReducido1 = new DestinoReducidoDTO(aulaNueva.getNombreDestino(), aulaNueva.getGeometria());
    }

    @Test
    void findDestino() throws Exception {
        //Arrange
        String id= "D100";
        Mockito.when(destinoService.findDestino(id)).thenReturn(List.of(destinoReducido));
        //Act
        mockMvc.perform(get("/destinos/{id}",id))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreDestino").value("Aula Test"));
    }
    @Test
    void saveDestino() throws Exception {
        //Arrange
        Mockito.when(destinoService.saveDestino(aulaNueva)).thenReturn(aulaNueva);
        //Act
        mockMvc.perform(post("/guardarDestino")
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(aulaNueva))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.idDestino").value("D200"))
                .andExpect(jsonPath("$.nombreDestino").value("Aula Nueva"));

    }
    @Test
    void updateDestino() throws Exception {
        //Arrange
        String id = "D200";
        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        Destino aulaNuevaActualizada = new Destino();
        aulaNuevaActualizada.setIdDestino("D200");
        aulaNuevaActualizada.setNombreDestino("Aula Nueva Actualizada");
        aulaNuevaActualizada.setGeometria(puntoAula1);
        Mockito.when(destinoService.updateDestino(id,aulaNuevaActualizada)).thenReturn(aulaNuevaActualizada);
        //Act
        mockMvc.perform(put("/actualizarDestino/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(aulaNuevaActualizada))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.idDestino").value("D200"))
                .andExpect(jsonPath("$.nombreDestino").value("Aula Nueva Actualizada"));

    }
    @Test
    void deleteDestino() throws Exception {
        //Arrange
        String id = "D200";
        //Act
        mockMvc.perform(delete("/eliminarDestino/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))

                //Assert
                .andExpect(status().isOk());

        // Se verifica que el service realmente fue llamado para borrar ese ID
        verify(destinoService, times(1)).deleteDestino(id);
    }
}