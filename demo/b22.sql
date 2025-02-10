
------------------------------------------------------------------------------------------
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

--------------------------------------------------------------------------------------------
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    role_id INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    photo_profil_path VARCHAR(255),
    crypto_preferee_id INT,
    notifications_crypto_actives BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (crypto_preferee_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

--------------------------------------------------------------------------------------------
CREATE TABLE portefeuille (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-------------------------------------------------------------------------------------------------

CREATE TABLE type_crypto (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    symbole VARCHAR(10)
);

---------------------------------------------------------------------------------------------
CREATE TABLE portefeuille_crypto (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type_crypto_id INT NOT NULL,
    nombre_crypto DECIMAL(18,8) NOT NULL CHECK (nombre_crypto >= 0),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

---------------------------------------------------------------------------------------------------

CREATE TABLE fond (
    utilisateur_id INT PRIMARY KEY,
    montant_actuel DECIMAL(18,2) NOT NULL CHECK (montant_actuel >= 0),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-----------------------------------------------------------------------------------------------------------

CREATE TABLE operation_fond (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

-----------------------------------------------------------------------------------------------------------

CREATE TABLE operation_fait_fond (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    operation_fond_id INT NOT NULL,
    montant DECIMAL(18,2) NOT NULL CHECK (montant > 0),
    date_operation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES fond(utilisateur_id) ON DELETE CASCADE,
    FOREIGN KEY (operation_fond_id) REFERENCES operation_fond(id) ON DELETE CASCADE
);

----------------------------------------------------------------------------------------------------

CREATE TABLE operation_crypto (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL 
);

-----------------------------------------------------------------------------------------------------

CREATE TABLE commission (
    id SERIAL PRIMARY KEY,
    pourcentage DECIMAL(5,2) NOT NULL CHECK (pourcentage >= 0 AND pourcentage <= 100),
    type_operation VARCHAR(50) NOT NULL,
    description TEXT
);

-----------------------------------------------------------------------------------------------------

CREATE TABLE cours_cryptomonnaie (
    id SERIAL PRIMARY KEY,
    type_crypto_id INT NOT NULL,
    prix DECIMAL(18,2) NOT NULL,
    date_heure TIMESTAMP NOT NULL,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

-----------------------------------------------------------------------------------------------------

CREATE TABLE validation_utilisateur (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    statut VARCHAR(50) NOT NULL,
    date_validation TIMESTAMP,
    validateur_id INT,
    commentaire TEXT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (validateur_id) REFERENCES utilisateur(id) ON DELETE SET NULL
);

-----------------------------------------------------------------------------------------------------

CREATE TABLE preference_crypto (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type_crypto_id INT NOT NULL,
    alerte_prix BOOLEAN DEFAULT FALSE,
    seuil_alerte DECIMAL(18,2),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

-----------------------------------------------------------------------------------------------------

CREATE TABLE operation_validation (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type_operation VARCHAR(50) NOT NULL,
    montant DECIMAL(18,2) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    date_operation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    validateur_id INT,
    commentaire TEXT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (validateur_id) REFERENCES utilisateur(id) ON DELETE SET NULL
);
