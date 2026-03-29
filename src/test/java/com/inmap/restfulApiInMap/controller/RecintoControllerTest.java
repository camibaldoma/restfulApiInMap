package com.inmap.restfulApiInMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.service.DestinoService;
import com.inmap.restfulApiInMap.service.RecintoService;
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

@WebMvcTest(RecintoController.class)
class RecintoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //conversor de Java a JSON

    @MockBean
    private RecintoService recintoService;

    @MockBean
    private DestinoService destinoService;

    private GeometryFactory geometryFactory = new GeometryFactory();

    private Recinto recinto1;
    private InformacionRecintoDTO informacionRecinto;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JtsModule());
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


        informacionRecinto = new InformacionRecintoDTO(aula5.getIdDestino(),recinto1.getIdRecinto(), aula5.getNombreDestino(), materia.getNombreMateria(),recinto1.getGeometria());
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
    @Test
    void saveRecinto() throws Exception {
        //Arrange
        Mockito.when(recintoService.saveRecinto(recinto1)).thenReturn(recinto1);
        //Act
        mockMvc.perform(post("/guardarRecinto")
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(recinto1))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.idRecinto").value("R50"));


    }
    @Test
    void updateRecinto() throws Exception {
        //Arrange
        String id = "R50";
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D50");
        aulaNueva.setNombreDestino("Aula Nueva Actualizada");
        aulaNueva.setGeometria(puntoAula);
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
        recinto1.setDestino(aulaNueva);
        recinto1.setGeometria(recintoMultiPoligono);
        Mockito.when(recintoService.updateRecinto(id,recinto1)).thenReturn(recinto1);
        //Act
        mockMvc.perform(put("/actualizarRecinto/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(recinto1))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.idRecinto").value("R50"))
                .andExpect(jsonPath("$.destino.nombreDestino").value("Aula Nueva Actualizada"));

    }
    @Test
    void deleteRecinto() throws Exception {
        //Arrange
        String id = "R50";
        //Act
        mockMvc.perform(delete("/eliminarRecinto/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))

                //Assert
                .andExpect(status().isOk());

        // Se verifica que el service realmente fue llamado para borrar ese ID
        verify(recintoService, times(1)).deleteRecinto(id);
    }
}