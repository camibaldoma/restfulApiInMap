package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
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
    public List<UbicacionPersonal> findUbicacionCompletaNative(String id,String dia,String hora)
    {
        List<Object[]> resultados = personalRepository.findUbicacionCompletaNative(id, dia, hora);
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
        return personalRepository.save(personal);
    }

    @Override
    public Personal updatePersonal(String idPersonal, Personal personal) {
        Personal personalToUpdate = personalRepository.findById(idPersonal).get();
        //Se verifica que el id del Personal traido de la base de datos no sea nulo
        //ni que la información esté vacía
        if(Objects.nonNull(personalToUpdate.getIdPersonal()) && !"".equalsIgnoreCase(personalToUpdate.getIdPersonal())){
            personalToUpdate.setIdPersonal(personal.getIdPersonal());
        }
        if(Objects.nonNull(personalToUpdate.getNombrePersonal()) && !"".equalsIgnoreCase(personalToUpdate.getNombrePersonal())){
            personalToUpdate.setNombrePersonal(personal.getNombrePersonal());
        }
        if(Objects.nonNull(personalToUpdate.getApellidoPersonal()) && !"".equalsIgnoreCase(personalToUpdate.getApellidoPersonal())){
            personalToUpdate.setApellidoPersonal(personal.getApellidoPersonal());
        }
        if(Objects.nonNull(personalToUpdate.getCargoLaboral()) && !"".equalsIgnoreCase(personalToUpdate.getCargoLaboral())){
            personalToUpdate.setCargoLaboral(personal.getCargoLaboral());
        }
        if(Objects.nonNull(personalToUpdate.getDni()) && !"".equalsIgnoreCase(personalToUpdate.getDni())){
            personalToUpdate.setDni(personal.getDni());
        }
        return personalRepository.save(personalToUpdate);
    }

    @Override
    public void deletePersonal(String idPersonal) {
        personalRepository.deleteById(idPersonal);
    }
}
