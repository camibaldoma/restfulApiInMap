package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Recinto;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tiene_asociadoRepository extends JpaRepository<Tiene_asociado, String> {
}
