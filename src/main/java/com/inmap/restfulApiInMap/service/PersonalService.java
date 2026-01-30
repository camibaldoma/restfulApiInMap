package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalService {
    List<Personal> obtenerTodoPersonal();
    List<PersonalReducido> findAllOrderByApellido();
    List<UbicacionPersonal> findUbicacionCompletaNative(String id, String dia, String hora) throws NotFoundException;
    Personal savePersonal(Personal personal) throws ArgumentNotValidException;
    Personal updatePersonal(String idPersonal, Personal personal) throws NotFoundException,ArgumentNotValidException;
    void deletePersonal(String idPersonal) throws NotFoundException;
}
