/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nmproto;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author kenb
 */
public class NMProto extends Application {
    
    
    public void testHandler(ActionEvent event)
    {
        System.out.println("Hello World!");
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        MainWnd frm = new MainWnd();
        frm.setVisible(true);       
        
        
//        
//        
//        Button btn = new Button();
//        btn.setText("Get Server IP");
//        btn.setOnAction( event-> testHandler(event));
//        
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        
//        Scene scene = new Scene(root, 800, 600);
//        
//        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
