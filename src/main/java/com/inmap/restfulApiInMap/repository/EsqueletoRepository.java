package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Esqueleto;
import com.inmap.restfulApiInMap.entity.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsqueletoRepository extends JpaRepository<Esqueleto, String> {
}
