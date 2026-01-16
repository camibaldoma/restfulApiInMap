package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class DestinoController {

    @Autowired
    private DestinoRepository destinoRepository;

    @GetMapping("/destinos")
    public List<Destino> obtenerTodosDestinos() {
        return destinoRepository.findAll(); // Esto hace el SELECT * FROM destino automáticamente
    }
    @GetMapping("/destinos/{id}")
    public List<DestinoReducido> findDestino(@PathVariable String id){
        return destinoRepository.findDestino(id);
    }
}
