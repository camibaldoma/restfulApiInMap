package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DestinoServiceTest {

    @Autowired
    private DestinoService destinoService;

    @MockBean
    private DestinoRepository destinoRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {
        // Se crea un Destino (el punto en el mapa)
        Point puntoAula = geometryFactory.createPoint(new Coordinate(-38.0, -57.5));
        Destino aula5 = new Destino();
        aula5.setIdDestino("D50");
        aula5.setNombreDestino("Aula 5");
        aula5.setGeometria(puntoAula);

        DestinoReducido destinoReducido = new DestinoReducido(aula5.getNombreDestino(), aula5.getGeometria());
        Mockito.when(destinoRepository.findDestino(aula5.getIdDestino())).thenReturn(List.of(destinoReducido));
    }

    @Test
    void findDestino() {
        String id = "D50";
        List<DestinoReducido> resultados =  destinoService.findDestino(id);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista no debería estar vacía");
        DestinoReducido resultado = resultados.get(0);
        assertEquals(resultado.getNombreDestino(),"Aula 5");
    }
}