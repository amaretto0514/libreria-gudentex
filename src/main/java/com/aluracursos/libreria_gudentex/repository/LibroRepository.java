package com.aluracursos.libreria_gudentex.repository;

import com.aluracursos.libreria_gudentex.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTituloIgnoreCase(String titulo);

    List<Libro> findByIdiomasContaining(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC")
    List<Libro> top5Libros(Pageable pageable);
}
