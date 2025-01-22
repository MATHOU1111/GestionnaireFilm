package fr.com.gestionnairefilms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AreYouSureController {

    @FXML
    private Label confirmationMessage;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private boolean confirmed = false;

    // Méthode pour définir un message personnalisé de confirmation.
    public void setConfirmationMessage(String message) {
        if (confirmationMessage != null) {
            confirmationMessage.setText(message);
        }
    }

    // Retourne si l'utilisateur a confirmé ou non.
    public boolean isConfirmed() {
        return confirmed;
    }

    // Action lors du clic sur "Confirmer".
    @FXML
    private void onConfirmAction() {
        confirmed = true; // L'utilisateur a confirmé
        closeModal();
    }

     // Action lors du clic sur "Annuler".
    @FXML
    private void onCancelAction() {
        confirmed = false; // L'utilisateur a annulé
        closeModal();
    }

    // Fermer la modale.
    private void closeModal() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
