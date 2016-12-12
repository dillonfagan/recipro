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
    private static boolean privateServer = true;


    /**
     * Initializes a new Stage as the main window.
     */
    @Override
    public void start(Stage primaryStage) throws SQLException {
        // Primary layout for the window
        VBox rootLayout = new VBox();

        // Set up the window
        primaryStage.setTitle("Recipro");
        primaryStage.setScene(new Scene(rootLayout, 1024, 768));
        primaryStage.setMaximized(true);

        // Initialize the tab manager and add a HomeTab
        tabs = new TabPane();
        tabs.setPrefSize(3840, 2160);
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

        // File Menu
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

        MenuItem serverConnectionCommand = new MenuItem("Connect to Heroku Server");
        serverConnectionCommand.setOnAction(a -> {
            privateServer = !privateServer;

            // TODO Close all tabs?
            tabs.getTabs().removeAll();

            // Toggle the Label of the serverConnectionCommand
            if (privateServer) {
                serverConnectionCommand.setText("Connect to Heroku");
            } else {
                serverConnectionCommand.setText("Connect to SQL Server");
            }
        });

        fileMenu.getItems().addAll(newRecipeCommand, serverConnectionCommand);

        // Tools Menu
        Menu toolsMenu = new Menu("Tools");

        MenuItem calcCommand = new MenuItem("Conversion Assistant");
        calcCommand.setOnAction(a -> {
    		CalcTab newCalcTab = new CalcTab();
    		tabs.getTabs().add(newCalcTab);
    		tabs.getSelectionModel().select(newCalcTab);
        });

        toolsMenu.getItems().add(calcCommand);

        menuBar.getMenus().addAll(fileMenu, toolsMenu);

        return menuBar;
    }

    public static boolean connectsToPrivateServer() {
        return privateServer;
    }
}
