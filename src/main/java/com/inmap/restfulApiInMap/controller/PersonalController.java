package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import com.inmap.restfulApiInMap.repository.EsqueletoRepository;
import com.inmap.restfulApiInMap.repository.PersonalRepository;
import com.inmap.restfulApiInMap.service.PersonalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/personalCompleto")
    public List<Personal> obtenerTodoPersonal() {
        return personalService.obtenerTodoPersonal();
    }
    @GetMapping("/personal")
    public List<PersonalReducido> findAllOrderByApellido(){
        return personalService.findAllOrderByApellido();
    }
    @GetMapping("/personal/{id}/{hora}/{dia}")
    public List<UbicacionPersonal> findUbicacionCompletaNative(@PathVariable String id,@PathVariable String dia,@PathVariable String hora) throws NotFoundException
    {
        return personalService.findUbicacionCompletaNative(id, dia, hora);
    }
    @PostMapping("/guardarPersonal")
    public Personal savePersonal(@Valid @RequestBody Personal personal) throws ArgumentNotValidException {
        return personalService.savePersonal(personal);
    }
    @PutMapping("/actualizarPersonal/{id}")
    public Personal updatePersonal(@PathVariable String id, @Valid @RequestBody Personal personal) throws NotFoundException,ArgumentNotValidException {
        return personalService.updatePersonal(id, personal);
    }
    @DeleteMapping("/eliminarPersonal/{id}")
    public void deletePersonal(@PathVariable String id) throws NotFoundException {
        personalService.deletePersonal(id);
    }
}
