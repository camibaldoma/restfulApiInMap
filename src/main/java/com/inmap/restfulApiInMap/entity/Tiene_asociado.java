package com.inmap.restfulApiInMap.entity;

import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;


//@Entity define la entidad como persistible
@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "tiene_asociado")
@IdClass(TieneAsociadoId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tiene_asociado {
    @Id
    @Column(name = "id_personal")
    private String idPersonal;

    @Id
    @Column(name = "id_destino")
    private String idDestino;
}
