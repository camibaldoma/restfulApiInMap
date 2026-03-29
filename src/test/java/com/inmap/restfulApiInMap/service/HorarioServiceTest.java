package com.inmap.restfulApiInMap.service;


import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.error.ArgumentNotValidException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.HorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*@SpringBootTest
public class HorarioServiceTest {

    @Autowired
    private HorarioService horarioService;

    @MockBean
    private HorarioRepository horarioRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private Horario horarioLunes;
    @BeforeEach
    public void setup() {

        this.horarioLunes = new Horario();
        this.horarioLunes.setIdHorario("H200");
        this.horarioLunes.setDias("Lunes");
        this.horarioLunes.setHoraInicio("08:00");
        this.horarioLunes.setHoraFin("10:00");

        String idExistente = "H300";
        String idInexistente = "H400";

        Mockito.when(horarioRepository.save(any(Horario.class))).thenAnswer(returnsFirstArg());
        Mockito.when(horarioRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(horarioRepository.findById(horarioLunes.getIdHorario())).thenReturn(Optional.of(horarioLunes));
        Mockito.when(horarioRepository.existsById(idInexistente)).thenReturn(false);
    }
    //Se testea que el horario SI se guarde
    @Test
    void saveHorarioSI(){
        //Arrange
        //Act
        Horario save = horarioService.saveHorario(horarioLunes);
        //Assert
        assertNotNull(save,"El objeto guardado no debería ser nulo.");
        assertEquals("H200", save.getIdHorario());
        assertEquals("Lunes", save.getDias());
        assertEquals("08:00", save.getHoraInicio());
        assertEquals("10:00", save.getHoraFin());
    }
    //Se testea que el horario NO se guarde. Lanzamiento de ArgumentNotValidException
    @Test
    void saveHorarioNO(){
        //Arrange
        String idExistente = "H300";
        Horario horarioLunesExistente = new Horario();
        horarioLunesExistente.setIdHorario(idExistente);
        horarioLunesExistente.setDias("LunesActualizado");
        horarioLunesExistente.setHoraInicio("08:00");
        horarioLunesExistente.setHoraFin("12:00");
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            horarioService.saveHorario(horarioLunesExistente);
        });
    }
    //Se testea que el horario SI se actualice
    @Test
    void updateHorarioSI(){
        //Arrange
        String id = "H200";
        Horario horarioLunesActualizado = new Horario();
        horarioLunesActualizado.setIdHorario(id);
        horarioLunesActualizado.setDias("LunesActualizado");
        horarioLunesActualizado.setHoraInicio("08:00");
        horarioLunesActualizado.setHoraFin("12:00");
        //Act
        Horario update = horarioService.updateHorario(id,horarioLunesActualizado);
        //Assert
        assertNotNull(update,"El objeto guardado no debería ser nulo.");
        assertEquals("H200", update.getIdHorario());
        assertEquals("08:00", update.getHoraInicio());
        assertEquals("12:00", update.getHoraFin());
    }
    //Se testea que NO se actualice el horario. Lanzamiento de NotFoundException
    @Test
    void updateHorarioNO(){
        //Arrange
        String idInexistente = "H400";
        //Act y Assert
        assertThrows(NotFoundException.class, () -> {
            horarioService.updateHorario(idInexistente,horarioLunes);
        });
    }
    //Se testea que no se actualice el horario. Lanzamiento de ArgumentNotValidException
    @Test
    void updateHorarioNo_1(){
        //Arrange
        String idExistente = "H300";
        Horario horarioLunesExistente = new Horario();
        horarioLunesExistente.setIdHorario(idExistente);
        horarioLunesExistente.setDias("LunesActualizado");
        horarioLunesExistente.setHoraInicio("08:00");
        horarioLunesExistente.setHoraFin("12:00");
        Mockito.when(horarioRepository.findById(horarioLunesExistente.getIdHorario())).thenReturn(Optional.of(horarioLunesExistente));
        //Act y Assert
        assertThrows(ArgumentNotValidException.class, () -> {
            horarioService.updateHorario(idExistente,horarioLunes);
        });
    }
    //Se testea que el horario SI se elimine.
    @Test
    void deleteHorarioSI(){
        //Arrange
        String id = "H200";
        //Act
        horarioService.deleteHorario(id);
        // Assert. Se verifica que el metodo deleteById del repo se ejecutó 1 vez
        verify(horarioRepository, times(1)).deleteById(id);
    }
}*/
