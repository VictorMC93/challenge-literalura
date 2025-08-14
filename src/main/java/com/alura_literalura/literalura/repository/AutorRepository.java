package com.alura_literalura.literalura.repository;

import com.alura_literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);


    @Query("SELECT a FROM Autor a WHERE (a.fechaDeNacimiento <= :fecha) AND (a.fechaDeFallecimiento >= :fecha)")
    List<Autor> buscarPorYYYY(int fecha);
}
