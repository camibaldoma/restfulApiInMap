package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Materia;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.MateriaRepository;

import com.inmap.restfulApiInMap.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping("/materias")
    public List<Materia> obtenerTodasMaterias() {
        return materiaService.obtenerTodasMaterias();
    }
    @GetMapping("/materia/{id}/{hora}/{dia}")
    public List<Recinto> findMateria(@PathVariable String id, @PathVariable String hora, @PathVariable String dia)
    {
        return materiaService.findMateria(id,hora,dia);
    }
    @PostMapping("/guardarMateria")
    public Materia saveMateria(@RequestBody Materia materia) {
        return materiaService.saveMateria(materia);
    }
    @PutMapping("/actualizarMateria/{id}")
    public Materia updateMateria(@PathVariable String id, @RequestBody Materia materia) {
        return materiaService.updateMateria(id, materia);
    }
    @DeleteMapping("/eliminarMateria/{id}")
    public void deleteMateria(@PathVariable String id) {
        materiaService.deleteMateria(id);
    }
}
