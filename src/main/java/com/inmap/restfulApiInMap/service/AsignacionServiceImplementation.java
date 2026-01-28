package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service //Se usa para construir una clase de Servicio que habitualmente se conecta a varios repositorios y agrupa su funcionalidad
public class AsignacionServiceImplementation implements AsignacionService {
    @Autowired //inyecta una instancia de la ...Repository en la clase, lo que permite llamar a sus métodos.
    AsignacionRepository asignacionRepository;
    @Autowired
    MateriaRepository materiaRepository;
    @Autowired
    DestinoRepository destinoRepository;
    @Autowired
    HorarioRepository horarioRepository;
    @Autowired
    RecintoRepository recintoRepository;
    @Override
    public List<Asignacion> obtenerTodasAsignaciones(){
        return asignacionRepository.findAll();
    }

    @Override
    public Boolean existsChoqueDeHorario(String idDestino, String dia, String horaInicio, String horaFin, String idActual) {
        return asignacionRepository.existsChoqueDeHorario(idDestino, dia, horaInicio, horaFin, idActual);
    }

    @Override
    public Asignacion saveAsignacion(Asignacion asignacion) {
        // Se verifica que los componentes existan
        boolean existeMateria = materiaRepository.existsById(asignacion.getMateria().getCodMateria());
        boolean existeDestino = destinoRepository.existsById(asignacion.getDestino().getIdDestino());
        boolean existeHorario = horarioRepository.existsById(asignacion.getHorario().getIdHorario());
        if (existeMateria && existeDestino && existeHorario) {
            String dia = asignacion.getHorario().getDias();
            String inicio = asignacion.getHorario().getHoraInicio();
            String fin = asignacion.getHorario().getHoraFin();
            String idDestino = asignacion.getDestino().getIdDestino();
            String idActual = asignacion.getIdAsignacion();
            String codMateria = asignacion.getMateria().getCodMateria();
            boolean estaOcupado = asignacionRepository.existsChoqueDeHorario(idDestino, dia, inicio, fin, idActual);
            boolean materiaDuplicada = asignacionRepository.existsMateriaDuplicada(codMateria,dia,inicio,fin,idActual);
            if (estaOcupado || materiaDuplicada) {
                throw new RuntimeException("Error: El destino " + idDestino + " ya tiene una asignación en el horario " + inicio + " - " + fin);
            }

            // Si pasó todas las validaciones, se guarda
            return asignacionRepository.save(asignacion);
        } else {
            // Se lanza una excepción
            throw new RuntimeException("Error: La Materia, el Destino o el Horario no existen en la base de datos.");
        }
    }

    @Override
    public Asignacion updateAsignacion(String id, Asignacion asignacion) {
        Asignacion asignacionToUpdate = asignacionRepository.findById(id).get();
        // Se verifica que los componentes existan
        boolean existeMateria = materiaRepository.existsById(asignacion.getMateria().getCodMateria());
        boolean existeDestino = destinoRepository.existsById(asignacion.getDestino().getIdDestino());
        boolean existeHorario = horarioRepository.existsById(asignacion.getHorario().getIdHorario());

        if (existeMateria && existeDestino && existeHorario)
        {
            String dia = asignacion.getHorario().getDias();
            String inicio = asignacion.getHorario().getHoraInicio();
            String fin = asignacion.getHorario().getHoraFin();
            String idDestino = asignacion.getDestino().getIdDestino();
            String codMateria = asignacion.getMateria().getCodMateria();
            boolean estaOcupado = asignacionRepository.existsChoqueDeHorario(idDestino, dia, inicio, fin, id);
            boolean materiaDuplicada = asignacionRepository.existsMateriaDuplicada(codMateria,dia,inicio,fin,id);
            if (estaOcupado || materiaDuplicada) {
                throw new RuntimeException("Error: El destino " + idDestino + " ya tiene una asignación en el horario " + inicio + " - " + fin + "o la materia " + codMateria + "tiene otra asignación en ese horario");
            }
            else
            {
                if(Objects.nonNull(asignacionToUpdate.getIdAsignacion()) && !"".equalsIgnoreCase(asignacionToUpdate.getIdAsignacion()))
                {
                    asignacionToUpdate.setIdAsignacion(asignacion.getIdAsignacion());
                }
                if(Objects.nonNull(asignacionToUpdate.getDestino()))
                {
                    asignacionToUpdate.setDestino(asignacion.getDestino());
                }
                if(Objects.nonNull(asignacionToUpdate.getMateria()))
                {
                    asignacionToUpdate.setMateria(asignacion.getMateria());
                }
                if(Objects.nonNull(asignacionToUpdate.getHorario()))
                {
                    asignacionToUpdate.setHorario(asignacion.getHorario());
                }
                return asignacionRepository.save(asignacionToUpdate);
            }
        } else {
            // Se lanza una excepción
            throw new RuntimeException("Error: La Materia, el Destino o el Horario no existen en la base de datos.");
        }
    }

    @Override
    public void deleteAsignacion(String id) {
        asignacionRepository.deleteById(id);
    }
}
