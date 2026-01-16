package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;

import java.util.List;

public interface DestinoService {
    List<Destino> obtenerTodosDestinos();
    List<DestinoReducido> findDestino(String id_destino);
}
