package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DestinoServiceImplementation implements DestinoService {
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Destino> obtenerTodosDestinos(){
        return destinoRepository.findAll();
    }
    @Override
    public List<DestinoReducido> findDestino(String id_destino){
        return destinoRepository.findDestino(id_destino);
    }

    @Override
    public Destino saveDestino(Destino destino) {
        return destinoRepository.save(destino);
    }

    @Override
    public Destino updateDestino(String id, Destino destino) {
        Destino destinoToUpdate = destinoRepository.findById(id).get();
        if(Objects.nonNull(destinoToUpdate.getIdDestino()) && !"".equalsIgnoreCase(destinoToUpdate.getIdDestino())){
            destinoToUpdate.setIdDestino(destino.getIdDestino());
        }
        if(Objects.nonNull(destinoToUpdate.getNombreDestino()) && !"".equalsIgnoreCase(destinoToUpdate.getNombreDestino())){
            destinoToUpdate.setNombreDestino(destino.getNombreDestino());
        }
        if(Objects.nonNull(destinoToUpdate.getGeometria())){
            destinoToUpdate.setGeometria(destino.getGeometria());
        }
        return destinoRepository.save(destino);
    }

    @Override
    public void deleteDestino(String id) {
        destinoRepository.deleteById(id);
    }

}

