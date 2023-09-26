package Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class StartMenuController {
    private Label welcomeLabel;
    private Button drinksButton;
    private Button foodButton;
    private VBox leftMenu;
    private String currentContext;

    public StartMenuController()
    {
        this.welcomeLabel = new Label("WELCOME!");
        this.drinksButton = new Button("DRINKS");
        this.foodButton = new Button("FOOD");
        this.currentContext = "";
    }

    public void setupStartMenu()
    {
        {
            welcomeLabel.getStyleClass().add("welcome-label");
            welcomeLabel.setStyle("-fx-font-size: 100px;");

            drinksButton.getStyleClass().add("menu-button");
            foodButton.getStyleClass().add("menu-button");
        }

        drinksButton.setPrefSize(340, 340);
        foodButton.setPrefSize(340, 340);

        HBox menuButtons = new HBox(40, drinksButton, foodButton);
        HBox.setMargin(drinksButton, new Insets(0, 0, 0, 40));
        menuButtons.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(60);

        leftMenu = new VBox(20, welcomeLabel, menuButtons, spacer);
        leftMenu.setAlignment(Pos.CENTER);
    }

    public void setupDrinksButton(BorderPane borderPane, HBox drinkActionBox, YourOrderViewController yourOrderViewController)
    {
        this.drinksButton.setOnAction(e -> {
            borderPane.setLeft(drinkActionBox);
            borderPane.setRight(yourOrderViewController.getOrderBox());
            this.currentContext = "drinks";
        });
    }

    public void setupFoodButton(BorderPane borderPane, HBox foodActionBox, YourOrderViewController yourOrderViewController)
    {
        this.foodButton.setOnAction(e -> {
            borderPane.setLeft(foodActionBox);
            borderPane.setRight(yourOrderViewController.getOrderBox());
            this.currentContext = "foods";
        });
    }

    public String getCurrentContext() {
        return currentContext;
    }

    public VBox getLeftMenu() {
        return leftMenu;
    }
}
