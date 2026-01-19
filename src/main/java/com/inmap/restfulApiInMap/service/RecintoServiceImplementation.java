package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
@Service
public class RecintoServiceImplementation implements RecintoService {
    @Autowired
    RecintoRepository recintoRepository;
    @Override
    public List<Recinto> obtenerTodosRecintos(){
        return recintoRepository.findAll();
    }
    @Override
    public List<Recinto> findRecinto(String id){
        return recintoRepository.findRecinto(id);
    }
    @Override
    public List<InformacionRecinto> findInformation(String id, String hora, String dia ){
        return recintoRepository.findInformation(id,hora,dia);}
}
