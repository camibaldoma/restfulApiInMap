package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalService {
    List<Personal> obtenerTodoPersonal();
    List<PersonalReducido> findAllOrderByApellido();
    List<UbicacionPersonal> findUbicacionCompletaNative(String id, String dia, String hora);
}
