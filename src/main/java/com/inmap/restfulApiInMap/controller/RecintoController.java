package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.service.RecintoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Recinto> findRecinto(@PathVariable String id) throws NotFoundException {
        return recintoService.findRecinto(id);
    }
    @GetMapping("/informacionRecintos/{id}/{hora}/{dia}")
    public List<InformacionRecinto> findInformation(@PathVariable String id, @PathVariable String hora, @PathVariable String dia ) throws NotFoundException {return recintoService.findInformation(id,hora,dia);}

    @PostMapping("/guardarRecinto")
    public Recinto saveRecinto(@Valid  @RequestBody Recinto recinto) throws ArgumentNotValidException {
        return recintoService.saveRecinto(recinto);
    }

    @PutMapping("/actualizarRecinto/{id}")
    public Recinto updateRecinto(@PathVariable String id, @Valid @RequestBody Recinto recinto) throws NotFoundException, ArgumentNotValidException{
        return recintoService.updateRecinto(id, recinto);
    }

    @DeleteMapping("/eliminarRecinto/{id}")
    public void deleteRecinto(@PathVariable String id) throws NotFoundException{
        recintoService.deleteRecinto(id);
    }
}
