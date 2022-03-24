DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;

CREATE TABLE projet.adresses
(
    id_adresse  SERIAL PRIMARY KEY,
    rue         VARCHAR(255) NOT NULL,
    numero      INTEGER      NOT NULL,
    boite       VARCHAR(10),
    code_postal INTEGER      NOT NULL,
    commune     VARCHAR(30)
);

CREATE TABLE projet.types_objets
(
    id_type SERIAL PRIMARY KEY,
    nom     VARCHAR(50) NOT NULL
);

CREATE TABLE projet.utilisateurs
(
    id_utilisateur   SERIAL PRIMARY KEY,
    pseudo           VARCHAR(25)                                     NOT NULL,
    nom              VARCHAR(50)                                     NOT NULL,
    prenom           VARCHAR(50)                                     NOT NULL,
    mdp              VARCHAR(255)                                    NOT NULL,
    gsm              VARCHAR(15),
    est_admin        BOOLEAN                                         NOT NULL,
    adresse          INTEGER REFERENCES projet.adresses (id_adresse) NOT NULL,
    etat_inscription VARCHAR(10)                                     NOT NULL,
    commentaire      VARCHAR(255) NULL
);

CREATE TABLE projet.objets
(
    id_objet    SERIAL PRIMARY KEY,
    etat_objet  VARCHAR(9)                                              NOT NULL,
    description VARCHAR(255)                                            NOT NULL,
    photo       VARCHAR(255),
    type_objet  INTEGER REFERENCES projet.types_objets (id_type)        NOT NULL,
    offreur     INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
    receveur    INTEGER REFERENCES projet.utilisateurs (id_utilisateur)
);

CREATE TABLE projet.interets
(
    utilisateur INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
    objet       INTEGER REFERENCES projet.objets (id_objet)             NOT NULL,
    PRIMARY KEY (utilisateur, objet)
);

CREATE TABLE projet.evaluations
(
    id_evaluation SERIAL PRIMARY KEY,
    objet         INTEGER REFERENCES projet.objets (id_objet) NOT NULL,
    commentaire   VARCHAR(255)                                NOT NULL
);

CREATE TABLE projet.offres
(
    id_offre      SERIAL PRIMARY KEY,
    date_offre    TIMESTAMP                                   NOT NULL,
    plage_horaire VARCHAR(255)                                NOT NULL,
    id_objet      INTEGER REFERENCES projet.objets (id_objet) NOT NULL
);

CREATE TABLE projet.offres_precedentes
(
    id_offre_precedente SERIAL PRIMARY KEY,
    date_precedente     DATE                                        NOT NULL,
    offre               INTEGER REFERENCES projet.offres (id_offre) NOT NULL
);

INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue de l’Eglise', 11, 'B1', 4987, 'Stoumont');
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue de Renkin', 7, NULL, 4800, 'Verviers');
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue Haute Folie', 6, 'A103', 4800, 'Verviers');
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Haut-Vinâve', 13, NULL, 4845, 'Jalhay');

INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'caro', 'Line', 'Caroline',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 1, 'Refusé',
        'Il faudra patienter un jour ou deux.');
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'achil', 'Ile', 'Achille',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 2,
        'En attente', NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bazz', 'Ile', 'Basile',
        '$2a$10$fzEFB4Vk.hEEPRvpbm.27OkxekRLuhsj1W2d0gSR.ryW7hmINPVkS', NULL, false, 3, 'Confirmé',
        NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'bri', 'Lehmann', 'Brigitte',
        '$2a$10$W0IiogOO7ef5/Kw.GdmEkO46mtg6VSeDsV5SYc4Dzmp4XnnOBUAkC', NULL, true, 4, 'Confirmé',
        NULL);

INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo1', 'nom1', 'prenom1',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, false, 1, 'Confirmé',
        NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo2', 'nom2', 'prenom2',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, false, 2,
        'En attente', NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo3', 'nom3', 'prenom3',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', '0475858535', false, 3,
        'Refusé', 'Seuls les amis proches ont accès au site.');
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo4', 'nom4', 'prenom4',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, true, 1, 'Confirmé',
        NULL);

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
VALUES (DEFAULT, 'Offert', 'Décorations de Noël de couleur rouge.', 'test', 3, 3, NULL);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 'Cadre représentant un chien noir sur un fond noir.', 'test', 3, 3,
        NULL);
INSERT INTO projet.objets
VALUES (DEFAULT, 'Offert', 'Ancien bureau d’écolier.', 'test', 8, 4, NULL);

INSERT INTO projet.offres
VALUES (DEFAULT, '21-03-22', 'Mardi de 17h à 22h', 1);
INSERT INTO projet.offres
VALUES (DEFAULT, '25-03-22', 'Lundi de 18h à 22h', 2);
INSERT INTO projet.offres
VALUES (DEFAULT, '25-03-22', 'Tous les jours de 15h à 18h', 3);