package com.aluracursos.libreria_gudentex.repository;

import com.aluracursos.libreria_gudentex.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreIgnoreCase(String nombre);
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :fecha AND a.fechaMuerte >= :fecha")
    List<Autor> autorVivoEnDeterminadoAnio(String fecha);
}
