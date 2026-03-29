package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.dto.InformacionRecintoDTO;
import com.inmap.restfulApiInMap.entity.*;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RecintoServiceTest {

    @Autowired
    private RecintoService recintoService;


    @MockBean
    private RecintoRepository recintoRepository;
    @MockBean
    private DestinoRepository destinoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Recinto recinto1;
    private Recinto recinto;
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
        this.recinto1 = new Recinto();
        this.recinto1.setIdRecinto("R50");
        this.recinto1.setDestino(aula5);
        this.recinto1.setBloqueado(false);
        this.recinto1.setGeometria(recintoMultiPoligono);

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

        String horaConsulta1 = "13:00:00";


        InformacionRecintoDTO informacionRecinto = new InformacionRecintoDTO(aula5.getIdDestino(),recinto1.getIdRecinto(), aula5.getNombreDestino(), materia.getNombreMateria(),recinto1.getGeometria());

        String idInexistente = "R999";
        String idExistente = "R60";
        String idDestinoExistente = "D60";
        String idOK ="R70";

        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula Nueva");
        aulaNueva.setGeometria(puntoAula);


        this.recinto = new Recinto();
        this.recinto.setIdRecinto("R70");
        this.recinto.setDestino(aulaNueva);
        this.recinto.setGeometria(recintoMultiPoligono);
        this.recinto.setBloqueado(false);

        Mockito.when(recintoRepository.findInformation(recinto1.getIdRecinto(),horaConsulta,diaConsulta)).thenReturn(List.of(informacionRecinto));
        Mockito.when(recintoRepository.findInformation(recinto1.getIdRecinto(),horaConsulta1,diaConsulta)).thenReturn(Collections.emptyList());
        Mockito.when(recintoRepository.findById(recinto1.getIdRecinto())).thenReturn(Optional.of(recinto1));
        Mockito.when(recintoRepository.findById(idInexistente)).thenReturn(Optional.empty());
        Mockito.when(recintoRepository.save(recinto1)).thenReturn(recinto1);
        Mockito.when(recintoRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(destinoRepository.existsById(idDestinoExistente)).thenReturn(true);
        Mockito.when(recintoRepository.existsById(idOK)).thenReturn(false);
        Mockito.when(recintoRepository.existsDestinoInRecinto(idDestinoExistente,idOK)).thenReturn(true);
        Mockito.when(recintoRepository.save(any(Recinto.class))).thenAnswer(returnsFirstArg());
        Mockito.when(recintoRepository.findById(idExistente)).thenReturn(Optional.of(recinto1));
        Mockito.when(recintoRepository.findById(idOK)).thenReturn(Optional.of(recinto));
    }
    //Se testea que SI se encuentre el recinto
    @Test
    void findRecintoSI() throws NotFoundException {
        //Arrange
        String id = "R50";
        //Act
        List<Recinto> resultado = recintoService.findRecinto(id);
        //Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "La lista no debería estar vacía");
        Recinto recinto = resultado.get(0);
        assertEquals(id,recinto.getIdRecinto());
    }
    //Se testea que NO se encuentre el recinto. Lanzamiento NotFoundException
    @Test
    void findRecintoNO() throws NotFoundException {
        //Arrange
        String id = "D999";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            recintoService.findRecinto(id);
        });
    }
    //Se testea que SI se encuentre información
    @Test
    void findInformationSI() throws NotFoundException {
        //Arrange
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String id = "R50";
        String horaConsulta = "09:00:00";
        String diaConsulta = "Lunes";
        //Act
        List<InformacionRecintoDTO> resultado = recintoService.findInformation(id,horaConsulta, diaConsulta);
        //Assert
        assertThat(resultado).isNotNull();
        InformacionRecintoDTO informacionRecinto = resultado.get(0);

        assertThat(informacionRecinto.getIdDestino()).isEqualTo("D50");
        assertThat(informacionRecinto.getIdRecinto()).isEqualTo("R50");
        assertThat(informacionRecinto.getNombreDestino()).isEqualTo("Aula 5");
        assertThat(informacionRecinto.getNombreMateria()).isEqualTo("Sistemas Operativos");
    }
    //Se testea que NO se encuentre información. Lanzamiento NotFoundException por no hallar información
    @Test
    void findInformationNO() throws NotFoundException, OverlapException {
        //Arrange
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String id = "R50";
        String horaConsulta = "13:00:00";
        String diaConsulta = "Lunes";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            recintoService.findInformation(id,horaConsulta, diaConsulta);
        });

    }
    //Se testea que NO se encuentre información. Lanzamiento NotFoundException por no hallar el recinto
    @Test
    void findInformationNO_1() throws NotFoundException, OverlapException {
        //Arrange
        // Se prueba buscar a las 09:00 (en medio de la clase)
        String id = "R999";
        String horaConsulta = "13:00:00";
        String diaConsulta = "Lunes";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            recintoService.findInformation(id,horaConsulta, diaConsulta);
        });

    }
    //Se testea que SI se guarde el recinto.
    @Test
    void saveRecintoSI() throws Exception, ArgumentNotValidException, OverlapException {
        //Arrange
        //Act
        Recinto save = recintoService.saveRecinto(recinto1);
        //Assert
        assertNotNull(save,"El objeto guardado no debería ser nulo.");
        assertEquals("R50", save.getIdRecinto());
        //Verificación de la persistencia real
        List<Recinto> resultados =  recintoService.findRecinto(save.getIdRecinto());
        assertNotNull(resultados,"La lista no debería ser nula.");
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        Recinto resultado = resultados.get(0);
        assertEquals("Aula 5", resultado.getDestino().getNombreDestino());
    }
    //Se testea que NO se guarde el recinto. Lanzamiento de ArgumentNotValidException
    @Test
    void saveRecintoNO() throws Exception,ArgumentNotValidException, OverlapException {
        //Arrange
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula Nueva");
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
        Recinto recintoIdExistente = new Recinto();
        recintoIdExistente.setIdRecinto("R60");
        recintoIdExistente.setDestino(aulaNueva);
        recintoIdExistente.setGeometria(recintoMultiPoligono);
        recintoIdExistente.setBloqueado(false);
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            recintoService.saveRecinto(recintoIdExistente);
        });
    }
    //Se testea que NO se guarde el recinto. Lanzamiento de OverlapException
    @Test
    void saveRecintoNO_1() throws Exception,ArgumentNotValidException, OverlapException {
        //Arrange

        //Act y Assert
        assertThrows(OverlapException.class, () -> {
            recintoService.saveRecinto(recinto);
        });
    }
    //Se testea que SI se actualice el recinto.
    @Test
    void updateRecintoSI() throws Exception {
        //Arrange
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaNueva = new Destino();
        aulaNueva.setIdDestino("D60");
        aulaNueva.setNombreDestino("Aula ACTUALIZADA");
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
        Recinto recinto = new Recinto();
        recinto.setIdRecinto("R60");
        recinto.setDestino(aulaNueva);
        recinto.setGeometria(recintoMultiPoligono);
        recinto.setBloqueado(false);
        //Act
        Recinto update = recintoService.updateRecinto("R60",recinto);
        //Assert
        assertNotNull(update,"El objeto actualizado no debería ser nulo.");
        assertEquals("R50", update.getIdRecinto());
        //Verificación de la persistencia real
        List<Recinto> resultados =  recintoService.findRecinto(update.getIdRecinto());
        assertNotNull(resultados,"La lista no debería ser nula.");
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        Recinto resultado = resultados.get(0);
        assertEquals("Aula ACTUALIZADA", resultado.getDestino().getNombreDestino());
    }
    //Se testea que NO se actualice el recinto. Lanzamiento de NotFoundException
    @Test
    void updateRecintoNO() throws Exception {
        //Arrange
        String id = "R999";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            recintoService.updateRecinto(id,recinto1);
        });
    }
    //Se testea que NO se actualice el recinto. Lanzamiento de ArgumentNotValidException
    @Test
    void updateRecintoNO_1() throws Exception {
        //Arrange

        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            recintoService.updateRecinto(recinto1.getIdRecinto(),recinto);
        });
    }
    //Se testea que NO se actualice el recinto. Lanzamiento de OverlapException
    @Test
    void updateRecintoNO_2() throws Exception {
        //Arrange

        //Act y Assert
        assertThrows(OverlapException.class, () -> {
            recintoService.updateRecinto(recinto.getIdRecinto(),recinto);
        });
    }
    //Se testea que el recinto SI se elimine.
    @Test
    void deleteRecintoSI() throws Exception, NotFoundException {
        //Arrange
        String id = "R60";
        //Act
        recintoService.deleteRecinto(id);
        // Assert. Se verifica que el metodo deleteById del repo se ejecutó 1 vez
        verify(recintoRepository, times(1)).deleteById(id);
    }
    //Se testea que el estado del recinto SI se actualice
    @Test
    void updateStateRecintoSI() throws Exception{
        //Arrange
        String id = "R60";
        Boolean newState = true;
        //Act
        recinto1.setBloqueado(newState);
        Recinto resultado = recintoService.updateStateRecinto(id, newState);
        // Assert
        assertNotNull(resultado);
        assertEquals(true, resultado.getBloqueado(), "El estado debería haber cambiado a true");
        // Verificamos que el service realmente llamó al save del repo
        verify(recintoRepository, times(1)).save(any(Recinto.class));
    }
    //Se testea que el estado del recinto NO se actualice. Lanzamiento de NotFoundException
    @Test
    void updateStateRecintoNo() throws Exception{
        //Arrange
        String id = "R999";
        Boolean newState = true;
        //Act
        recinto1.setBloqueado(newState);

        //Assert
        assertThrows(NotFoundException.class, () -> {
            recintoService.updateStateRecinto(id, newState);
        });

    }
}