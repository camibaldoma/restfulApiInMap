package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecintoService {
    List<Recinto> obtenerTodosRecintos();
    List<Recinto> findRecinto(String id);
    List<InformacionRecinto> findInformation(String id, String hora, String dia );
}
