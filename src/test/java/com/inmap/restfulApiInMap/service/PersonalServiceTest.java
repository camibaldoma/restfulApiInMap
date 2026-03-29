package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.PersonalReducidoDTO;
import com.inmap.restfulApiInMap.dto.UbicacionPersonalDTO;
import com.inmap.restfulApiInMap.entity.*;

import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;

/*@SpringBootTest
class PersonalServiceTest {

    @Autowired
    PersonalService personalService;

    @MockBean
    PersonalRepository personalRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Personal docenteTest1;
    private Personal docenteTest2;
    @BeforeEach
    void setUp() {

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

        PersonalReducidoDTO p1 = new PersonalReducidoDTO(docenteTest1.getNombrePersonal(), docenteTest1.getApellidoPersonal(),  docenteTest1.getCargoLaboral());
        PersonalReducidoDTO p2 = new PersonalReducidoDTO(docenteTest2.getNombrePersonal(), docenteTest2.getApellidoPersonal(),  docenteTest2.getCargoLaboral());

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

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        org.geolatte.geom.Geometry<?> geoLatteGeom = JTS.from(recintoMultiPoligono);
        // Se definen los datos que irían en la "fila" de la base de datos
        Object[] filaSimulada = new Object[]{
                aula5.getIdDestino(),
                recinto1.getIdRecinto(),
                aula5.getNombreDestino(),
                materia.getNombreMateria(),
                geoLatteGeom
        };

        List<Object[]> resultadosSimulados = new ArrayList<>();
        resultadosSimulados.add(filaSimulada);

        String idInexistente = "000";
        Mockito.when(personalRepository.findAllOrderByApellido()).thenReturn(List.of(p1, p2));
        Mockito.when(personalRepository.findUbicacionCompletaNative(eq(docenteTest1.getIdPersonal()), anyString(), anyString())).thenReturn(resultadosSimulados);
        Mockito.when(personalRepository.findUbicacionCompletaNative(eq(docenteTest2.getIdPersonal()), anyString(), anyString())).thenReturn(null);
        Mockito.when(personalRepository.existsById(docenteTest2.getIdPersonal())).thenReturn(true);
        Mockito.when(personalRepository.existsById(idInexistente)).thenReturn(false);
        Mockito.when(personalRepository.save(any(Personal.class))).thenAnswer(returnsFirstArg());
        Mockito.when(personalRepository.existsById(docenteTest2.getIdPersonal())).thenReturn(true);
        Mockito.when(personalRepository.findById(docenteTest1.getIdPersonal())).thenReturn(Optional.of(docenteTest1));
        Mockito.when(personalRepository.findById(idInexistente)).thenReturn(Optional.empty());
    }
    @Test
    void findAllOrderByApellido() {
        //Arrange
        //Act
        List<PersonalReducidoDTO> resultados = personalService.findAllOrderByApellido();
        //Assert
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        String nombreRecibido1 = resultados.get(0).getNombreCompleto();
        String nombreRecibido2 = resultados.get(1).getNombreCompleto();
        assertEquals("Zulema AAA_Prueba", nombreRecibido1);
        assertEquals("Juana BBB_Prueba", nombreRecibido2);
    }
    //Se testea que SI se encuentre la ubicación
    @Test
    void findUbicacionCompletaNativeSI() {
        //Arrange
        String id = "999";
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        //Act
        List<UbicacionPersonalDTO> resultados = personalService.findUbicacionCompletaNative(id, diaConsulta, horaConsulta);
        //Assert
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");

        UbicacionPersonalDTO fila = resultados.get(0);

        assertThat(fila.getIdDestino()).isEqualTo("D50");
        assertThat(fila.getIdRecinto()).isEqualTo("R50");
        assertThat(fila.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(fila.getGeometria()).isNotNull();
    }
    //Se testea que NO se encuentre la ubicación. Lanzamiento de NotFoundException throw new NotFoundException por no encontrarse presente en este momento en el establecimiento.
    @Test
    void findUbicacionCompletaNativeNO() {
        //Arrange
        String id = "998";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            personalService.findUbicacionCompletaNative(id, diaConsulta, horaConsulta);
        });
    }
    //Se testea que NO se encuentre la ubicación. Lanzamiento de NotFoundException throw new NotFoundException por id inexistente
    @Test
    void findUbicacionCompletaNativeNO_1() {
        //Arrange
        String idInexistente = "000";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";

        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            personalService.findUbicacionCompletaNative(idInexistente, diaConsulta, horaConsulta);
        });
    }
    //Se testea que SI se guarde el personal.
    @Test
    void savePersonalSI() throws Exception {
        //Arrange
        //Act
        Personal save = personalService.savePersonal(docenteTest1);
        //Assert
        assertNotNull(save,"El objeto guardado no debería ser nulo.");
        assertEquals("999", save.getIdPersonal());
        assertEquals("Zulema", save.getNombrePersonal());
    }
    //Se testea que NO se guarde el personal. Lanzamiento de ArgumentNotValidException
    @Test
    void savePersonalNO() throws Exception {

        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            personalService.savePersonal(docenteTest2);
        });
    }
    //Se testea que SI se actualice el personal.
    @Test
    void updatePersonalSI() throws Exception {
        //Arrange
        String id = docenteTest1.getIdPersonal();
        Personal docenteTest1Actualizado = new Personal();
        docenteTest1Actualizado.setIdPersonal(id);
        docenteTest1Actualizado.setDni("123456789");
        docenteTest1Actualizado.setCargoLaboral("Laboral_A");
        docenteTest1Actualizado.setNombrePersonal("NombreActualizado");
        docenteTest1Actualizado.setApellidoPersonal("ApellidoActualizado");
        //Act
        Personal update = personalService.updatePersonal(id,docenteTest1Actualizado);
        //Assert
        assertNotNull(update,"El objeto guardado no debería ser nulo.");
        assertEquals("999", update.getIdPersonal());
        assertEquals("NombreActualizado", update.getNombrePersonal());
        assertEquals("ApellidoActualizado", update.getApellidoPersonal());
    }
    //Se testea que NO se actualice el personal. Lanzamiento de NotFoundException
    @Test
    void updatePersonalNO() throws Exception {
        //Arrange
        String idInexistente = "000";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            personalService.updatePersonal(idInexistente,docenteTest2);
        });
    }
    //Se testea que NO se actualice el personal. Lanzamiento de ArgumentNotValidException
    @Test
    void updatePersonalNO_1() throws Exception {
        //Arrange
        String id = docenteTest1.getIdPersonal();
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            personalService.updatePersonal(id,docenteTest2);
        });
    }
    //Se testea que el personal SI se elimine.
    @Test
    void deletePersonalSI() throws Exception, NotFoundException {
        //Arrange
        String id = "999";
        //Act
        personalService.deletePersonal(id);
        // Assert. Se verifica que el metodo deleteById del repo se ejecutó 1 vez
        Mockito.verify(personalRepository, Mockito.times(1)).deleteById(id);
    }
}*/