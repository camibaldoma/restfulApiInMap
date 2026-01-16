package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "horario") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Horario {

    @Id
    @Column(name = "id_horario") // Nombre exacto de la columna PK
    private String idHorario;

    @Column(name = "dias")
    private String dias;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;
}
