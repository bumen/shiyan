package com.bmn.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class HelloFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("javafx sample");
        Button button = new Button();
        button.setText("Button");

        final Label label = new Label();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText("Hello World");
            }
        });

        BorderPane pane = new BorderPane();
        pane.setTop(button);
        pane.setBottom(label);

        Group group = new Group(pane);

        MediaView node = new MediaView();

        group.getChildren().add(node);



        primaryStage.setScene(new Scene(group, 400, 300));

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("sthop");
    }
}
