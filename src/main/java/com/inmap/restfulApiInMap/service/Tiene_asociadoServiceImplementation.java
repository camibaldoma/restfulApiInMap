package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Tiene_asociadoServiceImplementation implements Tiene_asociadoService {
    @Autowired
    Tiene_asociadoRepository tiene_asociadoRepository;
    @Autowired
    PersonalRepository personalRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Tiene_asociado> obtenerTodosAsociados(){
        return tiene_asociadoRepository.findAll();
    }

    @Override
    public Tiene_asociado saveTiene_asociado(Tiene_asociado tiene) {
        boolean existeDestino = destinoRepository.existsById(tiene.getIdDestino());
        boolean existePersonal = personalRepository.existsById(tiene.getIdPersonal());
        if(existeDestino && existePersonal){
            // Regla de Negocio: Un personal solo puede tener UN destino asociado (oficina)
            if(tiene_asociadoRepository.existsAsociacion(tiene.getIdPersonal())){
                throw new RuntimeException("Error: El personal "+ tiene.getIdPersonal() +" ya tiene asociado un destino fijo." );
            }
            else {
                return tiene_asociadoRepository.save(tiene);
            }
        }
        else {
            throw new RuntimeException("Error: El Personal o el Destino no existen." );
        }
    }


    @Override
    public void deleteTiene_asociado(String idPersonal, String idDestino) {
        TieneAsociadoId id = new TieneAsociadoId(idPersonal,idDestino);
        tiene_asociadoRepository.deleteById(id);
    }

}
