package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;

import java.util.List;

public interface Tiene_asociadoService {
    List<Tiene_asociado> obtenerTodosAsociados();
}
