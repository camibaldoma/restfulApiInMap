package com.inmap.restfulApiInMap.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "esqueleto") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Esqueleto {
    @Id
    @Column(name = "id_esqueleto") // Nombre exacto de la columna PK
    private String idEsqueleto;

    @JsonIgnore
    @JdbcTypeCode(org.hibernate.type.SqlTypes.GEOMETRY)
    @Column(name = "geom")
    private Geometry geometria;

    @JsonProperty("geometria")
    public Map<String, Object> getGeometriaGeoJson() {
        if (geometria == null) return null;

        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", geometria.getGeometryType());
        geoJson.put("coordinates", convertToCoordinates(geometria));
        return geoJson;
    }

    private Object convertToCoordinates(Geometry geometry) {
        if (geometry instanceof org.locationtech.jts.geom.Point p) {
            return new double[]{p.getX(), p.getY()};
        } else if (geometry instanceof org.locationtech.jts.geom.LineString line) {
            return coordsToList(line.getCoordinates());
        } else if (geometry instanceof org.locationtech.jts.geom.MultiLineString mLine) {
            List<List<double[]>> lines = new ArrayList<>();
            for (int i = 0; i < mLine.getNumGeometries(); i++) {
                lines.add(coordsToList(mLine.getGeometryN(i).getCoordinates()));
            }
            return lines;
        } else if (geometry instanceof org.locationtech.jts.geom.Polygon poly) {
            return extractCoordsFromPolygon(poly);
        } else if (geometry instanceof org.locationtech.jts.geom.MultiPolygon mPoly) {
            List<Object> coords = new ArrayList<>();
            for (int i = 0; i < mPoly.getNumGeometries(); i++) {
                coords.add(extractCoordsFromPolygon((org.locationtech.jts.geom.Polygon) mPoly.getGeometryN(i)));
            }
            return coords;
        }
        return null;
    }

    private List<List<double[]>> extractCoordsFromPolygon(org.locationtech.jts.geom.Polygon poly) {
        List<List<double[]>> rings = new ArrayList<>();
        // Exterior ring
        rings.add(coordsToList(poly.getExteriorRing().getCoordinates()));
        // Interior rings (huecos)
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            rings.add(coordsToList(poly.getInteriorRingN(i).getCoordinates()));
        }
        return rings;
    }

    private List<double[]> coordsToList(org.locationtech.jts.geom.Coordinate[] coords) {
        List<double[]> list = new ArrayList<>();
        for (org.locationtech.jts.geom.Coordinate c : coords) {
            list.add(new double[]{c.x, c.y});
        }
        return list;
    }
}
