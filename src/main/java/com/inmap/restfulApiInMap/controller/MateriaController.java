package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Materia;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.MateriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MateriaController {
    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping("/materias")
    public List<Materia> obtenerTodasMaterias() {
        return materiaRepository.findAll();
    }
    @GetMapping("/materia/{id}/{hora}/{dia}")
    public List<Recinto> findMateria(@PathVariable String id, @PathVariable String hora, @PathVariable String dia)
    {
        return materiaRepository.findMateria(id,hora,dia);
    }
}
