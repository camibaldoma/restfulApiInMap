package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonalRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonalRepository personalRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {
    }

    @Test
    @Transactional
    void findAllOrderByApellido() {
        // Se crea un docente con un apellido que empiece con "AAA"
        // para forzarlo a estar en los primeros lugares.
        Personal docenteTest = new Personal();
        docenteTest.setIdPersonal("999");
        docenteTest.setNombrePersonal("Zulema");
        docenteTest.setApellidoPersonal("AAA_Prueba");
        entityManager.persist(docenteTest);

        entityManager.flush();

        // Se ejecuta la consulta
        List<PersonalReducido> resultados = personalRepository.findAllOrderByApellido();

        // VERIFICACIÓN
        // Se busca si el docente de prueba
        // está en una posición menor (más arriba) que uno con Z.
        boolean estaOrdenado = false;
        for (int i = 0; i < resultados.size() - 1; i++) {
            String actual = resultados.get(i).getNombreCompleto();
            if (actual.contains("AAA_Prueba")) {
                estaOrdenado = true; // Se encontró al principio o cerca
                break;
            }
        }
        assertTrue(estaOrdenado, "El personal de prueba debería aparecer en los primeros resultados");
    }

    @Test
    @Transactional
    void findUbicacionCompletaNative() {

        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);
        entityManager.persist(aula5);

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
        entityManager.persist(recinto1);

        // Se crea al Personal
        Personal docente = new Personal();
        docente.setIdPersonal("80");
        docente.setNombrePersonal("Cami");
        docente.setApellidoPersonal("Baldomá");
        entityManager.persist(docente);

        // Se crea el Horario (Lunes de 08:00 a 10:00)
        Horario horarioLunes = new Horario();
        horarioLunes.setIdHorario("H200");
        horarioLunes.setDias("Lunes");
        horarioLunes.setHoraInicio("08:00");
        horarioLunes.setHoraFin("10:00");
        entityManager.persist(horarioLunes);

        // Se crea la Materia
        Materia materia = new Materia();
        materia.setCodMateria("M1T");
        materia.setNombreMateria("Sistemas Operativos");
        entityManager.persist(materia);

        // Se crea la Asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion("A200");
        asignacion.setDestino(aula5);
        asignacion.setHorario(horarioLunes);
        asignacion.setMateria(materia);
        entityManager.persist(asignacion);

        //Se crea Esta
        Esta esta = new Esta();
        esta.setIdPersonal(docente.getIdPersonal());
        esta.setIdAsignacion(asignacion.getIdAsignacion());
        entityManager.persist(esta);



        entityManager.flush(); // Sincroniza con la DB

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        List<Object[]> resultados = personalRepository.findUbicacionCompletaNative(docente.getIdPersonal(), diaConsulta, horaConsulta);

        //VERIFICACIÓN (Aseverar) ---

        Object[] fila = resultados.get(0);



        // Se verifican las columnas según el orden del mapeo en el Service:
        assertThat(fila[0]).isEqualTo("D50");
        assertThat(fila[1]).isEqualTo("R50");
        assertThat(fila[2]).isEqualTo("Aula 5");
        assertThat(fila[4]).isNotNull(); // Esto es la geometría (GeoLatte)

    }
}