package com.inmap.restfulApiInMap.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal") // Debe coincidir exactamente con el nombre en Postgres
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Personal {
    @Id
    @Column(name = "id_personal") // Nombre exacto de la columna PK
    private String idPersonal;

    @Column(name = "nombre_personal")
    private String nombrePersonal;

    @Column(name = "apellido_personal")
    private String apellidoPersonal;

    @Column(name = "cargo_laboral")
    private String cargoLaboral;

    @Column(name = "dni")
    private String dni;

    @JsonIgnore // para que no se duplique en el JSON
    private String nombre_completo;

    // Este método genera la columna para el JSON de forma dinámica
    @JsonProperty("nombreCompleto") // Esto le dice a Jackson: "incluye esto en el JSON"
    public String getNombreCompleto() {
        // Si el SQL nativo llenó 'nombre_completo', se usa.
        // Si no (por ejemplo en un findAll común), se calcula.
        if (nombre_completo != null) {
            return nombre_completo.trim();
        }
        return (nombrePersonal != null ? nombrePersonal.trim() : "") + " " +
                (apellidoPersonal != null ? apellidoPersonal.trim() : "");
    }
}
