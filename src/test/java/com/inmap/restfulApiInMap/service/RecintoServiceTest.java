package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecintoServiceTest {

    @Autowired
    private RecintoService recintoService;

    @MockBean
    private RecintoRepository recintoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

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
        Recinto recinto1 = new Recinto();
        recinto1.setIdRecinto("R50");
        recinto1.setDestino(aula5);
        recinto1.setGeometria(recintoMultiPoligono);

        //Simula que lo trae de la base de datos
        Mockito.when(recintoRepository.findRecinto(recinto1.getIdRecinto())).thenReturn(List.of(recinto1));

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

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        InformacionRecinto informacionRecinto = new InformacionRecinto(aula5.getIdDestino(),recinto1.getIdRecinto(), aula5.getNombreDestino(), materia.getNombreMateria(),recinto1.getGeometria());

        Mockito.when(recintoRepository.findInformation(recinto1.getIdRecinto(),horaConsulta,diaConsulta)).thenReturn(List.of(informacionRecinto));
    }
    // Este no es necesario testearlo porque extiende de JpaRepository
    // No hace falta testearlo. No hay lógica de negocio.
    @Test
    void obtenerTodosRecintos() {
    }


    @Test
    void findRecinto() {
        String id = "R50";
        List<Recinto> resultado = recintoService.findRecinto(id);
        Recinto recinto = resultado.get(0);
        assertEquals(id,recinto.getIdRecinto());
    }

    @Test
    void findInformation() {
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String id = "R50";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        List<InformacionRecinto> resultado = recintoService.findInformation(id,horaConsulta, diaConsulta);

        //VERIFICACIÓN (Aseverar) ---
        assertThat(resultado).isNotNull();
        InformacionRecinto informacionRecinto = resultado.get(0);

        assertThat(informacionRecinto.getIdDestino()).isEqualTo("D50");
        assertThat(informacionRecinto.getIdRecinto()).isEqualTo("R50");
        assertThat(informacionRecinto.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(informacionRecinto.getNombreMateria()).isEqualTo("Sistemas Operativos");
    }
}