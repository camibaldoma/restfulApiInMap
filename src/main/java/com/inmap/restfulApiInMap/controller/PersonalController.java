package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/personalCompleto")
    public List<Personal> obtenerTodoPersonal() {
        return personalService.obtenerTodoPersonal();
    }
    @GetMapping("/personal")
    public List<PersonalReducido> findAllOrderByApellido(){
        return personalService.findAllOrderByApellido();
    }
    @GetMapping("/personal/{id}/{hora}/{dia}")
    public List<UbicacionPersonal> findUbicacionCompletaNative(@PathVariable String id,@PathVariable String dia,@PathVariable String hora)
    {
        return personalService.findUbicacionCompletaNative(id, dia, hora);
    }
}
