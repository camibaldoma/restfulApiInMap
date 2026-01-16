package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;

import java.util.List;

public interface PersonalService {
    List<Personal> obtenerTodoPersonal();
    List<PersonalReducido> findAllOrderByApellido();
}
