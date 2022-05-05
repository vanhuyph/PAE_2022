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
    receveur_choisi BOOLEAN                                                 NULL,
    venu_chercher   BOOLEAN                                                 NULL,
    vue_empecher    BOOLEAN                                                 NOT NULL,
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
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue de Verviers', 47, NULL, 4800, 'Liège', 0);
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue du salpêtré', 789, 'Bis', 1040, 'Bruxelles', 0);
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue des Minières', 45, 'Ter', 4800, 'Verviers', 0);

INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'caro', 'Line', 'Caroline',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, true, 1, 'Confirmé',
        NULL, 0, 3, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'achil', 'Ile', 'Achille',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 2,
        'Refusé', 'L''application n''est pas encore ouverte à tous.', 0, 0, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bazz', 'Ile', 'Basile',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 3, 'Confirmé',
        NULL, 0, 2, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bri', 'Lehmann', 'Brigitte',
        '$2a$10$W0IiogOO7ef5/Kw.GdmEkO46mtg6VSeDsV5SYc4Dzmp4XnnOBUAkC', NULL, true, 4, 'Confirmé',
        NULL, 0, 1, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'theo', 'Ile', 'Théophile',
        '$2a$10$avAv2TwCATrvOIQfxTf25uuL8NUKpoG6g7gT9Z4tZi9.vzbdBjsqq', NULL, false, 2, 'Confirmé',
        NULL, 0, 7, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'emi', 'Ile', 'Emile',
        '$2a$10$ZPfoCSBFystdHe9OTinWEe8Aq0ElN7.A3VhoXlEA3KwF0fqWseuDq', NULL, false, 5, 'Refusé',
        'L''application n''est pas encore ouverte à tous.', 0, 0, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'cora', 'Line', 'Coralie',
        '$2a$10$RgnvUGTI2p89Prea3I8X6OEQDKPcHtv3ntCSeZa206cN66MaPlXKW', NULL, false, 6, 'Refusé',
        'Vous devez encore attendre quelques jours.', 0, 0, 0, 0, 0);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'charline', 'Line', 'Charles',
        '$2a$10$91M2cZ0qMr9fPNcGFBOHfOBloy0iKiX1LxPmBdWvfHtFI6XsFPomG', NULL, false, 7,
        'En attente',
        NULL, 0, 0, 0, 0, 0);

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
VALUES (DEFAULT, 'Annulé', 3, 'Décorations de Noël de couleur rouge', 3, NULL,
        'christmas-1869533_640.png', 1, false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 3, 'Cadre représentant un chien noir sur un fond noir', 3, NULL,
        'dog-4118585_640.jpg', 1, false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Intéressé', 8, 'Ancien bureau d’écolier', 4, NULL, 'BureauEcolier-7.JPG', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Intéressé', 7,
        'Brouette à deux roues à l''avant. Améliore la stabilité et ne fatigue pas le dos', 5, NULL,
        'wheelbarrows-4566619_640.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 7,
        'Scie sur perche Gardena', 5, NULL,
        'donnamis.png', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 8,
        'Table jardin et deux chaises en bois', 5, NULL,
        'Table-jardin.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 8,
        'Table bistro', 5, NULL,
        'table-bistro.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Intéressé', 8,
        'Table bistro ancienne de couleur bleue', 1, NULL,
        'table-bistro-carree-bleue.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Confirmé', 4,
        'Tableau noir pour enfant', 5, NULL,
        'Tableau.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Intéressé', 3,
        'Cadre cottage naïf', 5, NULL,
        'cadre-cottage-1178704_640.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Intéressé', 6,
        'Tasse de couleur claire rose & mauve', 5, NULL,
        'tasse-garden-5037113_640.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Confirmé', 9,
        'Pâquerettes dans pots rustiques', 1, NULL,
        'pots-daisy-181905_640.jpg', 1,
        false);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 9,
        'Pots en grès pour petites plantes', 1, NULL,
        'pots-plants-6520443_640.jpg', 1,
        false);

INSERT INTO projet.offres
VALUES (DEFAULT, 1, '21-03-22', 'Mardi de 17h à 22h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 2, '25-03-22', 'Lundi de 18h à 22h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 3, '25-03-22', 'Tous les jours de 15h à 18h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 4, '28-03-22', 'Tous les matins avant 11h30', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 5, '28-03-22', 'Tous les matins avant 11h30', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 6, '29-03-22', 'En semaine, de 20h à 21h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 7, '30-03-22', 'Lundi de 18h à 20h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 8, '14-04-22', 'Samedi en journée', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 9, '14-04-22', 'Lundi de 18h à 20h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 10, '21-04-22', 'Lundi de 18h30 à 20h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 11, '21-04-22', 'Lundi de 18h30 à 20h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 12, '21-04-22', 'Lundi de 16h à 17h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, 13, '21-04-22', 'Lundi de 16h à 17h', 1);

INSERT INTO projet.interets
VALUES (5, 3, '16-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (3, 3, '17-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (4, 4, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (3, 4, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (1, 4, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (4, 8, '14-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (5, 8, '14-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (1, 9, '16-05-22', 0, false, true, NULL, false);
INSERT INTO projet.interets
VALUES (4, 10, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (3, 10, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (1, 10, '09-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (3, 11, '16-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (1, 11, '16-05-22', 0, false, false, NULL, false);
INSERT INTO projet.interets
VALUES (3, 12, '16-05-22', 0, false, true, NULL, false);
