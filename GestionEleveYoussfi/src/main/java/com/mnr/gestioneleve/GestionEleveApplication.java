package com.mnr.gestioneleve;

import com.mnr.gestioneleve.dao.EtudiantRepository;
import com.mnr.gestioneleve.entities.Etudiant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class GestionEleveApplication {

	public static void main(String[] args) throws ParseException {
		ApplicationContext ctx= SpringApplication.run(GestionEleveApplication.class, args);
		//donnez moi la class qui implemente cette interface
		EtudiantRepository etudiantRepository=ctx.getBean(EtudiantRepository.class);

        //formater date and add  throws ParseException
		DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
		//DateFormat.parse(" "): convertir string to date
		/* comment pour eviter d'ajouter ces etudiants a chaque fois qu'il demarre
		etudiantRepository.save(new Etudiant("ahmed",df.parse("1989-06-12"),"ahmes@gmail.com","ahmed.jpg"));
		etudiantRepository.save(new Etudiant("mounir",df.parse("2005-02-15"),"mnr@gmail.com","mnr.jpg"));
		etudiantRepository.save(new Etudiant("saidou",df.parse("2020-04-28"),"said@gmail.com","said.jpg"));
        */
		/*
		//chercher etudiant par page,ex:page 0
		Page<Etudiant> etds=etudiantRepository.findAll(PageRequest.of(0,5));
		etds.forEach(e -> System.out.println(e.getNom()));
		*/

		//
		Page<Etudiant> etds=etudiantRepository.chercherEtudiants("%d%",PageRequest.of(0,5));
		etds.forEach(e -> System.out.println(e.getNom()));
	}

}
