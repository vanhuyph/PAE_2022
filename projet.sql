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
adresse INTEGER REFERENCES projet.adresses (id_adresse) NOT NULL
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

CREATE TABLE projet.inscriptions(
id_inscription SERIAL PRIMARY KEY,
utilisateur INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
etat_inscription VARCHAR(10) NOT NULL
);

CREATE TABLE projet.interets(
utilisateur INTEGER REFERENCES projet.utilisateurs (id_utilisateur) NOT NULL,
objet INTEGER REFERENCES projet.objets(id_objet) NOT NULL,
PRIMARY KEY (utilisateur, objet)
);

CREATE TABLE projet.evaluations(
id_evaluation SERIAL PRIMARY KEY,
objet INTEGER REFERENCES projet.objets(id_objet) NOT NULL,
commentaire VARCHAR(255) NOT NULL
);



CREATE TABLE projet.offres(
id_offre SERIAL PRIMARY KEY,
date_offre DATE NOT NULL,
plage_horaire VARCHAR(500) NOT NULL
);

CREATE TABLE projet.offres_precedentes(
id_offre_precedente SERIAL PRIMARY KEY,
date_precedente DATE NOT NULL,
offre INTEGER REFERENCES projet.offres(id_offre) NOT NULL
);

-- type date_offre, date pour l'instant, à voir si il faut plus de précision ou un timezone, etc

-- INSERT INTO :

INSERT INTO projet.adresses VALUES (DEFAULT, 'Rue1', 21, NULL, 1420, 'Ophain');
INSERT INTO projet.adresses VALUES (DEFAULT, 'Rue2', 15, NULL, 1500, 'Hal');
INSERT INTO projet.adresses VALUES (DEFAULT, 'Rue3', 7, 23, 1700, 'Dilbeek');

INSERT INTO projet.utilisateurs VALUES (DEFAULT, 'pseudo1', 'nom1', 'prenom1', 'mdp1', NULL, true, 1);
INSERT INTO projet.utilisateurs VALUES (DEFAULT, 'pseudo2', 'nom2', 'prenom2', 'mdp2' , NULL, true, 2);
INSERT INTO projet.utilisateurs VALUES (DEFAULT, 'pseudo3', 'nom3', 'prenom3', 'mdp2', '0475858535', false, 3);


-- changer le mdp en crypté