import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * Created by Dillon Fagan on 9/13/16.
 */
public class Recipro extends Application {

    /**
     * The TabPane stores and manages all open tabs.
     */
    private TabPane tabs;

    /**
     * Determines whether to connect to the Heroku PostgreSQL DB or
     * a private Database.
     */
    private static boolean privateServer = false;

    /**
     * Initializes a new Stage as the main window.
     */
    @Override
    public void start(Stage primaryStage) throws SQLException {
        // Primary layout for the window
        VBox rootLayout = new VBox();

        // Set up the window
        primaryStage.setTitle("Recipro");
        primaryStage.setScene(new Scene(rootLayout, 860, 660));

        // Initialize the tab manager and add a HomeTab
        tabs = new TabPane();
        tabs.setPrefSize(860, 660);
        tabs.getTabs().add(new HomeTab());

        try {
            // Add all items to the root layout
            rootLayout.getChildren().addAll(createMenuBar(), tabs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Show the window
        primaryStage.show();
    }

    /**
     * Returns a completed MenuBar for the window.
     */
    private MenuBar createMenuBar() throws SQLException {
        MenuBar menuBar  = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem newRecipeCommand = new MenuItem("New Recipe...");
        newRecipeCommand.setOnAction(a -> {
            try {
                RecipeTab newRecipeTab = new RecipeTab();
                tabs.getTabs().add(newRecipeTab);
                tabs.getSelectionModel().select(newRecipeTab);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        MenuItem serverConnectionCommand = new MenuItem("Connect to SQL Server");
        serverConnectionCommand.setOnAction(a -> {
            privateServer = !privateServer;

            // Toggle the Label of the serverConnectionCommand
            if (privateServer) {
                serverConnectionCommand.setText("Connect to Heroku");
            } else {
                serverConnectionCommand.setText("Connect to SQL Server");
            }
        });

        fileMenu.getItems().addAll(newRecipeCommand, serverConnectionCommand);

        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    public static boolean connectsToPrivateServer() {
        return privateServer;
    }
}
