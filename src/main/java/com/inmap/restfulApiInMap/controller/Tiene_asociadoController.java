package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import com.inmap.restfulApiInMap.service.Tiene_asociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Tiene_asociadoController {


    @Autowired
    private Tiene_asociadoService tiene_asociadoService;

    @GetMapping("/asociados")
    public List<Tiene_asociado> obtenerTodosAsociados() {
        return tiene_asociadoService.obtenerTodosAsociados();
    }
}
