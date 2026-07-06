Drop DATABASE if EXISTS TechLife;
Create DATABASE if not EXISTS TechLife;
Use TechLife;

Create Table AnagraficaUtente(
ID int not NULL auto_Increment,
Nome varchar(50) not NULL,
Cognome varchar(50) not NULL,
Codice_Fiscale varchar(50) not NULL,
Luogo_di_Nascita varchar(50) not NULL,
Data_di_Nascita DATE not NULL,
Comune_Residenza varchar(50) not NULL,
Indirizzo_Residenza varchar(50) not NULL,
CAP_Residenza varchar(5) not NULL,
Comune_Domicilio varchar(50),
Indirizzo_Domicilio varchar(50),
CAP_Domicilio varchar(5),
Email varchar(50) not NULL,
Password varchar(50) not NULL,
PRIMARY KEY(ID)
);

Create table AnagraficaPIVA(
ID int not NULL auto_Increment,
NomeAzienda varchar(50) not NULL,
Partita_IVA varchar(50) not NULL,
Indirizzo_Legale varchar(50) not Null,
CAP_Legale varchar(5) not Null,
Comune_Legale varchar(50) not Null,
PEC varchar(50) not NULL,
Email varchar(50) not NULL,
Password varchar(500) not NULL,
PRIMARY KEY(ID)
);

Create table Amministratori(
    ID int not NULL auto_Increment,
    Email varchar(50) not NULL,
    Password varchar(500) not NULL,
    PRIMARY KEY(ID)
);

