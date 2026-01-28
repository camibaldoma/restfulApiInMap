package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;

import java.util.List;

public interface Tiene_asociadoService {
    List<Tiene_asociado> obtenerTodosAsociados();
    Tiene_asociado saveTiene_asociado(Tiene_asociado tiene);
    void deleteTiene_asociado(String idPersonal,String idDestino);
}
