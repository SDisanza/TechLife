Drop DATABASE if EXISTS TechLife;
Create DATABASE if not EXISTS TechLife;
Use TechLife;

Create Table AnagraficaUtente(
`ID` int not NULL auto_Increment,
`Nome` varchar(50) not NULL,
`Cognome` varchar(50) not NULL,
`Codice_Fiscale` varchar(50) not NULL,
`Luogo_di_Nascita` varchar(50) not NULL,
`Data_di_Nascita` DATE not NULL,
`Comune_Residenza` varchar(50) not NULL,
`Indirizzo_Residenza` varchar(50) not NULL,
`CAP_Residenza` varchar(5) not NULL,
`Comune_Domicilio` varchar(50),
`Indirizzo_Domicilio` varchar(50),
`CAP_Domicilio` varchar(5),
`Email` varchar(50) not NULL,
`Password` varchar(50) not NULL,
PRIMARY KEY(`ID`)
);

Create table AnagraficaPIVA(
`ID` int not NULL auto_Increment,
`NomeAzienda` varchar(50) not NULL,
`Partita_IVA` varchar(50) not NULL,
`Indirizzo_Legale` varchar(50) not Null,
`CAP_Legale` varchar(50) not Null,
`Comune_Legale` varchar(50) not Null,
`PEC` varchar(50) not NULL,
`Email` varchar(50) not NULL,
`Password` varchar(50) not NULL,
PRIMARY KEY(`ID`)
);

Create table Prodotto(
`ID` int not Null auto_increment,
`Nome` varchar(50) not NULL,
`Prezzo` decimal(8,2) not Null,
`Foto` varchar(100) not NULL,
`Descrizione` varchar(100) not NULL,
PRIMARY KEY(`ID`)
);

Create table Ordine(
`ID` int not Null auto_increment,
`ID_Cliente` int not Null,
`Data_Ordine` datetime not Null,
`Totale_Ordine` decimal(10,2) not Null,
PRIMARY KEY(`ID`),
FOREIGN KEY (`ID_Cliente`) REFERENCES AnagraficaUtente(`ID`),
FOREIGN KEY (`ID_Cliente`) REFERENCES AnagraficaPIVA(`ID`)
);

Create table DettaglioOrdine(
`ID` int not Null auto_increment,
`ID_Ordine` int not Null,
`ID_Prodotto` int not Null,
`Quantità` int not Null,
`Prezzo` decimal(8,2) not Null,
PRIMARY KEY(`ID`),
FOREIGN KEY (`ID_Ordine`) REFERENCES Ordine(`ID`),
FOREIGN KEY (`ID_Prodotto`) REFERENCES Prodotto(`ID`)
);


