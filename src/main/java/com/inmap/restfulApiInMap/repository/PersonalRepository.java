package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.classes.UbicacionPersonal;
import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonalRepository extends JpaRepository<Personal, String> {
    //Consultas nativas de SQL

    @Query(value = "SELECT p.id_personal AS idPersonal, " +
            "(p.nombre_personal || ' ' || p.apellido_personal) AS nombreCompleto, " +
            "p.cargo_laboral AS cargoLaboral " +
            "FROM personal p ORDER BY p.apellido_personal ASC",
            nativeQuery = true)
    List<PersonalReducido> findAllOrderByApellido();

    //No se puede usar JPQL por la UNION
    @Query(value = "SELECT d.id_destino AS idDestino, r.id_recinto AS idRecinto, " +
            "d.nombre_destino AS nombreDestino, 'Clase: ' || m.nombre_materia AS motivo, " +
            "d.geom AS geometria " +
            "FROM recinto r " +
            "JOIN destino d ON r.id_destino = d.id_destino " +
            "JOIN asignacion a ON d.id_destino = a.id_destino " +
            "JOIN materia m ON a.cod_materia = m.cod_materia " +
            "JOIN horario h ON a.id_horario = h.id_horario " +
            "JOIN esta e ON a.id_asignacion = e.id_asignacion " +
            "JOIN personal p ON e.id_personal = p.id_personal " +
            "WHERE p.id_personal = :id " +
            "AND h.dias = :dia AND :hora BETWEEN h.hora_inicio AND h.hora_fin " +
            "UNION " +
            "SELECT d.id_destino AS idDestino, r.id_recinto AS idRecinto, " +
            "d.nombre_destino AS nombreDestino, 'Oficina/Asociado' AS motivo, " +
            "d.geom AS geometria " +
            "FROM recinto r " +
            "JOIN destino d ON r.id_destino = d.id_destino " +
            "JOIN tiene_asociado t ON d.id_destino = t.id_destino " +
            "JOIN personal p ON t.id_personal = p.id_personal " +
            "WHERE p.id_personal = :id " +
            "ORDER BY motivo", nativeQuery = true)
    List<Object[]> findUbicacionCompletaNative(@Param("id") String id,
                                               @Param("dia") String dia,
                                               @Param("hora") String hora);

}



