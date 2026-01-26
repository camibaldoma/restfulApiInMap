package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.service.DestinoService;
import com.inmap.restfulApiInMap.service.RecintoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecintoController.class)
class RecintoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecintoService recintoService;

    @MockBean
    private DestinoService destinoService;


    private GeometryFactory geometryFactory = new GeometryFactory();

    private Recinto recinto1;
    private InformacionRecinto informacionRecinto;

    @BeforeEach
    void setUp() {
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

        // Se crea al Personal
        Personal docente = new Personal();
        docente.setIdPersonal("80");
        docente.setNombrePersonal("Cami");
        docente.setApellidoPersonal("Baldomá");

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
        esta.setIdPersonal(docente.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());


        informacionRecinto = new InformacionRecinto(aula5.getIdDestino(),recinto1.getIdRecinto(), aula5.getNombreDestino(), materia.getNombreMateria(),recinto1.getGeometria());
    }

    @Test
    void findRecinto() throws Exception {
        String idRecinto = "R50";
        Mockito.when(recintoService.findRecinto(idRecinto)).thenReturn(List.of(recinto1));
        mockMvc.perform(get("/recintos/{id}", idRecinto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }

    @Test
    void findInformation() throws Exception {
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        String idRecinto = "R50";
        Mockito.when(recintoService.findInformation(idRecinto,horaConsulta,diaConsulta)).thenReturn(List.of(informacionRecinto));
        mockMvc.perform(get("/informacionRecintos/{id}/{hora}/{dia}",idRecinto,horaConsulta,diaConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecinto").value("R50"));
    }
}