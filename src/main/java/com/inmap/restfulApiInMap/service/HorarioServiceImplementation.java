package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import com.inmap.restfulApiInMap.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HorarioServiceImplementation implements HorarioService {
    @Autowired
    HorarioRepository horarioRepository;
    @Override
    public List<Horario> obtenerTodosHorarios(){
        return horarioRepository.findAll();
    }

    @Override
    public Horario saveHorario(Horario horario) {
        if (horarioRepository.existsById(horario.getIdHorario())) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        return horarioRepository.save(horario);
    }

    @Override
    public Horario updateHorario(String id, Horario horario) {
        Horario horarioToUpdate = horarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        if (horario.getIdHorario() != null && !id.equals(horario.getIdHorario())) {
            throw new ArgumentNotValidException("No está permitido cambiar el ID de un horario.");
        }
        if(Objects.nonNull(horario.getIdHorario()) && !"".equalsIgnoreCase(horario.getIdHorario())){
            horarioToUpdate.setIdHorario(horario.getIdHorario());
        }
        if(Objects.nonNull(horario.getDias()) && !"".equalsIgnoreCase(horario.getDias())){
            horarioToUpdate.setDias(horario.getDias());
        }
        if(Objects.nonNull(horario.getHoraInicio()) && !"".equalsIgnoreCase(horario.getHoraInicio())){
            horarioToUpdate.setHoraInicio(horario.getHoraInicio());
        }
        if(Objects.nonNull(horario.getHoraFin()) && !"".equalsIgnoreCase(horario.getHoraFin())){
            horarioToUpdate.setHoraFin(horario.getHoraFin());
        }
        return horarioRepository.save(horarioToUpdate);
    }

    @Override
    public void deleteHorario(String id) {
        Horario horarioToDelete = horarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        horarioRepository.deleteById(id);
    }

}
