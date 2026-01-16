package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MateriaServiceImplementation implements MateriaService {
    @Autowired
    MateriaRepository materiaRepository;
    @Override
    public List<Materia> obtenerTodasMaterias(){
        return materiaRepository.findAll();
    }
}
