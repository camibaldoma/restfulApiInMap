package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.EstaRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EstaServiceImplementation implements EstaService {

    @Autowired
    EstaRepository estaRepository;
    @Override
    public List<Esta> obtenerTodosEsta(){
        return estaRepository.findAll();
    }
}
