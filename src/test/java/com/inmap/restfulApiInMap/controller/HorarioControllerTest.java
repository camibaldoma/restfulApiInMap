package com.inmap.restfulApiInMap.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.inmap.restfulApiInMap.entity.Horario;
import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.service.HorarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@WebMvcTest(HorarioController.class)
public class HorarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //conversor de Java a JSON
    @MockBean
    private HorarioService horarioService;

    private Horario horarioLunes;
    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JtsModule());
        this.horarioLunes = new Horario();
        this.horarioLunes.setIdHorario("H200");
        this.horarioLunes.setDias("Lunes");
        this.horarioLunes.setHoraInicio("08:00");
        this.horarioLunes.setHoraFin("10:00");
    }
    @Test
    void saveHorario() throws Exception {
        //Arrange
        Mockito.when(horarioService.saveHorario(horarioLunes)).thenReturn(horarioLunes);
        //Act
        mockMvc.perform(post("/guardarHorario")
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(horarioLunes))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.idHorario").value("H200"))
                .andExpect(jsonPath("$.dias").value("Lunes"));

    }
    @Test
    void updateHorario() throws Exception {
        //Arrange
        String id = "H200";
        Horario horarioLunesActualizado = new Horario();
        horarioLunesActualizado.setIdHorario(id);
        horarioLunesActualizado.setDias("LunesActualizado");
        horarioLunesActualizado.setHoraInicio("08:00");
        horarioLunesActualizado.setHoraFin("12:00");
        Mockito.when(horarioService.updateHorario(id,horarioLunesActualizado)).thenReturn(horarioLunesActualizado);
        //Act
        mockMvc.perform(put("/actualizarHorario/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON) // Se envía JSON
                        .content(objectMapper.writeValueAsString(horarioLunesActualizado))) // Se convierte el objeto a JSON String
                //Assert
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.idHorario").value("H200"))
                .andExpect(jsonPath("$.dias").value("LunesActualizado"));

    }
    @Test
    void deleteHorario() throws Exception {
        //Arrange
        String id = "H200";
        //Act
        mockMvc.perform(delete("/eliminarHorario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk());

        // Se verifica que el service realmente fue llamado para borrar ese ID
        verify(horarioService, times(1)).deleteHorario(id);
    }
}*/
