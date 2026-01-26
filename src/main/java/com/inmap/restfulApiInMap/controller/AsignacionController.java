package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AsignacionController {

    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private AsignacionService asignacionService;

    @GetMapping("/asignaciones")
    public List<Asignacion> obtenerTodoasAsignaciones() {
        return asignacionService.obtenerTodasAsignaciones(); // Esto hace el SELECT * FROM destino automáticamente
    }
}
