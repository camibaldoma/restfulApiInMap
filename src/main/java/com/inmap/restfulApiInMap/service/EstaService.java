package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;

import java.util.List;

public interface EstaService {
    List<Esta> obtenerTodosEsta();
    Esta saveEsta(Esta esta);
    void deleteEsta(String idPersonal, String idAsignacion);
}
