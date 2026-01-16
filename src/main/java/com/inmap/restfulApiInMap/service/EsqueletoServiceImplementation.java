package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EsqueletoServiceImplementation implements EsqueletoService {
    @Autowired
    EsqueletoRepository esqueletoRepository;
    @Override
    public List<Esqueleto> obtenerEsqueleto(){
        return esqueletoRepository.findAll();
    }
}
