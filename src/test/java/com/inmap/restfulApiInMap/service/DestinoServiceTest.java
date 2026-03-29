package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DestinoServiceTest {

    @Autowired
    private DestinoService destinoService;

    @MockBean
    private DestinoRepository destinoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Destino aulaNueva;
    private Destino aulaTest;
    private Destino aulaTest2;
    @BeforeEach
    void setUp() {

        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        this.aulaTest = new Destino();
        this.aulaTest.setIdDestino("D100");
        this.aulaTest.setNombreDestino("Aula Test");
        this.aulaTest.setGeometria(puntoAula);

        Point puntoAula1 = geometryFactory.createPoint(new Coordinate(-40.0, -57.5));
        this.aulaNueva = new Destino();
        this.aulaNueva.setIdDestino("D200");
        this.aulaNueva.setNombreDestino("Aula Nueva");
        this.aulaNueva.setGeometria(puntoAula1);

        Point puntoAula2 = geometryFactory.createPoint(new Coordinate(-50.0, -57.5));
        this.aulaTest2 = new Destino();
        this.aulaTest2.setIdDestino("D101");
        this.aulaTest2.setNombreDestino("Aula Test2");
        this.aulaTest2.setGeometria(puntoAula2);

        String idInexistente = "D999";
        String idExistente = "D300";

        DestinoReducidoDTO destinoReducido = new DestinoReducidoDTO(aulaTest.getNombreDestino(), aulaTest.getGeometria());
        DestinoReducidoDTO destinoReducido1 = new DestinoReducidoDTO(aulaNueva.getNombreDestino(), aulaNueva.getGeometria());
        DestinoReducidoDTO destinoReducido2 = new DestinoReducidoDTO(aulaTest2.getNombreDestino(), aulaTest2.getGeometria());
        Mockito.when(destinoRepository.findDestino(aulaTest.getIdDestino())).thenReturn(List.of(destinoReducido));
        Mockito.when(destinoRepository.findDestino(idInexistente)).thenReturn(null);
        Mockito.when(destinoRepository.findDestino(aulaNueva.getIdDestino())).thenReturn(List.of(destinoReducido1));
        Mockito.when(destinoRepository.save(aulaNueva)).thenReturn(aulaNueva);
        Mockito.when(destinoRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(destinoRepository.save(aulaTest2)).thenReturn(aulaTest2);
        Mockito.when(destinoRepository.findDestino(aulaTest2.getIdDestino())).thenReturn(List.of(destinoReducido2));
        Mockito.when(destinoRepository.findById(aulaTest.getIdDestino())).thenReturn(Optional.of(aulaTest));
        Mockito.when(destinoRepository.findById(idInexistente)).thenReturn(Optional.empty());
        Mockito.when(destinoRepository.findById(aulaTest2.getIdDestino())).thenReturn(Optional.of(aulaTest2));
    }
    //Se testea que se el destino SI se encuentre
    @Test
    void findDestinoSI() throws NotFoundException {
        //Arrange
        String id = "D100";
        //Act
        List<DestinoReducidoDTO> resultados = destinoService.findDestino(id);
        //Assert
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        DestinoReducidoDTO resultado = resultados.get(0);
        assertEquals(resultado.getNombreDestino(), "Aula Test");
    }
    //Se testea que el destino NO se encuentre. Lanzamiento de NotFoundException
    @Test
    void findDestinoNO() throws NotFoundException {
        //Arrange
        String id = "D999";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            destinoService.findDestino(id);
        });
    }
    //Se testea que el destino SI se guarde
    @Test
    void saveDestinoSI() throws Exception, ArgumentNotValidException {
        //Arrange
        //Act
        Destino save = destinoService.saveDestino(aulaNueva);
        //Assert
        assertNotNull(save,"El objeto guardado no debería ser nulo.");
        assertEquals("D200", save.getIdDestino());
        //Verificación de la persistencia real
        List<DestinoReducidoDTO> resultados =  destinoService.findDestino(aulaNueva.getIdDestino());
        assertNotNull(resultados,"La lista no debería ser nula.");
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        DestinoReducidoDTO resultado = resultados.get(0);
        assertEquals("Aula Nueva", resultado.getNombreDestino());
    }
    //Se testea que el destino NO se guarde. Lanzamiento ArgumentNotValidException
    @Test
    void saveDestinoNO() throws Exception, ArgumentNotValidException {
        //Arrange
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aulaIdExistente = new Destino();
        aulaIdExistente.setIdDestino("D300");
        aulaIdExistente.setNombreDestino("Aula IdExistente");
        aulaIdExistente.setGeometria(puntoAula);
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            destinoService.saveDestino(aulaIdExistente);
        });
    }
    //Se testea que el destino SI se pueda actualizar.
    @Test
    void updateDestinoSI() throws Exception, NotFoundException,ArgumentNotValidException  {
        //Arrange
        String id = "D101";
        //Act
        Destino update = destinoService.updateDestino(id,aulaTest2);
        //Assert
        assertNotNull(update,"El objeto actualizado no debería ser nulo.");
        assertEquals("D101", update.getIdDestino());
        //Verificación de la persistencia real
        List<DestinoReducidoDTO> resultados =  destinoService.findDestino(aulaTest2.getIdDestino());
        assertNotNull(resultados,"La lista no debería ser nula.");
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        DestinoReducidoDTO resultado = resultados.get(0);
        assertEquals("Aula Test2", resultado.getNombreDestino());
    }
    //Se testea que el destino NO se pueda actualizar. Lanzamiento de NotFoundException
    @Test
    void updateDestinoNO() throws Exception, NotFoundException,ArgumentNotValidException  {
        //Arrange
        String id = "D999";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            destinoService.updateDestino(id,aulaTest2);
        });
    }
    //Se testea que el destino NO se pueda actualizar. Lanzamiento de ArgumentNotValidException
    @Test
    void updateDestinoNO_1() throws Exception, NotFoundException,ArgumentNotValidException  {

        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            destinoService.updateDestino(aulaTest2.getIdDestino(),aulaNueva);
        });
    }
    //Se testea que el destino SI se elimine.
    @Test
    void deleteDestinoSI() throws Exception, NotFoundException {
        //Arrange
        String id = "D100";
        //Act
        destinoService.deleteDestino(id);
        // Assert. Se verifica que el metodo deleteById del repo se ejecutó 1 vez
        Mockito.verify(destinoRepository, Mockito.times(1)).deleteById(id);
    }
}