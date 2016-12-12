import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Simple conversion calculator to assist user in the cooking process.
 * Converts between metric and english units of volume.
 * @author Marc Betancourt
 *
 */
public class CalcTab extends Tab {

	CalcTab() {
		super("Conversion Assistant");

		// Primary layout for the tab
        VBox primaryLayout = new VBox();
        primaryLayout.setPadding(new Insets(20, 20, 20, 20));
        primaryLayout.setSpacing(20);
        primaryLayout.setAlignment(Pos.CENTER);
        primaryLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

	}
}
