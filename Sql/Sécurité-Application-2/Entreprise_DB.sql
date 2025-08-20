--Question 3
CREATE TABLE Employes (
    ID_Employe NUMBER PRIMARY KEY,
    Nom VARCHAR2(50),
    Prenom VARCHAR2(50),
    ID_Departement NUMBER
);

CREATE TABLE Departements (
    ID_Departement NUMBER PRIMARY KEY,
    Nom_Departement VARCHAR2(100)
);

CREATE TABLE Projets (
    ID_Projet NUMBER PRIMARY KEY,
    Nom_Projet VARCHAR2(100),
    ID_Departement NUMBER
);

--Question 4
INSERT INTO Employes (ID_Employe, Nom, Prenom, ID_Departement)
VALUES (1, 'Dupont', 'Jean', 101);

INSERT INTO Employes (ID_Employe, Nom, Prenom, ID_Departement)
VALUES (2, 'Durand', 'Marie', 102);

INSERT INTO Employes (ID_Employe, Nom, Prenom, ID_Departement)
VALUES (3, 'Morel', 'Paul', 101);

INSERT INTO Employes (ID_Employe, Nom, Prenom, ID_Departement)
VALUES (4, 'Martin', 'Claire', 103);

--
INSERT INTO Departements (ID_Departement, Nom_Departement)
VALUES (101, 'Ressources Humaines');

INSERT INTO Departements (ID_Departement, Nom_Departement)
VALUES (102, 'IT');

INSERT INTO Departements (ID_Departement, Nom_Departement)
VALUES (103, 'Marketing');

INSERT INTO Departements (ID_Departement, Nom_Departement)
VALUES (104, 'Finance');

--
INSERT INTO Projets (ID_Projet, Nom_Projet, ID_Departement)
VALUES (201, 'Migration ERP', 102);

INSERT INTO Projets (ID_Projet, Nom_Projet, ID_Departement)
VALUES (202, 'Recrutement 2024', 101);

INSERT INTO Projets (ID_Projet, Nom_Projet, ID_Departement)
VALUES (203, 'Campagne Marketing', 103);

INSERT INTO Projets (ID_Projet, Nom_Projet, ID_Departement)
VALUES (204, 'Analyse Budg?taire', 104);

--Question 5
SELECT * FROM Employes;
SELECT * FROM Departements;
SELECT * FROM Projets;

--PARTIE 2

--Question 1
CREATE AUDIT POLICY audit_conn_policy
ACTIONS LOGON;

AUDIT POLICY audit_conn_policy;

--Question 2
CREATE AUDIT POLICY audit_employes_dml
ACTIONS INSERT ON Employes,
        UPDATE ON Employes,
        DELETE ON Employes;

CREATE AUDIT POLICY audit_projets_dml
ACTIONS INSERT ON Projets,
        UPDATE ON Projets,
        DELETE ON Projets;

AUDIT POLICY audit_employes_dml;
AUDIT POLICY audit_projets_dml;

--Question 3
SELECT EVENT_TIMESTAMP, DBUSERNAME, ACTION_NAME, OBJECT_NAME, SQL_TEXT, RETURN_CODE
FROM UNIFIED_AUDIT_TRAIL
ORDER BY EVENT_TIMESTAMP DESC;

--PARTIE 3

--Question 1
DECLARE
    encryption_key RAW(32) := UTL_RAW.CAST_TO_RAW('Clechiffrement123!');
BEGIN
    DBMS_OUTPUT.PUT_LINE('Cl? cr??e : ' || encryption_key);
END;
/

ALTER TABLE Projets ADD description_chiffree RAW(2000);
ALTER TABLE Projets ADD description VARCHAR2(2000);


DECLARE
    encryption_key RAW(32) := UTL_RAW.CAST_TO_RAW('Clechiffrement123!');
BEGIN
    UPDATE Projets
    SET description_chiffree = DBMS_CRYPTO.ENCRYPT(
        src => UTL_RAW.CAST_TO_RAW(description),
        typ => DBMS_CRYPTO.ENCRYPT_AES256 + DBMS_CRYPTO.CHAIN_CBC + DBMS_CRYPTO.PAD_PKCS5,
        key => encryption_key
    );
END;
/







    













