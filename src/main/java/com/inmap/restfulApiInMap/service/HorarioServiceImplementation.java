package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
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
        return horarioRepository.save(horario);
    }

    @Override
    public Horario updateHorario(String id, Horario horario) {
        Horario horarioToUpdate = horarioRepository.findById(id).get();
        if(Objects.nonNull(horarioToUpdate.getIdHorario()) && !"".equalsIgnoreCase(horarioToUpdate.getIdHorario())){
            horarioToUpdate.setIdHorario(horario.getIdHorario());
        }
        if(Objects.nonNull(horarioToUpdate.getDias()) && !"".equalsIgnoreCase(horarioToUpdate.getDias())){
            horarioToUpdate.setDias(horario.getDias());
        }
        if(Objects.nonNull(horarioToUpdate.getHoraInicio()) && !"".equalsIgnoreCase(horarioToUpdate.getHoraInicio())){
            horarioToUpdate.setHoraInicio(horario.getHoraInicio());
        }
        if(Objects.nonNull(horarioToUpdate.getHoraFin()) && !"".equalsIgnoreCase(horarioToUpdate.getHoraFin())){
            horarioToUpdate.setHoraFin(horario.getHoraFin());
        }
        return horarioRepository.save(horarioToUpdate);
    }

    @Override
    public void deleteHorario(String id) {
        horarioRepository.deleteById(id);
    }

}
