import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.collections.*;
import javafx.beans.value.*;
import javafx.beans.property.*;
import javafx.util.Callback;

import java.sql.SQLException;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Dillon Fagan on 9/29/16.
 */
class SearchTab extends Tab {

    /** Displays Results of a Database Query. */
    private TableView<Recipe> table = new TableView<>();

    /** Results of a Query; the Contents of the TableView. */
    private ObservableList<Recipe> rowData = FXCollections.observableArrayList();

    /**
     * Default Constructor. Fetches all Recipes from the Database and
     * displays them in a Table.
     * @throws SQLException
     */
    SearchTab() throws SQLException {
        super("Searching...");

        try {
            if (Recipro.connectsToPrivateServer()) {
                rowData = Connect.getAll();
            } else {
                rowData = HerokuConnect.getAll();
            }

            setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches all Recipes that are similar to a given String of Keywords
     * and displays them in a Table.
     * @param s
     * @throws SQLException
     */
    SearchTab(String s) throws SQLException {
        super("Searching...");

        try {
            if (Recipro.connectsToPrivateServer()) {
                rowData = Connect.getByKeyword(s);
            } else {
                rowData = HerokuConnect.getByKeyword(s);
            }

            setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the UI for the Tab.
     * @throws SQLException
     */
    private void setup() throws SQLException {
        TableColumn recipeColumn = new TableColumn("Recipe");
        recipeColumn.setMinWidth(300);
        recipeColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        setContent(table);

        // Fetch the Recipe Object from a selected Row and throw into a RecipeTab.
        table.setRowFactory(t -> {
            TableRow<Recipe> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        Recipe r;
                        if (Recipro.connectsToPrivateServer()) {
                            r = Connect.getRecipe(row.getItem().getIndex());
                        } else {
                            r = HerokuConnect.getRecipe(row.getItem().getIndex());
                        }

                        RecipeTab newRecipeTab = new RecipeTab(r);
                        getTabPane().getTabs().addAll(newRecipeTab);
                        getTabPane().getSelectionModel().select(newRecipeTab);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            return row ;
        });

        // Add the Recipe Column to the Table.
        table.getColumns().addAll(recipeColumn);

        // Add the retrieved Data to the Table.
        table.setItems(rowData);

        // Update the Tab's Title with the Number of Results.
        setText(String.valueOf(table.getItems().size()) + " Recipes Found");
    }
}
