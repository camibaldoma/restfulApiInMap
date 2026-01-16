package com.inmap.restfulApiInMap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inmap.restfulApiInMap.classes.GeoJsonHelper;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point; // Importante para PostGIS

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "destino") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Destino {
    @Id
    @Column(name = "id_destino") // Nombre exacto de la columna PK
    private String idDestino;

    @Column(name = "nombre_destino")
    private String nombreDestino;

    @JsonIgnore
    @JdbcTypeCode(org.hibernate.type.SqlTypes.GEOMETRY)
    @Column(name = "geom")
    private Geometry geometria;

    @JsonProperty("geometria")
    public Map<String, Object> getGeometriaGeoJson() {
        if (geometria == null) return null;

        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", geometria.getGeometryType());
        geoJson.put("coordinates", GeoJsonHelper.convertToCoordinates(geometria));
        return geoJson;
    }



}