Create table Spedizione_Utente(
ID int not NULL auto_increment,
ID_Utente int not NULL,
Email_Utente varchar(50),
Indirizzo_Spedizione varchar(50),
CAP_Spedizione varchar(5),
Comune_Spedizione varchar(50),
Provincia_Spedizione varchar(2),
Note varchar(500),

PRIMARY KEY(ID),
FOREIGN KEY (ID_Utente) REFERENCES AnagraficaUtente(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

Create table Spedizione_Azienda(
ID int not NULL auto_increment,
ID_Azienda int not NULL,
Email_Azienda varchar(50),
Pec_Azienda varchar(50),
Indirizzo_Spedizione varchar(50),
CAP_Spedizione varchar(5),
Comune_Spedizione varchar(50),
Provincia_Spedizione varchar(2),
Note varchar(500),

PRIMARY KEY(ID),
FOREIGN KEY (ID_Azienda) REFERENCES AnagraficaPIVA(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

Create table Prodotto(
ID int not Null auto_increment,
Nome varchar(50) not NULL,
Categoria varchar(50) not NULL,
Prezzo decimal(8,2) not Null,
Foto varchar(100) not NULL,
Descrizione varchar(10000) not NULL,
PRIMARY KEY(ID)
);

CREATE TABLE Ordine (
ID int NOT NULL AUTO_INCREMENT,
ID_Cliente int NOT NULL,
Tipo_Cliente varchar(10) NOT NULL,
Data_Ordine TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
Totale_Ordine decimal(10,2) NOT NULL,
PRIMARY KEY (ID)
);

CREATE TABLE Carrello_Salvato (
    ID_Utente INT NOT NULL,
    ID_Prodotto INT NOT NULL,
    Quantita INT NOT NULL,
    PRIMARY KEY (ID_Utente, ID_Prodotto),
    FOREIGN KEY (ID_Prodotto) REFERENCES Prodotto(ID) ON DELETE CASCADE
);

Create table DettaglioOrdine(
ID int not Null auto_increment,
ID_Ordine int not Null,
ID_Prodotto int not Null,
Quantità int not Null,
Prezzo decimal(8,2) not Null,
PRIMARY KEY(ID),
FOREIGN KEY (ID_Ordine) REFERENCES Ordine(ID),
FOREIGN KEY (ID_Prodotto) REFERENCES Prodotto(ID)
);

INSERT INTO Amministratori (Email, Password) VALUES 
('admin@admin.it', 'prMYfOn007DCYfHouiK0L3FeK9qEoE6cRZb5zXD+HBc=');

INSERT INTO AnagraficaUtente (Nome, Cognome, Codice_Fiscale, Luogo_di_Nascita, Data_di_Nascita, Comune_Residenza, Indirizzo_Residenza, CAP_Residenza, 
	Comune_Domicilio, Indirizzo_Domicilio, CAP_Domicilio, Email, Password) VALUES 
('Luca', 'Bianhi', 'BNCLCU85M10F205H', 'Milano', '1985-05-10', 'Milano', 'Corso Buenos Aires 42', '20124', 'Fisciano', 'Via Giovanni Paolo II', '84084', 'luca.bianchi@email.com', 'prMYfOn007DCYfHouiK0L3FeK9qEoE6cRZb5zXD+HBc='),
('Giovanna', 'Verdi', 'VRDGNN90R41L219Z', 'Torino', '1990-10-22', 'Torino', 'Via Roma 100', '10121', 'Torino', 'Via Roma 100', '10121', 'giovanna.verdi@email.com', 'RTstxmLkKFzMj2lW87awU30kEnqFXUS0a+FH2dDus0o='),
('Mario', 'Rossi', 'RSSMRA80A01H501U', 'Roma', '1980-01-01', 'Roma', 'Via Roma 1', '00100','Roma', 'Via Roma 1', '00100', 'test@privato.it', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8='),
('Simone', 'Terralavoro', 'dsnsvt98r13g039m', 'battipaglia', '2005-07-05', 'battipaglia', 'madonna', '84015', 'battipaglia', 'madonna', '84015', 'd@d.it', '1uyh8GlJyZHHhDw/KYelpGlDOOVILGm59y2Pqpnqh2I=');


INSERT INTO AnagraficaPIVA (NomeAzienda, Partita_IVA, Indirizzo_Legale, CAP_Legale, Comune_Legale, PEC, Email, Password) VALUES 
('FitLife S.r.l.', '01234560123', 'Via dello Sport 8', '84084', 'Fisciano', 'fitlifesrl@legalmail.it', 'info@fitlife.it', 'EPiaglQ6PIvwP3/AeacUzbWdpMKfsUMldezbLY/Qruo='),
('Centro Medico San Raffaele SpA', '09876543210', 'Via dei Cedri 45', '20132', 'Milano', 'sanraffaelespa@pec.it', 'acquisti@sanraffaele.it', 'wzcxIPSLBT091WU+G+inbqNmmxgDb6+9aIu/oNDEDzg=');

INSERT INTO Prodotto(Nome, Categoria, Prezzo, Foto, Descrizione) VALUES
("Philips HeartStart FRx", "DAE", "199.99","img/prodotti/DAE_Philips.jpg","Philips HeartStart FRx è un defibrillatore semiautomatico portatile completo di batteria, 
coppia elettrodi smart e borsa morbida. Estremamente robusto, semplice da usare guida l’utente attraverso il processo di rianimazione di una vittima colpita da arresto cardiaco improvviso."),
("LifePack 35", "Monitor", "1999.99", "img/prodotti/LifePack35.jpg", "LIFEPAK 35 è un monitor/defibrillatore clinicamente avanzato con strumenti e tecnologie brevettati e basati 
su una piattaforma intuitiva e moderna3 per offrire ai pazienti cure avanzate.
Si tratta di un dispositivo pronto per il futuro, progettato per promuovere un'assistenza cardiaca affidabile1 e raggiungere l'eccellenza clinica nell’ambiente sanitario moderno di oggi."),
("Aspiratore SuperVega", "Aspiratore", "299.99", "img/prodotti/Aspiratore.jpg", "L'aspiratore a batteria Supervega 118 è un dispositivo medico portatile progettato per l'aspirazione di fluidi 
nasali, orali e tracheali in adulti e bambini. Questo apparecchio è ideale per l'uso in ambulanze grazie alla sua versatilità e alla possibilità di funzionare in diverse modalità."),
("Spencer Kompakt 170", "Ventilatore Polmonare", "849.99", "img/prodotti/VentilatorePolmonare.jpg", "Respiratore Kompak con valigetta: la valigetta contiene il respiratore 170 NTX e la 
bombola di ossigeno con regolatore della pressione, pronta all’uso. Respiratori polmonari elettronici per la ventilazione non invasiva di adulti e pazienti pediatrici.");