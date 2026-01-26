package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import org.geolatte.geom.jts.JTS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class MateriaServiceTest {

    @Autowired
    MateriaService materiaService;

    @MockBean
    MateriaRepository materiaRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {

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

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        Mockito.when(materiaRepository.findMateria(
                materia.getCodMateria(), // En lugar de docenteTest.getIdPersonal()
                horaConsulta, // En lugar de diaConsulta
                diaConsulta  // En lugar de horaConsulta
        )).thenReturn(List.of(recinto1));

    }
    @Test
    void findMateria() {
        String id = "M1T";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        List<Recinto> resultados = materiaService.findMateria(id, horaConsulta, diaConsulta);
        Recinto resultado = resultados.get(0);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        assertEquals(resultado.getIdRecinto(), "R50");
    }
}