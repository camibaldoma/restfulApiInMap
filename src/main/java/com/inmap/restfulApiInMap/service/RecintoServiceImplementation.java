package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.DestinoRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class RecintoServiceImplementation implements RecintoService {
    @Autowired
    RecintoRepository recintoRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Override
    public List<Recinto> obtenerTodosRecintos(){
        return recintoRepository.findAll();
    }
    @Override
    public List<Recinto> findRecinto(String id) throws NotFoundException {
        List<Recinto> recintos = recintoRepository.findRecinto(id);
        if(recintos == null || recintos.isEmpty())
        {
            throw new NotFoundException("Recinto no encontrado");
        }
        return recintos;
    }
    @Override
    public List<InformacionRecinto> findInformation(String id, String hora, String dia ) throws NotFoundException {
        List<InformacionRecinto> lista = recintoRepository.findInformation(id, hora, dia);

        if (lista.isEmpty() || lista == null) {
            //Si no se encontró información puede ser por dos motivos:
            //1) o el id no existe
            //2) o no hay información en ese momento del recinto porque está desocupado
            if(recintoRepository.existsById(id))
            {
                throw new NotFoundException("No se encontró información para el recinto: " + id + ". En este momento se encuentra desocupado.");
            }
            else {
                throw new NotFoundException("No se encontró información para el recinto: " + id + " porque el id no corresponde a un recinto disponible.");
            }

        }

        return lista;
    }

    @Override
    public Recinto saveRecinto(Recinto recinto) {
        if (recintoRepository.existsById(recinto.getIdRecinto())) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        return recintoRepository.save(recinto);
    }

    @Override
    public Recinto updateRecinto(String id, Recinto recinto) throws NotFoundException {
        Recinto recintoToUpdate = recintoRepository.findById(id).orElseThrow(() -> new NotFoundException("Recinto no encontrado"));
        if (recinto.getIdRecinto() != null && !id.equals(recinto.getIdRecinto())) {
            throw new ArgumentNotValidException("No está permitido cambiar el ID de un recinto.");
        }
        if (Objects.nonNull(recinto.getIdRecinto()) && !"".equalsIgnoreCase(recintoToUpdate.getIdRecinto())) {
            //El id del recinto no puede actualizarse
            //recintoToUpdate.setIdRecinto(recinto.getIdRecinto());
        }
        if (Objects.nonNull(recinto.getDestino())) {
            recintoToUpdate.setDestino(recinto.getDestino());
        }
        if (Objects.nonNull(recinto.getGeometria())) {
            recintoToUpdate.setGeometria(recinto.getGeometria());
        }
        return recintoRepository.save(recintoToUpdate);
    }
    @Override
    public void deleteRecinto(String id) throws NotFoundException {
        Recinto recintoToDelete = recintoRepository.findById(id).orElseThrow(() -> new NotFoundException("Recinto no encontrado"));;
        recintoRepository.deleteById(id);
    }
}
