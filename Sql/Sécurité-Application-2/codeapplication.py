import oracledb
from PyQt6.QtWidgets import (
    QApplication, QMainWindow, QLabel, QLineEdit, QPushButton, QVBoxLayout,
    QTableWidget, QTableWidgetItem, QTextEdit, QWidget
)
import hashlib

# ============================
# Formulaire de connexion à la base de données
# ============================
class ConnexionForm(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Connexion à la base de données")

        # Widgets pour la connexion
        self.label_user = QLabel("Nom d'utilisateur:")
        self.input_user = QLineEdit()
        self.label_password = QLabel("Mot de passe:")
        self.input_password = QLineEdit()
        self.input_password.setEchoMode(QLineEdit.EchoMode.Password)
        self.connect_button = QPushButton("Se connecter")

        # Layout principal
        layout = QVBoxLayout()
        layout.addWidget(self.label_user)
        layout.addWidget(self.input_user)
        layout.addWidget(self.label_password)
        layout.addWidget(self.input_password)
        layout.addWidget(self.connect_button)

        container = QWidget()
        container.setLayout(layout)
        self.setCentralWidget(container)

        # Connexion des événements
        self.connect_button.clicked.connect(self.connect_to_database)
        self.connection = None

    def connect_to_database(self):
        try:
            # Connexion à la base de données
            self.connection = oracledb.connect(
                user=self.input_user.text(),
                password=self.input_password.text(),
                host='192.168.2.172',
                port=1521,
                service_name='free'
            )
            self.statusBar().showMessage("Connexion réussie!")
            self.show_data_window()
        except oracledb.Error as e:
            self.statusBar().showMessage(f"Erreur: {str(e)}")

    def show_data_window(self):
        if self.connection:
            # Ouvrir les fenêtres après connexion
            self.data_window = AffichageDonnees(self.connection)
            self.data_window.show()
            self.chiffrement_window = Chiffrement()
            self.chiffrement_window.show()
            self.test_injection_window = TestInjection(self.connection)
            self.test_injection_window.show()

# ============================
# Affichage des données
# ============================
class AffichageDonnees(QWidget):
    def __init__(self, connection):
        super().__init__()
        self.connection = connection
        self.setWindowTitle("Affichage des données")

        # Configuration de la table
        self.layout = QVBoxLayout()
        self.table = QTableWidget()
        self.layout.addWidget(self.table)
        self.setLayout(self.layout)

        # Charger les données
        self.load_data()

    def load_data(self):
        query = "SELECT * FROM Employes"  # Exemple, ajustez à votre table
        try:
            cursor = self.connection.cursor()
            cursor.execute(query)
            rows = cursor.fetchall()

            self.table.setRowCount(len(rows))
            self.table.setColumnCount(len(cursor.description))

            for i, row in enumerate(rows):
                for j, value in enumerate(row):
                    self.table.setItem(i, j, QTableWidgetItem(str(value)))

        except oracledb.Error as e:
            print(f"Erreur lors de la récupération des données: {e}")

    def insert_employee(self, id_employe, nom, prenom, id_departement):
        try:
            query = """
            INSERT INTO Employes (ID_EMPLOYE, NOM, PRENOM, ID_DEPARTEMENT)
            VALUES (:id_employe, :nom, :prenom, :id_departement)
            """
            cursor = self.connection.cursor()
            cursor.execute(query, {
                "id_employe": id_employe,
                "nom": nom,
                "prenom": prenom,
                "id_departement": id_departement
            })
            self.connection.commit()
            print("Données insérées avec succès.")
        except oracledb.Error as e:
            print(f"Erreur lors de l'insertion des données: {e}")

# ============================
# Chiffrement des données avec SHA256
# ============================
class Chiffrement(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Chiffrement SHA256")

        # Widgets pour le chiffrement
        self.layout = QVBoxLayout()
        self.input_data = QLineEdit()
        self.input_data.setPlaceholderText("Entrez une donnée à chiffrer")
        self.hash_label = QLabel("Hash:")
        self.button_hash = QPushButton("Chiffrer")

        self.layout.addWidget(self.input_data)
        self.layout.addWidget(self.button_hash)
        self.layout.addWidget(self.hash_label)
        self.setLayout(self.layout)

        # Connexion des événements
        self.button_hash.clicked.connect(self.hash_data)

    def hash_data(self):
        data = self.input_data.text()
        hashed = hashlib.sha256(data.encode()).hexdigest()
        self.hash_label.setText(f"Hash: {hashed}")

# ============================
# Tests d'injection SQL
# ============================
class TestInjection(QWidget):
    def __init__(self, connection):
        super().__init__()
        self.connection = connection
        self.setWindowTitle("Test d'injection SQL")

        # Widgets pour tester les requêtes
        self.layout = QVBoxLayout()
        self.query_input = QTextEdit()
        self.query_input.setPlaceholderText("Entrez une requête SQL à tester")
        self.result_label = QLabel("Résultat:")
        self.button_test = QPushButton("Tester")

        self.layout.addWidget(self.query_input)
        self.layout.addWidget(self.button_test)
        self.layout.addWidget(self.result_label)
        self.setLayout(self.layout)

        # Connexion des événements
        self.button_test.clicked.connect(self.test_query)

    def test_query(self):
        query = self.query_input.toPlainText()
        try:
            cursor = self.connection.cursor()
            cursor.execute(query)
            rows = cursor.fetchall()

            result_text = ""
            for row in rows:
                result_text += " | ".join(str(value) for value in row) + "\n"

            if result_text.strip():
                self.result_label.setText(f"Résultat:\n{result_text}")
            else:
                self.result_label.setText("Résultat sécurisé: Aucun enregistrement trouvé.")
        except oracledb.Error as e:
            self.result_label.setText(f"Erreur: {e}")

# ============================
# Lancement de l'application
# ============================
if __name__ == "__main__":
    app = QApplication([])
    window = ConnexionForm()
    window.show()
    app.exec()
