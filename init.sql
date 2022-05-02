DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;

CREATE TABLE projet.adresses
(
    id_adresse  SERIAL PRIMARY KEY,
    rue         VARCHAR(255) NOT NULL,
    numero      INTEGER      NOT NULL,
    boite       VARCHAR(10),
    code_postal INTEGER      NOT NULL,
    commune     VARCHAR(30),
    version     INTEGER      NOT NULL
);

CREATE TABLE projet.types_objets
(
    id_type SERIAL PRIMARY KEY,
    nom     VARCHAR(50) NOT NULL
);

CREATE TABLE projet.utilisateurs
(
    id_utilisateur     SERIAL PRIMARY KEY,
    pseudo             VARCHAR(25)                                     NOT NULL,
    nom                VARCHAR(50)                                     NOT NULL,
    prenom             VARCHAR(50)                                     NOT NULL,
    mdp                VARCHAR(255)                                    NOT NULL,
    gsm                VARCHAR(15),
    est_admin          BOOLEAN                                         NOT NULL,
    adresse            INTEGER REFERENCES projet.adresses (id_adresse) NOT NULL,
    etat_inscription   VARCHAR(10)                                     NOT NULL,
    commentaire        VARCHAR(255) NULL,
    version            INTEGER                                         NOT NULL,
    nb_objet_offert    INTEGER                                         NOT NULL,
    nb_objet_donne     INTEGER                                         NOT NULL,
    nb_objet_recu      INTEGER                                         NOT NULL,
    nb_objet_abandonne INTEGER                                         NOT NULL
);

CREATE TABLE projet.objets
(
    id_objet    SERIAL PRIMARY KEY,
    etat_objet  VARCHAR(9)                                              NOT NULL,
    type_objet  INTEGER REFERENCES projet.types_objets (id_type)        NOT NULL,
    description VARCHAR(255)                                            NOT NULL,
    offreur     INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
    receveur    INTEGER REFERENCES projet.utilisateurs (id_utilisateur),
    photo       VARCHAR(255),
    version     INTEGER                                                 NOT NULL,
    vue         BOOLEAN                                                 NOT NULL
);

CREATE TABLE projet.interets
(
    utilisateur     INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
    objet           INTEGER REFERENCES projet.objets (id_objet)             NOT NULL,
    date            DATE                                                    NOT NULL,
    version         INTEGER                                                 NOT NULL,
    vue             BOOLEAN                                                 NOT NULL,
    receveur_choisi BOOLEAN NULL,
    venu_chercher   BOOLEAN NULL,
    PRIMARY KEY (utilisateur, objet)
);

CREATE TABLE projet.evaluations
(
    id_evaluation SERIAL PRIMARY KEY,
    objet         INTEGER REFERENCES projet.objets (id_objet) NOT NULL,
    note          INTEGER,
    commentaire   VARCHAR(255)

);

CREATE TABLE projet.offres
(
    id_offre      SERIAL PRIMARY KEY,
    id_objet      INTEGER REFERENCES projet.objets (id_objet) NOT NULL,
    date_offre    TIMESTAMP                                   NOT NULL,
    plage_horaire VARCHAR(255)                                NOT NULL,
    version       INTEGER                                     NOT NULL
);

INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue de l’Eglise', 11, 'B1', 4987, 'Stoumont', 0);
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue de Renkin', 7, NULL, 4800, 'Verviers', 0);
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue Haute Folie', 6, 'A103', 4800, 'Verviers', 0);
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Haut-Vinâve', 13, NULL, 4845, 'Jalhay', 0);

INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'caro', 'Line', 'Caroline',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 1, 'Refusé',
        'Il faudra patienter un jour ou deux.', 0, 0, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'achil', 'Ile', 'Achille',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 2,
        'Confirmé', NULL, 0, 0, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bazz', 'Ile', 'Basile',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 3, 'Confirmé',
        NULL, 0, 2, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bri', 'Lehmann', 'Brigitte',
        '$2a$10$W0IiogOO7ef5/Kw.GdmEkO46mtg6VSeDsV5SYc4Dzmp4XnnOBUAkC', NULL, true, 4, 'Confirmé',
        NULL, 0, 1, 0, 0, 0);

INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Accessoires pour animaux domestiques');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Accessoires pour voiture');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Décoration');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Jouets');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Literie');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Matériel de cuisine');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Matériel de jardinage');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Meuble');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Plantes');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Produits cosmétiques');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Vélo, trottinette');
INSERT INTO projet.types_objets
VALUES (DEFAULT, 'Vêtements');

INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 3, 'Décorations de Noël de couleur rouge.', 3, NULL,
        'christmas-1869533_640.png', 0, false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 3, 'Cadre représentant un chien noir sur un fond noir.', 3, NULL,
        'dog-4118585_640.jpg', 0, false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 8, 'Ancien bureau d’écolier.', 4, NULL, 'BureauEcolier-7.JPG', 0, false);

INSERT INTO projet.offres
VALUES (DEFAULT, 1, '21-03-22', 'Mardi de 17h à 22h', 0);
INSERT INTO projet.offres
VALUES (DEFAULT, 2, '25-03-22', 'Lundi de 18h à 22h', 0);
INSERT INTO projet.offres
VALUES (DEFAULT, 3, '25-03-22', 'Tous les jours de 15h à 18h', 0);

INSERT INTO projet.interets
VALUES (3, 3, '25-03-22', 0, false, false, NULL);
INSERT INTO projet.interets
VALUES (2, 3, '25-03-22', 0, false, false, NULL);

/*UPDATE projet.objets SET etat_objet = 'Intéressé' WHERE id_objet = 3;

UPDATE projet.utilisateurs SET etat_inscription = 'Empêché' WHERE id_utilisateur = 3;

UPDATE projet.interets SET vue = false WHERE utilisateur = 3 AND objet = 3;
*/
-- Requêtes démo 1 client
-- SELECT u.id_utilisateur, u.pseudo, u.est_admin, u.etat_inscription, u.commentaire
-- FROM projet.utilisateurs u
-- ORDER BY u.est_admin, u.etat_inscription;
--
-- SELECT o.id_objet, o.description, t.nom AS "type", o.etat_objet, of.date_offre
-- FROM projet.objets o,
--      projet.types_objets t,
--      projet.offres of
-- WHERE o.type_objet = t.id_type
--   AND of.id_objet = o.id_objet
-- ORDER BY of.date_offre;
--
-- SELECT u.nom, o.description
-- FROM projet.objets o,
--      projet.utilisateurs u
-- WHERE u.id_utilisateur = o.offreur
-- ORDER BY u.nom, o.description;
