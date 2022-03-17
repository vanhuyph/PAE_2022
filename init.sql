DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;

CREATE TABLE projet.adresses(
                                id_adresse SERIAL PRIMARY KEY,
                                rue VARCHAR(255) NOT NULL ,
                                numero INTEGER NOT NULL,
                                boite INTEGER,
                                code_postal INTEGER NOT NULL,
                                commune VARCHAR(30)
);


CREATE TABLE projet.types_objets(
                                    id_type SERIAL PRIMARY KEY,
                                    nom VARCHAR(50) NOT NULL
);


CREATE TABLE projet.utilisateurs(
                                    id_utilisateur SERIAL PRIMARY KEY,
                                    pseudo VARCHAR(25) NOT NULL ,
                                    nom VARCHAR(50) NOT NULL,
                                    prenom VARCHAR(50) NOT NULL,
                                    mdp VARCHAR(255) NOT NULL,
                                    gsm VARCHAR(15),
                                    est_admin BOOLEAN NOT NULL,
                                    adresse INTEGER REFERENCES projet.adresses (id_adresse) NOT NULL,
                                    etat_inscription VARCHAR(10) NOT NULL,
                                    commentaire VARCHAR(255) NULL
);


CREATE TABLE projet.objets(
                              id_objet SERIAL PRIMARY KEY,
                              etat_objet VARCHAR(9) NOT NULL,
                              type_objet INTEGER REFERENCES projet.types_objets (id_type) NOT NULL,
                              description VARCHAR(255) NOT NULL,
                              offreur INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
                              receveur INTEGER REFERENCES projet.utilisateurs (id_utilisateur),
                              photo VARCHAR(255)
);

-- taille photo?

CREATE TABLE projet.interets(
                                utilisateur INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
                                objet INTEGER REFERENCES projet.objets(id_objet) NOT NULL,
                                date_rdv TIMESTAMP NOT NULL,
                                PRIMARY KEY (utilisateur, objet)
);

CREATE TABLE projet.evaluations(
                                   id_evaluation SERIAL PRIMARY KEY,
                                   objet INTEGER REFERENCES projet.objets(id_objet) NOT NULL,
                                   commentaire VARCHAR(255) NOT NULL
);

CREATE TABLE projet.offres
(
    id_offre      SERIAL PRIMARY KEY,
    id_objet      INTEGER REFERENCES projet.objets (id_objet) NOT NULL,
    date_offre    DATE                                        NOT NULL,
    plage_horaire VARCHAR(500)                                NOT NULL
);


INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue1', 21, NULL, 1420, 'Ophain');
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue2', 15, NULL, 1500, 'Hal');
INSERT INTO projet.adresses
VALUES (DEFAULT, 'Rue3', 7, 23, 1700, 'Dilbeek');

INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo1', 'nom1', 'prenom1',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, false, 1, 'confirmé',
        NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo2', 'nom2', 'prenom2',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, false, 2,
        'en attente', NULL);
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo3', 'nom3', 'prenom3',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', '0475858535', false, 3,
        'refusé', 'Seuls les amis proches ont accès au site.');
INSERT INTO projet.utilisateurs
VALUES (DEFAULT, 'pseudo4', 'nom4', 'prenom4',
        '$2a$10$0t0a./eaznbH5YnfPlgbA.8beRBzA6szoyafFijA3PNgFDnSdUKl2', NULL, true, 1, 'confirmé',
        NULL);

INSERT INTO projet.types_objets
VALUES (DEFAULT, 'machine');

INSERT INTO projet.objets
VALUES (DEFAULT, 'offert', 1, 'machine à laver', 1, NULL, 'photo machine à laver');
INSERT INTO projet.offres
VALUES (DEFAULT, 1, '2016-02-05', ' ');

INSERT INTO projet.objets
VALUES (DEFAULT, 'interrese', 1, 'machine à cuisiner', 2, NULL, 'photo machine à cuisiner');
INSERT INTO projet.offres
VALUES (DEFAULT, 2, now(), ' ');

INSERT INTO projet.objets
VALUES (DEFAULT, 'interrese', 1, 'machine à nettoyer', 1, 2, 'photo machine');
INSERT INTO projet.offres
VALUES (DEFAULT, 3, '2017-02-05', ' ');


INSERT INTO projet.types_objets VALUES (DEFAULT,'Décoration');
INSERT INTO projet.types_objets VALUES (DEFAULT,'Meuble');
INSERT INTO projet.types_objets VALUES (DEFAULT,'Plante');
INSERT INTO projet.types_objets VALUES (DEFAULT,'Jouet');
INSERT INTO projet.types_objets VALUES (DEFAULT,'Vêtement');


