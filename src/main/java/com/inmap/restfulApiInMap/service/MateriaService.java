package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Recinto;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface MateriaService {
    List<Materia> obtenerTodasMaterias();
    List<Recinto> findMateria(String id,String hora,String dia);
    Materia saveMateria(Materia materia);
    Materia updateMateria(String id,Materia materia);
    void deleteMateria(String idMateria);
}
