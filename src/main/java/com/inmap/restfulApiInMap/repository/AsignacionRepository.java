package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Asignacion;
import com.inmap.restfulApiInMap.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Interfaz del repositorio que extiende JpaRepository. Proporciona varios métodos preconfigurados para realizar operaciones CRUD en la entidad.
@Repository //Marca la interfaz como un repositorio JPA de Spring Data.
public interface AsignacionRepository extends JpaRepository<Asignacion, String> {
}
