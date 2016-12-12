import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;

import java.util.ArrayList;

/**
 * Created by Dillon Fagan on 9/29/16.
 */
class HomeTab extends Tab {

    /**
     * Serves as a space-separated entry for recipe search.
     */
    private TextField searchField;

    HomeTab() throws SQLException {
        super("Home");

        setClosable(false);

        // Primary layout for the tab
        VBox primaryLayout = new VBox();
        primaryLayout.setPadding(new Insets(16, 16, 16, 16));
        primaryLayout.setSpacing(20);
        primaryLayout.setAlignment(Pos.CENTER);
        primaryLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Logo
        Image logo = new Image("/assets/logo.png");
        ImageView logoView = new ImageView(logo);

        // Secondary layout to house input and submit button
        HBox secondaryLayout = new HBox();
        secondaryLayout.setSpacing(10);
        secondaryLayout.setAlignment(Pos.CENTER);

        // Text field for inputting the ingredients or recipe name
        searchField = new TextField();
        searchField.setPromptText("Enter ingredients...");
        searchField.setPrefSize(250, TextField.USE_COMPUTED_SIZE);
        searchField.setFocusTraversable(false);
        searchField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
            	KeyCode key = k.getCode();
            	if(key == KeyCode.ENTER){
            	try {
    				search();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

                searchField.setText("");
            }
            }
        });

        // Button to submit the query
        Button searchButton = new Button("Search");
        searchButton.setOnAction(a -> {
            try {
				search();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            searchField.setText("");
        });

        // Button to open a "New Recipe" window
        Button newRecipeButton = new Button("New Recipe...");
        newRecipeButton.setOnAction(a -> {
            try {
                createNewRecipe();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Add all items to the secondary layout
        secondaryLayout.getChildren().addAll(searchField, searchButton);

        // Add all items to the primary layout
        primaryLayout.getChildren().addAll(logoView,secondaryLayout, newRecipeButton);

        // Set primary layout as the content of the tab
        setContent(primaryLayout);
    }

    /**
     * Searches by space-separated keywords.
     */
    private void search() throws ClassNotFoundException, SQLException {
        try {
            SearchTab newSearchTab;

            if (searchField.getText() == "") {
                newSearchTab = new SearchTab();
            } else {
                newSearchTab = new SearchTab(searchField.getText());
            }

            getTabPane().getTabs().add(newSearchTab);
            getTabPane().getSelectionModel().select(newSearchTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a new RecipeTab to write a new recipe.
     */
    private void createNewRecipe() throws SQLException {
        try {
            RecipeTab newRecipeTab = new RecipeTab();

            getTabPane().getTabs().add(newRecipeTab);
            getTabPane().getSelectionModel().select(newRecipeTab);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
