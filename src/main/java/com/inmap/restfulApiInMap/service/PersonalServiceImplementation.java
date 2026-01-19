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

            //Se crea el ibjeto
            return new UbicacionPersonal(
                    (String) fila[0],
                    (String) fila[1],
                    (String) fila[2],
                    (String) fila[3],
                    jtsGeom
            );
        }).collect(Collectors.toList());
    }
}
