package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MateriaServiceImplementation implements MateriaService {
    @Autowired
    MateriaRepository materiaRepository;
    @Override
    public List<Materia> obtenerTodasMaterias(){
        return materiaRepository.findAll();
    }
    @Override
    public List<Recinto> findMateria(String id,String hora,String dia)
    {
        return materiaRepository.findMateria(id,hora,dia);
    }
}
