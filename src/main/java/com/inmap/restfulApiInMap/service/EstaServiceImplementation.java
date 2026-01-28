package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import com.inmap.restfulApiInMap.repository.AsignacionRepository;
import com.inmap.restfulApiInMap.repository.EstaRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.Tiene_asociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EstaServiceImplementation implements EstaService {


    @Autowired
    EstaRepository estaRepository;
    @Autowired
    AsignacionRepository asignacionRepository;
    @Autowired
    PersonalRepository personalRepository;
    @Override
    public List<Esta> obtenerTodosEsta(){
        return estaRepository.findAll();
    }

    @Override
    public Esta saveEsta(Esta esta) {
        // Se verifica que los componentes existan en sus tablas base
        boolean existeAsignacion = asignacionRepository.existsById(esta.getIdAsignacion());
        boolean existePersonal = personalRepository.existsById(esta.getIdPersonal());

        if (existeAsignacion && existePersonal) {
            // Se obtienen los detalles de la asignación a la que se quiere vincular al docente
            Asignacion nuevaAsignacion = asignacionRepository.findById(esta.getIdAsignacion())
                    .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

            String dia = nuevaAsignacion.getHorario().getDias();
            String inicio = nuevaAsignacion.getHorario().getHoraInicio();
            String fin = nuevaAsignacion.getHorario().getHoraFin();

            // Se valida si el docente ya tiene otra clase en el mismo horario
            boolean estaOcupado = asignacionRepository.existsChoqueHorarioPersonal(
                    esta.getIdPersonal(), dia, inicio, fin);

            if (estaOcupado) {
                throw new RuntimeException("Error: El docente " + esta.getIdPersonal() +
                        " ya tiene una clase asignada los " + dia + " de " + inicio + " a " + fin);
            }

            //Si no hay choque, guardamos el vínculo
            return estaRepository.save(esta);

        } else {
            throw new RuntimeException("Error: El Personal o la Asignación no existen.");
        }
    }

    @Override
    // Para eliminar con clave compuesta, se necesitan ambos IDs
    public void deleteEsta(String idPersonal, String idAsignacion) {
        EstaId id = new EstaId(idAsignacion, idPersonal);
        estaRepository.deleteById(id);
    }
}
