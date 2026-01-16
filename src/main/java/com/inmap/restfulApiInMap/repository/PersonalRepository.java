package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Personal;
import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.interfaces.PersonalReducido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonalRepository extends JpaRepository<Personal, String> {
    //Consultas nativas de SQL

    @Query(value = "SELECT p.id_personal AS idPersonal, " +
            "(p.nombre_personal || ' ' || p.apellido_personal) AS nombreCompleto, " +
            "p.cargo_laboral AS cargoLaboral " +
            "FROM personal p ORDER BY p.apellido_personal ASC",
            nativeQuery = true)
    List<PersonalReducido> findAllOrderByApellido();
}
