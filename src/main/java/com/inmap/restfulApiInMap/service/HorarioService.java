package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;

import java.util.List;

public interface HorarioService {
    List<Horario> obtenerTodosHorarios();
    Horario saveHorario(Horario horario);
    Horario updateHorario(String id, Horario horario);
    void deleteHorario(String id);
}
