package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class PersonalServiceTest {

    @Autowired
    PersonalService personalService;

    @MockBean
    PersonalRepository personalRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {
        // Se crea un docente con un apellido que empiece con "AAA"
        // para forzarlo a estar en los primeros lugares.
        Personal docenteTest = new Personal();
        docenteTest.setIdPersonal("999");
        docenteTest.setNombrePersonal("Zulema");
        docenteTest.setApellidoPersonal("AAA_Prueba");

        //Se debe hacer esto porque PersonalReducido es una interfaz
        PersonalReducido mockPersonal = Mockito.mock(PersonalReducido.class);
        Mockito.when(mockPersonal.getNombreCompleto()).thenReturn("Zulema AAA_Prueba");

        Mockito.when(personalRepository.findAllOrderByApellido()).thenReturn(List.of(mockPersonal));

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
        org.geolatte.geom.Geometry<?> geoLatteGeom = JTS.from(recintoMultiPoligono);
        // Definís los datos que irían en la "fila" de la base de datos
        Object[] filaSimulada = new Object[]{
                aula5.getIdDestino(),              // fila[0]
                recinto1.getIdRecinto(),              // fila[1]
                aula5.getNombreDestino(),           // fila[2]
                materia.getNombreMateria(),     // fila[3] (motivo/materia)
                geoLatteGeom // fila[4]
        };

        // Primero se crea la lista explícitamente para que Java no se maree
        List<Object[]> resultadosSimulados = new ArrayList<>();
        resultadosSimulados.add(filaSimulada);

        // Ahora el thenReturn va a reconocer el tipo sin problemas
        Mockito.when(personalRepository.findUbicacionCompletaNative(
                anyString(), // En lugar de docenteTest.getIdPersonal()
                anyString(), // En lugar de diaConsulta
                anyString()  // En lugar de horaConsulta
        )).thenReturn(resultadosSimulados);

    }
    @Test
    void findAllOrderByApellido() {
        List<PersonalReducido> resultados = personalService.findAllOrderByApellido();
        //Verificación
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");

        String nombreRecibido = resultados.get(0).getNombreCompleto();
        assertEquals("Zulema AAA_Prueba", nombreRecibido);
    }
    @Test
    void findUbicacionCompletaNative() {
        String id = "999";
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        List<UbicacionPersonal> resultados = personalService.findUbicacionCompletaNative(id, diaConsulta, horaConsulta);

        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");

        UbicacionPersonal fila = resultados.get(0);

        // Se verifican las columnas según el orden del mapeo en el Service:
        assertThat(fila.getIdDestino()).isEqualTo("D50");
        assertThat(fila.getIdRecinto()).isEqualTo("R50");
        assertThat(fila.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(fila.getGeometria()).isNotNull(); // Esto es la geometría (GeoLatte)
    }
}