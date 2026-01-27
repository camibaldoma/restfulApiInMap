package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/horarios")
    public List<Horario> obtenerTodosHorarios() {
        return horarioService.obtenerTodosHorarios();
    }

    @PostMapping("/guardarHorario")
    public Horario saveHorario(@RequestBody Horario horario) {
        return horarioService.saveHorario(horario);
    }

    @PutMapping("/actualizarHorario/{id}")
    public Horario updateHorario(@PathVariable String id, @RequestBody Horario horario) {
        return horarioService.updateHorario(id, horario);
    }

    @DeleteMapping("/eliminarHorario/{id}")
    public void deleteHorario(@PathVariable String id) {
        horarioService.deleteHorario(id);
    }

}
