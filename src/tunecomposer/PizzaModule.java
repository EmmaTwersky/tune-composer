/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.NONE;
import javafx.scene.control.ButtonType;

/**
 * Pizza Module orders a pizza or many pizzas depending on the time of day.
 * The pizza is typically from Domino's but not always.
 * The pizza will ALWAYS come the immediate location of the user.
 * User's credit card will be charged automatically for pizza purchases.
 * While Domino's has a 30-minutes-or-less refund policy,
 * such guarantees to not translate to SynergyTM.
 * 
 */
public class PizzaModule {
    
    /**
     * Can you smell the Pepperoni? I can!
     */
    public static void orderPizza(){
        Alert about = new Alert(NONE);
        about.setTitle("Delivery");
        about.setResizable(true);
        about.getDialogPane().setPrefSize(300, 70);
        String text = "Your pizza is on its way!";
        about.setContentText(text);
        about.getDialogPane().getButtonTypes().add(ButtonType.OK);
        about.showAndWait();
    }
    
}
