package com.inmap.restfulApiInMap.entity;


import com.inmap.restfulApiInMap.classes.EstaId;
import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "esta") // Debe coincidir exactamente con el nombre en Postgres
@IdClass(EstaId.class)
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
