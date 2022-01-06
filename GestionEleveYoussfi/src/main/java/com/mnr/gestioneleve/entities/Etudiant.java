package com.mnr.gestioneleve.entities;

import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Etudiant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOM",length =30)
    @NotEmpty
    @Size(min=1, max=30,message="le nom doit etre compris entre 1 et 30 au max")
    private String nom;

    @Column(name = "DATENAISSANCE")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateNaissance;

    @NotEmpty
    @Email
    @Column(name = "EMAIL",length =50)
    private String email;

    @Column(name = "PHOTO",length =200)
    private String photo;
    //si vous voulez stoquer la photo ds db
    //private byte[] photo;


    public Etudiant() {
    }
    //on a pas besoin d'id => auto-incremente
    public Etudiant(String nom, Date dateNaissance, String email, String photo) {
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
