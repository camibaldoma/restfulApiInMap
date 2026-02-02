package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.classes.InformacionRecinto;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface RecintoRepository extends JpaRepository<Recinto, String> {
    //@Query: Define una consulta JPQL personalizada o SQL nativa para una entidad.
    //JPQL
    //Como ya se configuró la relación entre Destino y Recinto dentro de la entidad Recinto
    // (@ManyToOne), no se necesita hacer el JOIN manualmente en la consulta.
    @Query("SELECT r FROM Recinto r WHERE r.idRecinto = :id")
    List<Recinto> findRecinto(@Param("id") String id);
    @Query("SELECT new com.inmap.restfulApiInMap.classes.InformacionRecinto(" +
            "d.idDestino, r.idRecinto, d.nombreDestino, m.nombreMateria, d.geometria) " +
            "FROM Recinto r " +
            "JOIN r.destino d " +
            "LEFT JOIN Asignacion a ON d = a.destino " +
            "LEFT JOIN a.materia m " +
            "LEFT JOIN a.horario h ON a.horario = h " +
            "WHERE r.idRecinto = :id " +
            "AND (" +
            "(h.dias = :dia AND :hora BETWEEN h.horaInicio AND h.horaFin) " +
            "OR a.idAsignacion IS NULL OR h.idHorario IS NULL" +
            ")")
    List<InformacionRecinto> findInformation(@Param("id") String id, @Param("hora") String hora, @Param("dia") String dia);
    @Query("SELECT COUNT(r) > 0 FROM Recinto r WHERE r.destino.idDestino = :idDestino AND r.idRecinto <> :idRecinto")
    boolean existsDestinoInRecinto(@Param("idDestino") String idDestino, @Param("idRecinto") String idRecinto);
}






