

-- Creation des utilisateurs SYSDBA
CREATE USER dba_principal IDENTIFIED BY principal;
CREATE USER dba_collegue IDENTIFIED BY collegue;

-- Creation de l'utilisateur gestionnaire principal
CREATE USER gestionnaire_principal IDENTIFIED BY gestionnaire;

-- Creation des utilisateurs du registrariat
CREATE USER registrariat1 IDENTIFIED BY reg1;
CREATE USER registrariat2 IDENTIFIED BY reg2;

-- Creation de l'utilisateur API
CREATE USER utilisateur_api IDENTIFIED BY api;

-- Creation de l'utilisateur enseignant
CREATE USER enseignant IDENTIFIED BY enseignant;

--------------------------------------------------------------------------------

-- Creation du rele role_dba
CREATE ROLE role_dba;

GRANT CREATE SESSION TO role_dba;
GRANT DROP ANY TABLE TO role_dba;
GRANT ALTER ANY TABLE TO role_dba;
GRANT CREATE ANY TABLE TO role_dba;
GRANT DROP ANY VIEW TO role_dba;
GRANT CREATE ANY VIEW TO role_dba;

-- Creation role pour le gestionnaire principal
CREATE ROLE role_gestionnaire;

-- Attribution des privil?ges pour le r?le gestionnaire principal
GRANT CREATE SESSION TO role_gestionnaire;
GRANT SELECT, INSERT, UPDATE, DELETE ON etudiant TO role_gestionnaire;
GRANT SELECT, INSERT, UPDATE, DELETE ON cours TO role_gestionnaire;
GRANT SELECT, INSERT, UPDATE, DELETE ON groupe TO role_gestionnaire;
GRANT SELECT, INSERT, UPDATE, DELETE ON semestre TO role_gestionnaire;
GRANT SELECT, INSERT, UPDATE, DELETE ON evaluation TO role_gestionnaire;

-- Creation role pour les utilisateurs du registrariat
CREATE ROLE role_registrariat;

-- Attribution des privil?ges pour le r?le registrariat
GRANT CREATE SESSION TO role_registrariat;
GRANT SELECT, INSERT, UPDATE ON etudiant TO role_registrariat;
GRANT SELECT, INSERT, UPDATE ON cours TO role_registrariat;

-- Creation role pour les utilisateurs API
CREATE ROLE role_api;

-- Attribution des privileges pour le r?le API
GRANT CREATE SESSION TO role_api;
GRANT SELECT, INSERT, UPDATE ON groupe TO role_api;
GRANT SELECT, INSERT, UPDATE ON semestre TO role_api;

-- Creation role pour l'utilisateur enseignant
CREATE ROLE role_enseignant;

-- Attribution des privil?ges pour le role enseignant
GRANT CREATE SESSION TO role_enseignant;
GRANT SELECT, INSERT, UPDATE, DELETE ON evaluation TO role_enseignant;

--------------------------------------------------------------------------------

-- Attribution du r?le_dba ? Mathis et aux utilisateurs DBA
GRANT role_dba TO dba_principal;
GRANT role_dba TO dba_collegue;
GRANT role_dba TO Mathis;

-- Attribution du r?le gestionnaire au gestionnaire principal
GRANT role_gestionnaire TO gestionnaire_principal;

-- Attribution du r?le registrariat aux utilisateurs du registrariat
GRANT role_registrariat TO registrariat1;
GRANT role_registrariat TO registrariat2;

-- Attribution du r?le API ? l'utilisateur API
GRANT role_api TO utilisateur_api;

-- Attribution du r?le enseignant ? l'utilisateur enseignant
GRANT role_enseignant TO enseignant;

--------------------------------------------------------------------------------

-- Cr?ation d'un profil plus permissif pour le gestionnaire principal
CREATE PROFILE profil_gestionnaire LIMIT
    FAILED_LOGIN_ATTEMPTS 10
    PASSWORD_LIFE_TIME 180
    PASSWORD_REUSE_TIME 365
    PASSWORD_REUSE_MAX UNLIMITED;

-- Cr?ation d'un profil plus restrictif pour les autres utilisateurs
CREATE PROFILE profil_restrictif LIMIT
    FAILED_LOGIN_ATTEMPTS 5
    PASSWORD_LIFE_TIME 90
    PASSWORD_REUSE_TIME 365
    PASSWORD_REUSE_MAX 5;

-- Attribution des profils aux utilisateurs
ALTER USER gestionnaire_principal PROFILE profil_gestionnaire;
ALTER USER registrariat1 PROFILE profil_restrictif;
ALTER USER registrariat2 PROFILE profil_restrictif;
ALTER USER utilisateur_api PROFILE profil_restrictif;
ALTER USER enseignant PROFILE profil_restrictif;
ALTER USER Mathis PROFILE profil_gestionnaire;






