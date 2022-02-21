package be.vinci.pae.donnees.DAO;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.UtilisateurDTO;
import be.vinci.pae.donnees.DAO.services.ServicesDAL;

import java.sql.*;

public class UtilisateurDAO {
    private DomaineFactory factory;
    private ServicesDAL servicesDAL;

    public UtilisateurDTO rechercheParPseudo(String pseudo){
        servicesDAL.getPs("SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin FROM projet.utilisateurs u WHERE u.pseudo = pseudo");
        UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
        utilisateurDTO.setIdUtilisateur();
    }

    }



