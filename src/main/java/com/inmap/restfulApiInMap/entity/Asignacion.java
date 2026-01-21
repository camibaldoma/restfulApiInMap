package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity define la entidad como persistible
@Entity
//Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "asignacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asignacion {

    @Id //Marca el atributo como la clave primaria de la entidad
    @Column(name = "id_asignacion") // Permite personalizar el mapeo entre el atributo de la clase y la columna en la tabla de la base de datos. Nombre exacto de la columna
    private String idAsignacion;

    @ManyToOne //Se encarga de generar una relación de muchos a uno
    @JoinColumn(name = "id_destino") //Sirve en JPA para hacer referencia a la columna que es clave externa en la tabla y que se encarga de definir la relación
    private Destino destino;

    @ManyToOne
    @JoinColumn(name = "cod_materia")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private Horario horario;
}
