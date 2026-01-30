package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.geolatte.geom.jts.JTS;
import org.locationtech.jts.geom.Geometry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PersonalServiceImplementation implements PersonalService{

    @Autowired
    PersonalRepository personalRepository;
    @Override
    public List<Personal> obtenerTodoPersonal(){
        return personalRepository.findAll();
    }
    @Override
    public List<PersonalReducido> findAllOrderByApellido(){
        return personalRepository.findAllOrderByApellido();
    }

    @Override
    public List<UbicacionPersonal> findUbicacionCompletaNative(String id,String dia,String hora) throws NotFoundException
    {
        List<Object[]> resultados = personalRepository.findUbicacionCompletaNative(id, dia, hora);
        if(resultados == null || resultados.isEmpty())
        {
            if(personalRepository.existsById(id))
            {
                //El personal existe, pero no está presente en el momento de la consulta.
                throw new NotFoundException("El miembro del personal de la facultad con id= "+ id + " no se encuentra presente en este momento en el establecimiento.");
            }
            else{
                //El personal no existe.
                throw new NotFoundException("Ubicación no encontrada. El id no corresponde a ningún miembro del personal de la facultad.");
            }

        }
        return resultados.stream().map(fila -> {
            //Se recibe lo que Hibernate manda (Geolatte)
            org.geolatte.geom.Geometry<?> geoLatteGeom = (org.geolatte.geom.Geometry<?>) fila[4];

            //Se convierte a JTS (Lo que  clase UbicacionPersonal espera)
            Geometry jtsGeom = JTS.to(geoLatteGeom);

            //Se crea el objeto
            return new UbicacionPersonal(
                    (String) fila[0],
                    (String) fila[1],
                    (String) fila[2],
                    (String) fila[3],
                    jtsGeom
            );
        }).collect(Collectors.toList());
    }

    @Override
    public Personal savePersonal(Personal personal) {
        if (personalRepository.existsById(personal.getIdPersonal())) {
            throw new ArgumentNotValidException("El ID ya existe, no se puede usar uno duplicado");
        }
        return personalRepository.save(personal);
    }

    @Override
    public Personal updatePersonal(String idPersonal, Personal personal) {
        Personal personalToUpdate = personalRepository.findById(idPersonal).orElseThrow(() -> new NotFoundException("Miembro del personal no encontrado"));
        if (personal.getIdPersonal() != null && !idPersonal.equals(personal.getIdPersonal())) {
            throw new ArgumentNotValidException("No está permitido cambiar el ID de un miembro del personal.");
        }
        if(Objects.nonNull(personal.getIdPersonal()) && !"".equalsIgnoreCase(personal.getIdPersonal())){
            personalToUpdate.setIdPersonal(personal.getIdPersonal());
        }
        if(Objects.nonNull(personal.getNombrePersonal()) && !"".equalsIgnoreCase(personal.getNombrePersonal())){
            personalToUpdate.setNombrePersonal(personal.getNombrePersonal());
        }
        if(Objects.nonNull(personal.getApellidoPersonal()) && !"".equalsIgnoreCase(personal.getApellidoPersonal())){
            personalToUpdate.setApellidoPersonal(personal.getApellidoPersonal());
        }
        if(Objects.nonNull(personal.getCargoLaboral()) && !"".equalsIgnoreCase(personal.getCargoLaboral())){
            personalToUpdate.setCargoLaboral(personal.getCargoLaboral());
        }
        if(Objects.nonNull(personal.getDni()) && !"".equalsIgnoreCase(personal.getDni())){
            personalToUpdate.setDni(personal.getDni());
        }
        return personalRepository.save(personalToUpdate);
    }

    @Override
    public void deletePersonal(String idPersonal) {
        Personal personalToDelete = personalRepository.findById(idPersonal).orElseThrow(() -> new NotFoundException("Miembro del personal no encontrado"));
        personalRepository.deleteById(idPersonal);
    }
}
