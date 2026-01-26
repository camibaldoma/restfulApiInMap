package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.service.RecintoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecintoController {
    @Autowired
    private RecintoService recintoService;

    @GetMapping("/recintos")
    public List<Recinto> obtenerTodosRecintos() {
        return recintoService.obtenerTodosRecintos();
    }
    @GetMapping("/recintos/{id}")
    public List<Recinto> findRecinto(@PathVariable String id){
        return recintoService.findRecinto(id);
    }
    @GetMapping("/informacionRecintos/{id}/{hora}/{dia}")
    public List<InformacionRecinto> findInformation(@PathVariable String id, @PathVariable String hora, @PathVariable String dia ){return recintoService.findInformation(id,hora,dia);}

}
