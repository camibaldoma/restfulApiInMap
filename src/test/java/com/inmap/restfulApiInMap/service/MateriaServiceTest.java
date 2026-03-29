package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;

import com.inmap.restfulApiInMap.repository.MateriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MateriaServiceTest {

    @Autowired
    MateriaService materiaService;

    @MockBean
    MateriaRepository materiaRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Materia materia;
    private Materia materiaTest;
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

        // Se prueba buscar a las 09:00 (en medio de la clase)
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        String idInexistente = "M9F";
        String idExistente = "M9P";

        this.materiaTest = new Materia();
        this.materiaTest.setCodMateria(idExistente);
        this.materiaTest.setNombreMateria("Materia test");

        Mockito.when(materiaRepository.findMateria(materia.getCodMateria(),horaConsulta,diaConsulta)).thenReturn(List.of(recinto1));
        Mockito.when(materiaRepository.findMateria(idExistente,horaConsulta,diaConsulta)).thenReturn(null);
        Mockito.when(materiaRepository.findMateria(eq(idInexistente),anyString(),anyString())).thenReturn(null);
        Mockito.when(materiaRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(materiaRepository.existsById(idInexistente)).thenReturn(false);
        Mockito.when(materiaRepository.save(any(Materia.class))).thenAnswer(returnsFirstArg());
        Mockito.when(materiaRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(materiaRepository.findById(materia.getCodMateria())).thenReturn(Optional.of(materia));
        Mockito.when(materiaRepository.findById(idInexistente)).thenReturn(Optional.empty());
        Mockito.when(materiaRepository.findById(idExistente)).thenReturn(Optional.of(materiaTest));
    }
    //Se testea que la materia SI se encuentre
    @Test
    void findMateriaSI() {
        //Arrange
        String id = "M1T";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        //Act
        List<Recinto> resultados = materiaService.findMateria(id, horaConsulta, diaConsulta);
        //Assert
        Recinto resultado = resultados.get(0);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        assertEquals(resultado.getIdRecinto(), "R50");
        assertEquals(resultado.getDestino().getNombreDestino(), "Aula 5");
    }
    //Se testea que la materia NO se encuentre. Lanzamiento de NotFoundException porque el id no existe
    @Test
    void findMateriaNO() {
        //Arrange
        String idInexistente = "M9F";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            materiaService.findMateria(idInexistente, horaConsulta, diaConsulta);
        });
    }
    //Se testea que la materia NO se encuentre. Lanzamiento de NotFoundException porque la materia no se está dictando en ese momento
    @Test
    void findMateriaNO_1() {
        //Arrange
        String idExistente = "M9P";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            materiaService.findMateria(idExistente, horaConsulta, diaConsulta);
        });
    }
    //Se testea que la materia SI se guarde
    @Test
    void saveMateriaSI() throws Exception {
        //Arrange
        //Act
        Materia save = materiaService.saveMateria(materia);
        //Assert
        assertNotNull(save,"El objeto guardado no debería ser nulo.");
        assertEquals("M1T", save.getCodMateria());
        assertEquals("Sistemas Operativos", save.getNombreMateria());
    }
    //Se testea que la materia NO se guarde. Lanzamiento de ArgumentNotValidException
    @Test
    void saveMateriaNO() throws Exception {
        //Arrange
        String idExistente = "M9P";

        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            materiaService.saveMateria(materiaTest);
        });
    }
    //Se testea que la materia SI se actualice
    @Test
    void updateMateriaSI() throws Exception {
        //Arrange
        String cod = "M1T";
        Materia materiaTest = new Materia();
        materiaTest .setCodMateria(cod);
        materiaTest .setNombreMateria("Materia test");
        //Act
        Materia update = materiaService.updateMateria(cod,materiaTest);
        //Assert
        assertNotNull(update,"El objeto actualizado no debería ser nulo.");
        assertEquals("M1T", update.getCodMateria());
        assertEquals("Materia test", update.getNombreMateria());
    }
    //Se testea que la materia NO se actualice. Lanzamiento de NotFoundException
    @Test
    void updateMateriaNO() throws Exception {
        //Arrange
        String idInexistente = "M9F";
        //Act y Assert
            assertThrows(NotFoundException.class, () -> {
            materiaService.updateMateria(idInexistente,materia);
        });

    }
    //Se testea que la materia NO se actualice. Lanzamiento de ArgumentNotValidException
    @Test
    void updateMateriaNO_1() throws Exception {
        //Arrange
        String idExistente = "M9P";
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            materiaService.updateMateria(idExistente,materia);
        });
    }
    //Se testea que la materia SI se elimine.
    @Test
    void deleteMateriaSI() throws Exception, NotFoundException {
        //Arrange
        String id = "M1T";
        //Act
        materiaService.deleteMateria(id);
        // Assert. Se verifica que el metodo deleteById del repo se ejecutó 1 vez
        verify(materiaRepository, times(1)).deleteById(id);
    }

}