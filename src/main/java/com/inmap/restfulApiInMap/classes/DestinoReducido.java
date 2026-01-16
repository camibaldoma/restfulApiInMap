package com.inmap.restfulApiInMap.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.Map;

//No funcionó como Interfaz porque no mapeaba bien la geometría
@Data
@AllArgsConstructor
public class DestinoReducido {
    private String nombreDestino;

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
}
