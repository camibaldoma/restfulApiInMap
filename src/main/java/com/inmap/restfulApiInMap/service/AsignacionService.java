package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;

import java.util.List;

public interface AsignacionService {
    List<Asignacion> obtenerTodasAsignaciones();
}
