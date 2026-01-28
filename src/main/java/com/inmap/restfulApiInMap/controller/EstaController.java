package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.EstaRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import com.inmap.restfulApiInMap.service.EstaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EstaController {

    @Autowired
    private EstaService estaService;

    @GetMapping("/esta")
    public List<Esta> obtenerTodosEsta() {
        return estaService.obtenerTodosEsta();
    }

    @PostMapping("/guardarEsta")
    public Esta saveEsta(@RequestBody Esta esta) {
        return estaService.saveEsta(esta);
    }
    @DeleteMapping("/eliminarEsta/{idPersonal}/{idAsignacion}")
    public void eliminarEsta(@PathVariable String idPersonal,@PathVariable String idAsignacion) {
        estaService.deleteEsta(idPersonal, idAsignacion);
    }
}
