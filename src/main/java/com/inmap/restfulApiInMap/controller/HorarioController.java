package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/horarios")
    public List<Horario> obtenerTodosHorarios() {
        return horarioService.obtenerTodosHorarios();
    }
}
