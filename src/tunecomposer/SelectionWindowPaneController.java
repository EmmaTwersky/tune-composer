package tunecomposer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Controls the selection window rectangle. 
 */
public class SelectionWindowPaneController implements Initializable {
    
    /**
     * Initialize a Rectangle window to select notes on dragged.
     */
    @FXML
    public Pane selectionWindowPane;
            
    /**
     * The visual rectangle object that will be displayed in the composition
     * pane while selecting an area.
     */
    @FXML
    public Rectangle SELECTION_WINDOW;
    
    /**
     * The SELECTION_WINDOW rectangle's x and y coordinates.
     */
    private double topX;
    private double topY;
    
    /**
     * Size of the SELECTION_WINDOW rectangle.
     */
    public double width;
    public double height;
    
    /** 
     * Coordinates where the mouse was first clicked. 
     * Used for scaling SELECTION_WINDOW's size.
     */
    private double dragStartX;
    private double dragStartY;
    
    /**
     * Sets original SELECTION_WINDOW to not show.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        SELECTION_WINDOW.setVisible(false);
    }
    
    /**
     * Manage scaling and movement of SELECTION_WINDOW for given mouse coordinates.
     * Will display the rectangle if the mouse is moved left, right, down or up
     * from the given start coordinates.
     * @param x give the y coordinate of where the mouse has been dragged to
     * @param y give the y coordinate of where the mouse has been dragged to
     */
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
    
    /**
     * Set topX and topY to given values.
     * @param x double type, and give coordinates not increments
     * @param y double type, and give coordinates not increments
     */
    public void setTopCoords(double x, double y) {
        topX = x;
        topY = y;
    }
    
    /**
     * Set initial click coordinates to anchor a corner ofSELECTION_WINDOW. 
     * @param x double type, and give coordinates not increments
     * @param y double type, and give coordinates not increments
     */
    public void setDragStartCoords(double x, double y) {
        dragStartX = x;
        dragStartY = y;
    }
    
    /**
     * Resets dragStartX and topX, which prepares SELECTION_WINDOW for new dragging.
     * @param x
     * @param y 
     */
    public void resetWindowCoords(double x, double y) {
        dragStartX = x;
        topX = x;
        
        dragStartY = y;
        topY = y;
    }
}