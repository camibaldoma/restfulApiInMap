package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecintoService {
    List<Recinto> obtenerTodosRecintos();
    List<Recinto> findRecinto(String id) throws NotFoundException;
    List<InformacionRecinto> findInformation(String id, String hora, String dia ) throws NotFoundException;
    Recinto saveRecinto(Recinto recinto) throws ArgumentNotValidException;
    Recinto updateRecinto(String id, Recinto recinto) throws NotFoundException,ArgumentNotValidException;
    void deleteRecinto(String id) throws NotFoundException;
}
