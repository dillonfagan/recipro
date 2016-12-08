import java.util.ArrayList;

/**
 * Created by dillon on 9/19/16.
 */
public class Recipe {

    private String index;
    private String title;
    private String text;

    public Recipe() {}

    public Recipe(String index, String title, String text) {
        this.index = index;
        this.title = title;
        this.text = text;
    }

    public String getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
