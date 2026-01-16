package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonalController {
    @Autowired
    private PersonalRepository personalRepository;

    @GetMapping("/personalCompleto")
    public List<Personal> obtenerTodoPersonal() {
        return personalRepository.findAll();
    }
    @GetMapping("/personal")
    public List<PersonalReducido> findAllOrderByApellido(){
        return personalRepository.findAllOrderByApellido();
    }
}
