package com.inmap.restfulApiInMap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inmap.restfulApiInMap.entity.Local;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocalRepository  extends JpaRepository<Local,Long> {
    @Query("SELECT l FROM Local l WHERE l.name = :name")
    Optional<Local> findLocalByNameWithJPQL(String name);

    //Misma consulta pero usando inversión de control de Spring
    Optional<Local> findByName(String name);

    //Ignora mayúsculas o minúsculas, simplemente compara por similitud
    Optional<Local> findByNameIgnoreCase(String name);

}
