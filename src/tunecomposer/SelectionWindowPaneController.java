package tunecomposer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class SelectionWindowPaneController implements Initializable {
    /**
     * Initialize a Rectangle window to select notes on dragged.
     */
    @FXML
    public Pane selectionWindowPane;
            
    @FXML
    public Rectangle SELECTION_WINDOW;
    
    /**
     * The coordinates of the selection window to be manipulated when dragged.
     */
    public double topX;
    public double topY;
    public double width;
    public double height;
    public double dragStartX;
    public double dragStartY;
    
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        SELECTION_WINDOW.setVisible(false);
    }
    
    public void translateWindow(double x, double y) {           
        if ((x > dragStartX) && (y > dragStartY)) {
            width = x - dragStartX;
            height = y - dragStartY;
        }
        else if ((x > dragStartX) && (y < dragStartY)) {
            width = x - dragStartX;
            height = dragStartY - y;
            topY = y;
        }
        else if ((x < dragStartX) && (y > dragStartY)) {
            width = dragStartX - x;
            height = y - dragStartY;
            topX = x;
        }
        else {
            width = dragStartX - x;
            height = dragStartY - y;
            topX = x;
            topY = y;
        }
        SELECTION_WINDOW.setX(topX);
        SELECTION_WINDOW.setY(topY);
        SELECTION_WINDOW.setWidth(width);
        SELECTION_WINDOW.setHeight(height);
    }
    
    public void resetWindow() {
        topX = 2000;
        topY = 1280;
    }
}