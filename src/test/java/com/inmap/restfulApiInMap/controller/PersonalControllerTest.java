package com.inmap.restfulApiInMap.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.PersonalRequestDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.*;

import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.service.PersonalService;
import org.geolatte.geom.jts.JTS;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@WebMvcTest(PersonalController.class)
class PersonalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; //conversor de Java a JSON

    @MockBean
    private PersonalService personalService;

    private GeometryFactory geometryFactory = new GeometryFactory();

    private Personal docenteTest1;
    private Personal docenteTest2;
    private PersonalReducidoDTO docenteTestReducido1;
    private PersonalReducidoDTO docenteTestReducido2;
    private UbicacionPersonalDTO ubicacionReal;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JtsModule());
        this.docenteTest1 = new Personal();
        this.docenteTest1.setIdPersonal("999");
        this.docenteTest1.setDni("123456789");
        this.docenteTest1.setCargoLaboral("Laboral_A");
        this.docenteTest1.setNombrePersonal("Zulema");
        this.docenteTest1.setApellidoPersonal("AAA_Prueba");

        this.docenteTest2 = new Personal();
        this.docenteTest2.setIdPersonal("998");
        this.docenteTest2.setDni("023456789");
        this.docenteTest2.setCargoLaboral("Laboral_B");
        this.docenteTest2.setNombrePersonal("Juana");
        this.docenteTest2.setApellidoPersonal("BBB_Prueba");

        docenteTestReducido1 = new PersonalReducidoDTO(docenteTest1.getNombrePersonal(),docenteTest1.getApellidoPersonal(),docenteTest1.getCargoLaboral() );
        docenteTestReducido2 = new PersonalReducidoDTO(docenteTest2.getNombrePersonal(),docenteTest2.getApellidoPersonal(),docenteTest2.getCargoLaboral() );
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
        Recinto recinto1 = new Recinto();
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
        Materia materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");


        // Se crea la Asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);


        //Se crea Esta
        Esta esta = new Esta();
        esta.setIdPersonal(docenteTest1.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());


        ubicacionReal = new UbicacionPersonalDTO(aula5.getIdDestino(),recinto1.getIdRecinto(),aula5.getNombreDestino(),materia.getNombreMateria(),recinto1.getGeometria());

    }

    @Test
    void findAllOrderByApellido() throws Exception {
        Mockito.when(personalService.findAllOrderByApellido()).thenReturn(List.of(docenteTestReducido1,docenteTestReducido2));
        mockMvc.perform(get("/personal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Zulema AAA_Prueba"));
    }

    @Test
    void findUbicacionCompletaNative() throws  Exception {
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        String id = "999";
        Mockito.when(personalService.findUbicacionCompletaNative(eq(id),anyString(),anyString())).thenReturn(List.of(ubicacionReal));
        mockMvc.perform(get("/personal/{id}/{hora}/{dia}",id,horaConsulta,diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }
    @Test
    void savePersonal() throws Exception {
        //Arrange
        PersonalRequestDTO personalRequestDTO = new PersonalRequestDTO();
        personalRequestDTO.setNombrePersonal(docenteTest1.getNombrePersonal());
        personalRequestDTO.setApellidoPersonal(docenteTest1.getApellidoPersonal());
        personalRequestDTO.setCargoLaboral(docenteTest1.getCargoLaboral());
        personalRequestDTO.setDni(docenteTest1.getDni());
        Mockito.when(personalService.savePersonal(personalRequestDTO)).thenReturn(docenteTest1);
        //Act
        mockMvc.perform(post("/guardarPersonal")
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(docenteTest1))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.idPersonal").value("999"))
                .andExpect(jsonPath("$.nombrePersonal").value("Zulema"))
                .andExpect(jsonPath("$.apellidoPersonal").value("AAA_Prueba"));
    }
    @Test
    void updatePersonal() throws Exception {
        //Arrange
        String id = "999";
        Personal personalTest = new Personal();
        personalTest.setIdPersonal(id);
        personalTest.setDni("123456789");
        personalTest.setCargoLaboral("Laboral_A");
        personalTest.setNombrePersonal("NombreTest");
        personalTest.setApellidoPersonal("ApellidoTest");
        Mockito.when(personalService.updatePersonal(id,personalTest)).thenReturn(personalTest);
        //Act
        mockMvc.perform(put("/actualizarPersonal/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(personalTest))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.idPersonal").value("999"))
                .andExpect(jsonPath("$.nombrePersonal").value("NombreTest"))
                .andExpect(jsonPath("$.apellidoPersonal").value("ApellidoTest"));

    }
    @Test
    void deletePersonal() throws Exception {
        //Arrange
        String id = "999";
        //Act
        mockMvc.perform(delete("/eliminarPersonal/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk());

        // Se verifica que el service realmente fue llamado para borrar ese ID
        verify(personalService, times(1)).deletePersonal(id);
    }
}*/