package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service //Se usa para construir una clase de Servicio que habitualmente se conecta a varios repositorios y agrupa su funcionalidad
public class AsignacionServiceImplementation implements AsignacionService {
    @Autowired //inyecta una instancia de la ...Repository en la clase, lo que permite llamar a sus métodos.
    AsignacionRepository asignacionRepository;
    @Override
    public List<Asignacion> obtenerTodasAsignaciones(){
        return asignacionRepository.findAll();
    }
}
