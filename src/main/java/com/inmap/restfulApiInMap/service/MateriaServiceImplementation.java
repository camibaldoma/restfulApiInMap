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
import java.util.Objects;

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

    @Override
    public Materia saveMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    @Override
    public Materia updateMateria(String id,Materia materia) {
        Materia materiaToUpdate = materiaRepository.findById(id).get();
        if(Objects.nonNull(materiaToUpdate.getCodMateria()) && !"".equalsIgnoreCase(materiaToUpdate.getCodMateria())){
            materiaToUpdate.setCodMateria(materia.getCodMateria());
        }
        if(Objects.nonNull(materiaToUpdate.getNombreMateria()) && !"".equalsIgnoreCase(materiaToUpdate.getNombreMateria())){
            materiaToUpdate.setNombreMateria(materia.getNombreMateria());
        }
        return materiaRepository.save(materiaToUpdate);
    }

    @Override
    public void deleteMateria(String idMateria) {
        materiaRepository.deleteById(idMateria);
    }
}
