package com.inmap.restfulApiInMap.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.Map;

@Data

public class InformacionRecinto {
    private String idDestino;
    private String idRecinto;
    private String nombreDestino;
    private String nombreMateria;

    // Hibernate inyectará aquí el objeto JTS correctamente
    private Geometry geometria;

    @JsonProperty("geometria")
    public Map<String, Object> getGeometriaGeoJson() {
        if (geometria == null) return null;
        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", geometria.getGeometryType());
        geoJson.put("coordinates", GeoJsonHelper.convertToCoordinates(geometria));
        return geoJson;
    }
    public InformacionRecinto(String idDestino, String idRecinto, String nombreDestino, String nombreMateria, Geometry geometria) {
        this.idDestino = idDestino;
        this.idRecinto = idRecinto;
        this.nombreDestino = nombreDestino;
        this.nombreMateria = nombreMateria;
        this.geometria = geometria;
    }
}
