package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Tiene_asociadoServiceImplementation implements Tiene_asociadoService {
    @Autowired
    Tiene_asociadoRepository tiene_asociadoRepository;
    @Override
    public List<Tiene_asociado> obtenerTodosAsociados(){
        return tiene_asociadoRepository.findAll();
    }

}
