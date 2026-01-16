package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.classes.DestinoReducido;
import com.inmap.restfulApiInMap.entity.Destino;
import com.inmap.restfulApiInMap.entity.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecintoRepository extends JpaRepository<Recinto, String> {

    //JPQL
    //Como ya se configuró la relación entre Destino y Recinto dentro de la entidad Recinto
    // (@ManyToOne), no se necesita hacer el JOIN manualmente en la consulta.
    @Query("SELECT r FROM Recinto r WHERE r.idRecinto = :id")
    List<Recinto> findRecinto(@Param("id") String id);
}
