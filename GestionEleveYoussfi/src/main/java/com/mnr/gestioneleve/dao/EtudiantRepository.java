package com.mnr.gestioneleve.dao;

import com.mnr.gestioneleve.entities.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {
    //setlect * from Etudiant where Nom =n;il est predefinie ds JpaRepository
    //public List<Etudiant> findByNom(String n);
    //pageable = interface contenant n° page et taille,chercher les enregistrement
    public Page<Etudiant> findByNom(String n,Pageable pageable);

    //chercher un mot proche et page par page
    @Query("select e from Etudiant e  where e.nom like:x")
    public Page<Etudiant> chercherEtudiants(@Param("x")String mc, Pageable pageable);

    //chercher list etudiant entre 2 dates
    @Query("select e from Etudiant e where e.dateNaissance >:x and e.dateNaissance<:y")
    List<Etudiant> chercherEtudiants(@Param("x")String mc,@Param("y")Date d2);


}
