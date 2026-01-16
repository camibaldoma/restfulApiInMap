package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecintoController {
    @Autowired
    private RecintoRepository recintoRepository;

    @GetMapping("/recintos")
    public List<Recinto> obtenerTodosRecintos() {
        return recintoRepository.findAll();
    }
    @GetMapping("/recintos/{id}")
    public List<Recinto> findRecinto(@PathVariable String id){
        return recintoRepository.findRecinto(id);
    }
}
