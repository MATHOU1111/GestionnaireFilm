package fr.com.gestionnairefilms;


import javafx.scene.control.TableView;

public class TableViewManager {
    private static TableView<Film> tableView;

    public static TableView<Film> getTableView() {
        if (tableView == null) {
            tableView = new TableView<>();
            // Configurez tableView si nécessaire
        }
        return tableView;
    }

    public static void setTableView(TableView<Film> tableView) {
        TableViewManager.tableView = tableView;
    }
}
