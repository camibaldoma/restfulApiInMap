package com.inmap.restfulApiInMap.entity;


import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//@Entity define la entidad como persistible
@Entity
//Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "esta")
@IdClass(EstaId.class) //Se usa para manejar claves primarias compuestas definiendo una clase separada (que implementa Serializable) para el ID
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Esta {
    @Id
    @Column(name = "id_asignacion")
    private String idAsignacion;

    @Id
    @Column(name = "id_personal")
    private String idPersonal;
}
