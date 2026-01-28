package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;

import java.util.List;

public interface AsignacionService {
    List<Asignacion> obtenerTodasAsignaciones();
    Boolean existsChoqueDeHorario(String idDestino,String dia, String horaInicio, String horaFin, String idActual);
    Asignacion saveAsignacion(Asignacion asignacion);
    Asignacion updateAsignacion(String id, Asignacion asignacion);
    void deleteAsignacion(String id);
}
