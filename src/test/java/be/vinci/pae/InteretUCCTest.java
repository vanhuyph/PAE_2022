package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@TestInstance(Lifecycle.PER_CLASS)
public class InteretUCCTest {

    private InteretUCC interetUCC;
    private DomaineFactory domaineFactory;
    private InteretDTO interetDTO;
    private InteretDAO interetDAO;
    private Date dateJava;


    @BeforeAll
    void initTout() throws java.text.ParseException {
        ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
        this.interetUCC = locator.getService(InteretUCC.class);
        this.domaineFactory = locator.getService(DomaineFactory.class);
        this.interetDAO = locator.getService(InteretDAO.class);
        interetDTO = domaineFactory.getInteret();

        String dateRdvString = "2022-07-20T20:30";
        this.dateJava = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.FRANCE)
                .parse(dateRdvString);

        interetDTO.setDateRdv(dateJava);
        interetDTO.setIdUtilisateur(1);
        interetDTO.setIdObjet(1);

    }

    @Test
    @DisplayName("Test raté : méthode creer un interet avec une mauvaise date de rendez vous. ")
    public void creerUnInteretV1() {


      Mockito.when(interetDAO.ajouterInteret(1, 1, dateJava)).thenReturn(interetDTO);
      assertThrows(BusinessException.class, () -> interetUCC.creerUnInteret(1, 1, null));
    }

    @Test
    @DisplayName("Test réussi : méthode creer un interet avec une mauvaise date de rendez vous. ")
    public void creerUnInteretV2() {


      Mockito.when(interetDAO.ajouterInteret(1, 1, dateJava)).thenReturn(interetDTO);
      assertEquals(interetDTO, interetUCC.creerUnInteret(1, 1, dateJava));
    }

}
