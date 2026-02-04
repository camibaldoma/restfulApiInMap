package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Zonas_a_bloquear;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface Zonas_a_bloquearService {
    List<Zonas_a_bloquear> obtenerTodasZonas();
    List<Zonas_a_bloquear> findZonasBlocked();
    Zonas_a_bloquear updateStateZona(String id, Boolean state) throws NotFoundException, ArgumentNotValidException;
}
