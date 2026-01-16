package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AsignacionServiceImplementation implements AsignacionService {
    @Autowired
    AsignacionRepository asignacionRepository;
    @Override
    public List<Asignacion> obtenerTodasAsignaciones(){
        return asignacionRepository.findAll();
    }
}
