package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.repository.RecintoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
}
