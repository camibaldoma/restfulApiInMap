package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinoServiceImplementation implements DestinoService {
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Destino> obtenerTodosDestinos(){
        return destinoRepository.findAll();
    }
    @Override
    public List<DestinoReducido> findDestino(String id_destino){
        return destinoRepository.findDestino(id_destino);
    }

}

