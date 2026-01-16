package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EsqueletoController {
    @Autowired
    private EsqueletoRepository esqueletoRepository;

    @GetMapping("/esqueleto")
    public List<Esqueleto> obtenerEsqueleto() {
        return esqueletoRepository.findAll();
    }
}
