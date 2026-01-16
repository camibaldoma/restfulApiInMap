package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asignacion") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asignacion {

    @Id
    @Column(name = "id_asignacion") // Nombre exacto de la columna PK
    private String idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_destino")
    private Destino destino;

    @ManyToOne
    @JoinColumn(name = "cod_materia")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private Horario horario;
}
