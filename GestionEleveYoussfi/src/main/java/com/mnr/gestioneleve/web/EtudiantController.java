package com.mnr.gestioneleve.web;

import com.mnr.gestioneleve.dao.EtudiantRepository;
import com.mnr.gestioneleve.entities.Etudiant;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
    //pour acceder a ce controller ,utilisez /Etudiant
@RequestMapping(value="/Etudiant")
public class EtudiantController {

        //injection des dependences
    @Autowired
    private EtudiantRepository etudiantRepository;

        //@Value pour injecter les proprietés(qui sont ds application.proprieties), essentiel dans la configuration des fichier
    @Value("${dir.images}")
    private String imageDir;

        //pour acceder a cette methode=> /Etudiant/index
        //Model model:transmettre données entre html et classes
        //@RequestParam(name="page") int p: chercher ds objet request un param appelé "page" et l'affecté a "p"
        //method serve a afficher les pages avec list etudiants
    @RequestMapping(value="/index")
    public String IndexMethod(Model model,@RequestParam(name="page",defaultValue = "0") int p,@RequestParam(name="motCle",defaultValue = "") String mc){
            //findAll() : predefined function jpa with tyle List<T>
            //Page<Etudiant> pageEtudiants= etudiantRepository.findAll(PageRequest.of(p,5));

            //appeler method chercher etudiant pr chercher par mot cle
            //"%"+mc+"%": == like en sql
        Page<Etudiant> pageEtudiants= etudiantRepository.chercherEtudiants("%"+mc+"%",PageRequest.of(p,3));

            //get nombre de pages
        int pageCount=pageEtudiants.getTotalPages();

            //creer tableau de totalPages; le parcourir et attribuer un seul numero a chaque page  pages[i]=i;
        int [] pages=new int [pageCount];
        for(int i=0;i<pageCount;i++)
            pages[i]=i;

        model.addAttribute("pages",pages);
        //stoquer liste des etudiant dans model
        model.addAttribute("pageEtudiants",pageEtudiants);
        //page courante
        model.addAttribute("pageCourante",p);
        model.addAttribute("motCle",mc);
        return "etudiants";
    }



    //----------afficher etudians dans formulaire GET,transmettre empty object au formualaire-----------
    @RequestMapping(value="/form",method= RequestMethod.GET)
    public  String formEtudiant(Model model){
        model.addAttribute("etudiant",new Etudiant());
        return "formEtudiant";
    }



    //----ENREGISTRER ETUDIANT VIA FORMULAIRE POST--------------

    @RequestMapping(value="/saveEtudiant",method=RequestMethod.POST)
        //rechercher dans la requete et les stoquer ds l'objet etudiant
        //@Valid : si les constreints dans entities sont realisés,la methode sera executée
        //BindingResult bindingResult:pour la validation des entities
        //MultipartFile file: recuperer file ou photo
    public  String save(@Valid Etudiant et, BindingResult bindingResult, @RequestParam(name="picture") MultipartFile file ) throws IOException {

        if(bindingResult.hasErrors()){
            return "formEtudiant";
        }
        if(!(file.isEmpty())){
            et.setPhoto(file.getOriginalFilename()); //save ds liste ac son origine nom.
        }
        etudiantRepository.save(et);
        if(!(file.isEmpty())){

                      //creer dossier de stockage
                File dossier = new File(System.getProperty("user.home")+"/images");
                boolean res = dossier.mkdir();

                if(res) {
                    System.out.println("Le dossier a été créé.");
                }
                else {
                    System.out.println("Le dossier existe déja.");
                }

                    //3eme methode:imageDir=>proprieté declaré ds application.properties
                    //il faut creer dossier images dans la destination .
                    //file.transferTo(new File(imageDir+"/"+file.getOriginalFilename()));
                    //enregistrer photo avec id etudiant comme nom de la photo, pr pouvoir la identifier facilement
                    //save ds db avec l'id etudiant,pr eviter repetition de nom,aussi soit facile a recuperer
                file.transferTo(new File(imageDir+et.getId()));

                    //enregister photo avec origine name in db
                    //et.setPhoto(file.getOriginalFilename());


                    //1ere methode
                    //file.transferTo(new File(System.getProperty("user.home")+"/sco"+file.getOriginalFilename())); //==C:/users/mnr/sco/nomefile

                    //2eme methode : utiliser un real path(deconseillé)
                    //file.transferTo(new File("C:\\Users\\pekit\\images"));

        }
        return "redirect:index";
    }


    //*************************CONSULTER UNE PHOTO *********************************
        //fichier retounée contient une photo,il faut utiliser produces=MediaType...
    @RequestMapping(value="/getPhoto",produces= MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody //resultat retourné va etre envoyé ds le corps de la reponse
    public byte[] getPhoto(Long id) throws Exception {
        File f=new File(imageDir+id); //chercher image par id=son nom
            //toByteArray:Gets the contents of a URL as a byte[].
        return IOUtils.toByteArray(new FileInputStream(f));  //org.apache.commons.io.IOUtils;
    }



    //*************************SUPPRIMER ETUDIANT *********************************
    @RequestMapping(value="/delet")
    public String supprimer(Long id){
        etudiantRepository.deleteById(id);
        return "redirect:index";
    }

    //*************************EDITER ETUDIANT *********************************
    @RequestMapping(value="/edit")
    public String editer(Long id,Model model){
        Etudiant et= etudiantRepository.getById(id);
        model.addAttribute("etudiant",et);
        return "EditEtudiant";
    }


    //*************************UPDATE ETUDIANT *********************************
    @RequestMapping(value="/Update", method= RequestMethod.POST)
    public String updateEtudiant(@Valid Etudiant et, BindingResult bindingResult,@RequestParam(name="picture") MultipartFile file) throws IOException {
        if(bindingResult.hasErrors()){
            return "EditEtudiant";
        }
        if(!file.isEmpty()){
            et.setPhoto(file.getOriginalFilename());
        }

        etudiantRepository.save(et);

        if(! file.isEmpty()){
            et.setPhoto(file.getOriginalFilename());
            file.transferTo(new File(imageDir + et.getId()));
        }

        return "redirect:index";
    }



}
