
------------------------------------------------------------------------------------------
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO role (id, nom) VALUES
    (1, 'admin'),
    (2, 'user');

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

INSERT INTO utilisateur (role_id, nom, prenom, email, password) VALUES
    (2, 'Brown', 'Bob', 'bob.brown@example.com', 'password'),
    (2, 'Johnson', 'Charlie', 'charlie.johnson@example.com', 'password'),
    (2, 'Williams', 'David', 'david.williams@example.com', 'password'),
    (2, 'Martinez', 'Emma', 'emma.martinez@example.com', 'password'),
    (2, 'Anderson', 'Olivia', 'olivia.anderson@example.com', 'password'),
    (2, 'Thomas', 'Lucas', 'lucas.thomas@example.com', 'password'),
    (2, 'Lee', 'Sophia', 'sophia.lee@example.com', 'password'),
    (2, 'Harris', 'Liam', 'liam.harris@example.com', 'password');

--------------------------------------------------------------------------------------------
CREATE TABLE portefeuille (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

INSERT INTO portefeuille (utilisateur_id)
SELECT id FROM utilisateur ORDER BY id DESC LIMIT 10;

-------------------------------------------------------------------------------------------------

CREATE TABLE type_crypto (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    symbole VARCHAR(10)
);

INSERT INTO type_crypto (nom, description, symbole) VALUES
    ('Bitcoin', 'Premiere cryptomonnaie decentralisee, creee par Satoshi Nakamoto en 2009.', 'BTC'),
    ('Ethereum', 'Blockchain programmable permettant les smart contracts et applications decentralisees.', 'ETH'),
    ('Ripple', 'Cryptomonnaie optimisee pour les paiements internationaux rapides et peu couteux.', 'XRP'),
    ('Litecoin', 'Alternative plus rapide et plus legere a Bitcoin, creee par Charlie Lee.', 'LTC'),
    ('Cardano', 'Blockchain proof-of-stake securisee et evolutive, developpee par des experts academiques.', 'ADA'),
    ('Polkadot', 'Blockchain permettant l interoperabilite entre differentes blockchains.', 'DOT'),
    ('Solana', 'Blockchain ultra-rapide et evolutive, ideale pour les applications decentralisees.', 'SOL'),
    ('Binance Coin', 'Cryptomonnaie native de Binance, utilisee pour reduire les frais de transaction.', 'BNB'),
    ('Dogecoin', 'Cryptomonnaie inspiree d un meme, populaire pour ses transactions rapides et ses frais bas.', 'DOGE'),
    ('Chainlink', 'Reseau d oracles decentralise reliant les smart contracts aux donnees du monde reel.', 'LINK'),
    ('Stellar', 'Plateforme de transferts internationaux.', 'XLM');

---------------------------------------------------------------------------------------------
CREATE TABLE portefeuille_crypto (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type_crypto_id INT NOT NULL,
    nombre_crypto DECIMAL(18,8) NOT NULL CHECK (nombre_crypto >= 0),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

INSERT INTO portefeuille_crypto (utilisateur_id, type_crypto_id, nombre_crypto) VALUES
    (1, 1, 0.5),   -- John Doe possede 0.5 Bitcoin
    (1, 2, 10.0),  -- John Doe possede 10 Ethereum
    (2, 3, 2000),  -- Alice Smith possede 2000 Ripple
    (2, 4, 50),    -- Alice Smith possede 50 Litecoin
    (3, 1, 1.2),   -- Bob Brown possede 1.2 Bitcoin
    (3, 5, 500),   -- Bob Brown possede 500 Cardano
    (4, 6, 100),   -- Charlie Johnson possede 100 Polkadot
    (5, 7, 250),   -- David Williams possede 250 Solana
    (6, 8, 5),     -- Emma Martinez possede 5 Binance Coin
    (7, 9, 10000), -- Olivia Anderson possede 10 000 Dogecoin
    (8, 10, 75),   -- Lucas Thomas possede 75 Chainlink
    (9, 2, 20),    -- Sophia Lee possede 20 Ethereum
    (10, 3, 5000); -- Liam Harris possede 5000 Ripple

---------------------------------------------------------------------------------------------------

CREATE TABLE fond (
    utilisateur_id INT PRIMARY KEY,
    montant_actuel DECIMAL(18,2) NOT NULL CHECK (montant_actuel >= 0),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);
INSERT INTO fond (utilisateur_id, montant_actuel) VALUES
    (1, 1500.75),  -- John Doe a 1500.75 €
    (2, 2500.00),  -- Alice Smith a 2500.00 €
    (3, 780.50),   -- Bob Brown a 780.50 €
    (4, 1200.00),  -- Charlie Johnson a 1200.00 €
    (5, 3500.30),  -- David Williams a 3500.30 €
    (6, 500.00),   -- Emma Martinez a 500.00 €
    (7, 9000.00),  -- Olivia Anderson a 9000.00 €
    (8, 220.10),   -- Lucas Thomas a 220.10 €
    (9, 1345.75),  -- Sophia Lee a 1345.75 €
    (10, 5000.00); -- Liam Harris a 5000.00 €

-----------------------------------------------------------------------------------------------------------

CREATE TABLE operation_fond (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO operation_fond (nom) VALUES
    ('depot'),
    ('retrait');

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

INSERT INTO operation_fait_fond (utilisateur_id, operation_fond_id, montant) VALUES
    -- Utilisateur 1 : Depots - Retraits = 1500.75
    (1, 1, 3000.00),  -- Depot
    (1, 2, 1499.25),  -- Retrait

    -- Utilisateur 2 : Depots - Retraits = 2500.00
    (2, 1, 5000.00),  -- Depot
    (2, 2, 2500.00),  -- Retrait

    -- Utilisateur 3 : Depots - Retraits = 780.50
    (3, 1, 2000.00),  -- Depot
    (3, 2, 1219.50),  -- Retrait

    -- Utilisateur 4 : Depots - Retraits = 1200.00
    (4, 1, 2500.00),  -- Depot
    (4, 2, 1300.00),  -- Retrait

    -- Utilisateur 5 : Depots - Retraits = 3500.30
    (5, 1, 7000.00),  -- Depot
    (5, 2, 3499.70),  -- Retrait

    -- Utilisateur 6 : Depots - Retraits = 500.00
    (6, 1, 1500.00),  -- Depot
    (6, 2, 1000.00),  -- Retrait

    -- Utilisateur 7 : Depots - Retraits = 9000.00
    (7, 1, 18000.00), -- Depot
    (7, 2, 9000.00),  -- Retrait

    -- Utilisateur 8 : Depots - Retraits = 220.10
    (8, 1, 500.00),   -- Depot
    (8, 2, 279.90),   -- Retrait

    -- Utilisateur 9 : Depots - Retraits = 1345.75
    (9, 1, 2000.00),  -- Depot
    (9, 2, 654.25),   -- Retrait

    -- Utilisateur 10 : Depots - Retraits = 5000.00
    (10, 1, 10000.00),-- Depot
    (10, 2, 5000.00); -- Retrait


----------------------------------------------------------------------------------------------------

CREATE TABLE operation_crypto (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL 
);

INSERT INTO operation_crypto (nom) VALUES
    ('achat'),
    ('vente'),
    ('Depot'),
    ('Retrait');

-----------------------------------------------------------------------------------------------------

CREATE TABLE commission (
    id SERIAL PRIMARY KEY,
    pourcentage DECIMAL(5,2) NOT NULL CHECK (pourcentage >= 0 AND pourcentage <= 100),
    operation_crypto_id INT NOT NULL,
    type_crypto_id INT NOT NULL,
    date_heure_commission TIMESTAMP NOT NULL,
    FOREIGN KEY (operation_crypto_id) REFERENCES operation_crypto(id) ON DELETE CASCADE,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

INSERT INTO commission (pourcentage, operation_crypto_id, type_crypto_id, date_heure_commission) VALUES
    -- Achat de cryptos
    (0.5, 1, 1, '2025-02-08 14:30:00'),  -- Achat Bitcoin
    (0.8, 1, 2, '2025-02-08 16:45:00'),  -- Achat Ethereum
    (1.0, 1, 3, '2025-02-08 17:15:00'),  -- Achat Litecoin
    (0.7, 1, 4, '2025-02-08 18:10:00'),  -- Achat Cardano
    (0.6, 1, 5, '2025-02-08 19:30:00'),  -- Achat Solana

    -- Vente de cryptos
    (1.2, 2, 1, '2025-02-08 20:00:00'),  -- Vente Bitcoin
    (1.5, 2, 2, '2025-02-08 21:10:00'),  -- Vente Ethereum
    (0.9, 2, 3, '2025-02-08 22:20:00'),  -- Vente Litecoin
    (1.3, 2, 4, '2025-02-08 23:30:00'),  -- Vente Cardano
    (1.1, 2, 5, '2025-02-09 00:45:00');  -- Vente Solana


-------------------------------------------------------------------------------------------------------------

CREATE TABLE cours_cryptomonnaie (
    id SERIAL PRIMARY KEY,
    type_crypto_id INT NOT NULL,
    prix DECIMAL(18,8) NOT NULL CHECK (prix > 0),
    date_heure TIMESTAMP NOT NULL,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

INSERT INTO cours_cryptomonnaie (type_crypto_id, prix, date_heure) VALUES
    -- Données pour le 2025-02-08
    (1, 43250.75, '2025-02-08 10:00:00'),  -- Bitcoin
    (1, 43500.20, '2025-02-08 12:00:00'),
    (2, 3120.50,  '2025-02-08 10:00:00'),  -- Ethereum
    (2, 3150.90,  '2025-02-08 12:00:00'),
    (3, 152.75,   '2025-02-08 10:00:00'),  -- Litecoin
    (3, 155.20,   '2025-02-08 12:00:00'),
    (4, 0.58,     '2025-02-08 10:00:00'),  -- Cardano
    (4, 0.60,     '2025-02-08 12:00:00'),
    (5, 102.30,   '2025-02-08 10:00:00'),  -- Solana
    (5, 105.10,   '2025-02-08 12:00:00'),

    -- Données pour le 2025-02-07
    (1, 42900.50, '2025-02-07 10:00:00'),  -- Bitcoin
    (1, 43150.80, '2025-02-07 12:00:00'),
    (2, 3090.60,  '2025-02-07 10:00:00'),  -- Ethereum
    (2, 3120.80,  '2025-02-07 12:00:00'),
    (3, 150.00,   '2025-02-07 10:00:00'),  -- Litecoin
    (3, 152.50,   '2025-02-07 12:00:00'),
    (4, 0.56,     '2025-02-07 10:00:00'),  -- Cardano
    (4, 0.58,     '2025-02-07 12:00:00'),
    (5, 100.00,   '2025-02-07 10:00:00'),  -- Solana
    (5, 103.00,   '2025-02-07 12:00:00'),

    -- Données pour le 2025-02-06
    (1, 42650.30, '2025-02-06 10:00:00'),  -- Bitcoin
    (1, 42900.60, '2025-02-06 12:00:00'),
    (2, 3060.40,  '2025-02-06 10:00:00'),  -- Ethereum
    (2, 3090.70,  '2025-02-06 12:00:00'),
    (3, 147.25,   '2025-02-06 10:00:00'),  -- Litecoin
    (3, 150.00,   '2025-02-06 12:00:00'),
    (4, 0.54,     '2025-02-06 10:00:00'),  -- Cardano
    (4, 0.56,     '2025-02-06 12:00:00'),
    (5, 98.00,    '2025-02-06 10:00:00'),  -- Solana
    (5, 101.00,   '2025-02-06 12:00:00'),

    -- Données pour aujourd'hui (2025-02-09)
    (1, 44000.75, '2025-02-09 00:00:00'),  -- Bitcoin
    (1, 44150.20, '2025-02-09 06:00:00'),
    (1, 44300.30, '2025-02-09 12:00:00'),
    (1, 44450.40, '2025-02-09 18:00:00'),
    (2, 3190.50,  '2025-02-09 00:00:00'),  -- Ethereum
    (2, 3210.90,  '2025-02-09 06:00:00'),
    (2, 3230.10,  '2025-02-09 12:00:00'),
    (2, 3250.50,  '2025-02-09 18:00:00'),
    (3, 157.75,   '2025-02-09 00:00:00'),  -- Litecoin
    (3, 159.20,   '2025-02-09 06:00:00'),
    (3, 161.30,   '2025-02-09 12:00:00'),
    (3, 163.40,   '2025-02-09 18:00:00'),
    (4, 0.65,     '2025-02-09 00:00:00'),  -- Cardano
    (4, 0.67,     '2025-02-09 06:00:00'),
    (4, 0.69,     '2025-02-09 12:00:00'),
    (4, 0.71,     '2025-02-09 18:00:00'),
    (5, 108.00,   '2025-02-09 00:00:00'),  -- Solana
    (5, 110.10,   '2025-02-09 06:00:00'),
    (5, 112.20,   '2025-02-09 12:00:00'),
    (5, 114.30,   '2025-02-09 18:00:00');


------------------------------------------------------------------------------------------------------

CREATE TABLE operation_fait_crypto (
    id SERIAL PRIMARY KEY,
    date_heure TIMESTAMP NOT NULL,
    operation_crypto_id INT NOT NULL,
    type_crypto_id INT NOT NULL,
    commission DECIMAL(5, 2) NOT NULL,  -- Commission en pourcentage
    nombre DECIMAL(18, 8) NOT NULL,  -- Nombre de cryptos achetées ou vendues
    prix_final DECIMAL(18, 8) NOT NULL,  -- Prix final payé ou reçu pour chaque crypto
    FOREIGN KEY (operation_crypto_id) REFERENCES operation_crypto(id) ON DELETE CASCADE,
    FOREIGN KEY (type_crypto_id) REFERENCES type_crypto(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    sujet VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    lue BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

UPDATE utilisateur SET 
    crypto_preferee_id = (SELECT id FROM type_crypto WHERE nom = 'Bitcoin'),
    notifications_crypto_actives = TRUE
WHERE id IN (1, 3, 5);

INSERT INTO notifications (utilisateur_id, sujet, message, date_creation, lue) VALUES
(1, 'Opération Bitcoin', 'Achat de 0.5 BTC effectué', NOW(), FALSE),
(3, 'Notification Crypto', 'Variation du prix de Bitcoin', NOW(), FALSE);