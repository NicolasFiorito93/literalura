package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a From Autor a WHERE a.fechaNacimiento < :fecha AND a.fechaFallecimiento > :fecha")
    List<Optional<Autor>> findAutorByFecha(Integer fecha);
}
