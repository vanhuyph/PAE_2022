package be.vinci.pae.business.interet;

import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import java.util.Date;

public class InteretUCCImpl implements InteretUCC {

    @Inject
    InteretDAO interetDAO;



    @Override
    public InteretDTO creerUnInteret(int idUtilisateurInteresse, int idObjet, Date dateRdv ) {

        InteretDTO interet = interetDAO.ajouterInteret(idUtilisateurInteresse, idObjet, dateRdv );
        if (interet == null) {
            throw new ExceptionBusiness("L'interet n'a pas pu être créé.",
                    Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
        }

        return interet;
    }
}
