package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HorarioServiceImplementation implements HorarioService {
    @Autowired
    HorarioRepository horarioRepository;
    @Override
    public List<Horario> obtenerTodosHorarios(){
        return horarioRepository.findAll();
    }

}
