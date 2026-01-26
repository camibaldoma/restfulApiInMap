package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.service.PersonalService;
import org.geolatte.geom.jts.JTS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonalController.class)
class PersonalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonalService personalService;

    private GeometryFactory geometryFactory = new GeometryFactory();

    private PersonalReducido docenteReal;
    private UbicacionPersonal ubicacionReal;

    @BeforeEach
    void setUp() {
        // Se crea un docente con un apellido que empiece con "AAA"
        // para forzarlo a estar en los primeros lugares.
        Personal docenteTest = new Personal();
        docenteTest.setIdPersonal("999");
        docenteTest.setNombrePersonal("Zulema");
        docenteTest.setApellidoPersonal("AAA_Prueba");

       docenteReal = new PersonalReducidoDTO("Zulema AAA_Prueba");

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
        esta.setIdPersonal(docenteTest.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());


        ubicacionReal = new UbicacionPersonal(aula5.getIdDestino(),recinto1.getIdRecinto(),aula5.getNombreDestino(),materia.getNombreMateria(),recinto1.getGeometria());

    }

    @Test
    void findAllOrderByApellido() throws Exception {
        Mockito.when(personalService.findAllOrderByApellido()).thenReturn(List.of(docenteReal));
        mockMvc.perform(get("/personal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Zulema AAA_Prueba"));
    }

    @Test
    void findUbicacionCompletaNative() throws  Exception {
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        String id = "999";
        Mockito.when(personalService.findUbicacionCompletaNative(anyString(),anyString(),anyString())).thenReturn(List.of(ubicacionReal));
        mockMvc.perform(get("/personal/{id}/{hora}/{dia}",id,horaConsulta,diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }

    // Esta clase"simula ser el objeto que vendría de la DB
    private static class PersonalReducidoDTO implements PersonalReducido {
        private String nombreCompleto;

        public PersonalReducidoDTO(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        @Override
        public String getIdPersonal() {
            return "";
        }

        @Override
        public String getNombreCompleto() {
            return nombreCompleto;
        }

        @Override
        public String getCargoLaboral() {
            return "";
        }
    }
}