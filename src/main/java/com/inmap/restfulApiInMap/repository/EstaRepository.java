package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Esta;
import com.inmap.restfulApiInMap.entity.Tiene_asociado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstaRepository extends JpaRepository<Esta, String> {
}
