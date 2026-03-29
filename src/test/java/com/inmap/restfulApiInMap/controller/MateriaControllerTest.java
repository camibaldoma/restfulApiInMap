package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.service.MateriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MateriaController.class)
class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //conversor de Java a JSON
    @MockBean
    private MateriaService materiaService;

    private GeometryFactory geometryFactory = new GeometryFactory();

    private Recinto recinto1;
    private Materia materia;
    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JtsModule());
        Personal docenteTest = new Personal();
        docenteTest.setIdPersonal("999");
        docenteTest.setNombrePersonal("Camila");
        docenteTest.setApellidoPersonal("Baldomá");

        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);


        //Se crea un Recinto (polígono en el mapa)
        // Se crea un Polígono simple (un cuadrado pequeño) para el recinto
        // Se usan coordenadas cercanas al punto del aula
        LinearRing shell = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(-31.41, -64.18, 0.0),
                new Coordinate(-31.41, -64.19, 0.0),
                new Coordinate(-31.42, -64.19, 0.0),
                new Coordinate(-31.42, -64.18, 0.0),
                new Coordinate(-31.41, -64.18, 0.0) // El último siempre igual al primero
        });
        Polygon poligonoAula = geometryFactory.createPolygon(shell);
        MultiPolygon recintoMultiPoligono = geometryFactory.createMultiPolygon(new Polygon[]{poligonoAula});
        recinto1 = new Recinto();
        recinto1.setIdRecinto("R50");
        recinto1.setDestino(aula5);
        recinto1.setGeometria(recintoMultiPoligono);

        // Se crea el Horario (Lunes de 08:00 a 10:00)
        Horario horarioLunes = new Horario();
        horarioLunes.setIdHorario("H200");
        horarioLunes.setDias("Lunes");
        horarioLunes.setHoraInicio("08:00");
        horarioLunes.setHoraFin("10:00");


        // Se crea la Materia
        this.materia = new Materia();
        this.materia.setCodMateria("M1T");
        this.materia.setNombreMateria("Sistemas Operativos");


        // Se crea la Asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);

        //Se crea Esta
        Esta esta = new Esta();
        esta.setIdPersonal(docenteTest.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());
    }

    @Test
    void findMateria() throws Exception {
        String idMateria = "M1T";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        Mockito.when(materiaService.findMateria(idMateria,horaConsulta,diaConsulta)).thenReturn(List.of(recinto1));
        mockMvc.perform(get("/materia/{id}/{hora}/{dia}",idMateria,horaConsulta,diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }
    @Test
    void saveMateria() throws Exception {
        //Arrange
        Mockito.when(materiaService.saveMateria(materia)).thenReturn(materia);
        //Act
        mockMvc.perform(post("/guardarMateria")
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(materia))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.codMateria").value("M1T"))
                .andExpect(jsonPath("$.nombreMateria").value("Sistemas Operativos"));

    }
    @Test
    void updateMateria() throws Exception {
        //Arrange
        String id = "M1T";
        Materia materiaTest = new Materia();
        materiaTest.setCodMateria(id);
        materiaTest.setNombreMateria("Materia Test");
        Mockito.when(materiaService.updateMateria(id,materiaTest)).thenReturn(materiaTest);
        //Act
        mockMvc.perform(put("/actualizarMateria/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(materiaTest))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.codMateria").value("M1T"))
                .andExpect(jsonPath("$.nombreMateria").value("Materia Test"));

    }
    @Test
    void deleteMateria() throws Exception {
        //Arrange
        String id = "M1T";
        //Act
        mockMvc.perform(delete("/eliminarMateria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk());

        // Se verifica que el service realmente fue llamado para borrar ese ID
        verify(materiaService, times(1)).deleteMateria(id);
    }
}