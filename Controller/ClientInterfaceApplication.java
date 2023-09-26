package Controller;

import Model.Drink;
import Model.Food;
import Repository.*;
import SQLDataBase.DrinkDataBase;
import SQLDataBase.FoodDataBase;
import Service.DrinkService;
import Service.FoodService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public class ClientInterfaceApplication extends Application {
    @Override
    public void start(Stage stage) throws SQLException {

        DrinkDataBase drinksDataBase = new DrinkDataBase();

        List<Drink> beers = drinksDataBase.loadDrinks("Beers");
        List<Drink> cocktails = drinksDataBase.loadDrinks("Cocktails");
        List<Drink> coffees = drinksDataBase.loadDrinks("Coffees");
        List<Drink> juices = drinksDataBase.loadDrinks("Juices");
        List<Drink> soft_drinks = drinksDataBase.loadDrinks("SoftDrinks");
        List<Drink> teas = drinksDataBase.loadDrinks("Teas");
        List<Drink> wines = drinksDataBase.loadDrinks("Wines");
        List<Drink> strong_drinks = drinksDataBase.loadDrinks("StrongDrinks");

        drinksDataBase.close();

        FoodDataBase foodDataBase = new FoodDataBase();

        List<Food> salads =foodDataBase.loadFoods("Salad");
        List<Food> soups = foodDataBase.loadFoods("Soup");
        List<Food> smallBites = foodDataBase.loadFoods("SmallBites");
        List<Food> meat = foodDataBase.loadFoods("Meat");
        List<Food> pasta = foodDataBase.loadFoods("Pasta");
        List<Food> pizza = foodDataBase.loadFoods("Pizza");
        List<Food> seafood = foodDataBase.loadFoods("Seafood");
        List<Food> cakes = foodDataBase.loadFoods("Cakes");
        List<Food> iceScreams = foodDataBase.loadFoods("IceScream");
        List<Food> otherDesserts = foodDataBase.loadFoods("OtherDesserts");

        foodDataBase.close();

        AlcoholicDrinkRepository alcoholicDrinkRepository = new AlcoholicDrinkRepository(beers, wines, cocktails, strong_drinks);
        NonAlcoholicDrinkRepository nonAlcoholicDrinkRepository = new NonAlcoholicDrinkRepository(coffees, teas, juices, soft_drinks);
        DessertFoodRepository dessertFoodRepository = new DessertFoodRepository(cakes, iceScreams, otherDesserts);
        MainFoodRepository mainFoodRepository = new MainFoodRepository(meat, pasta, seafood, pizza);
        StarterFoodRepository starterFoodRepository = new StarterFoodRepository(salads, soups, smallBites);

        DrinkService drinkService = new DrinkService(alcoholicDrinkRepository, nonAlcoholicDrinkRepository, drinksDataBase);
        FoodService foodService = new FoodService(starterFoodRepository, mainFoodRepository, dessertFoodRepository, foodDataBase);

        StartMenuController startMenuController = new StartMenuController();
        startMenuController.setupStartMenu();

        YourOrderViewController yourOrderViewController = new YourOrderViewController();

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(startMenuController.getLeftMenu());
        borderPane.setRight(yourOrderViewController.getOrderBox());

        DrinkMenuController drinkMenuController = new DrinkMenuController();
        FoodMenuController foodMenuController = new FoodMenuController();
        RateFoodController rateFoodController = new RateFoodController(yourOrderViewController);
        DrinkFoodViewerController drinkDrinkFoodViewerController = new DrinkFoodViewerController(yourOrderViewController, drinkMenuController, foodMenuController, startMenuController, rateFoodController);
        DrinkFoodViewerController foodDrinkFoodViewerController = new DrinkFoodViewerController(yourOrderViewController, drinkMenuController, foodMenuController, startMenuController, rateFoodController);

        HBox drinksActionBox = new HBox(10, drinkMenuController.getDrinksMenuBox(), drinkDrinkFoodViewerController.getMiddleBox());
        HBox foodActionBox = new HBox(10, foodMenuController.getFoodMenuBox(), foodDrinkFoodViewerController.getMiddleBox());

        startMenuController.setupDrinksButton(borderPane, drinksActionBox, yourOrderViewController);
        startMenuController.setupFoodButton(borderPane, foodActionBox, yourOrderViewController);

        drinkMenuController.setupBeersButton(drinkDrinkFoodViewerController, beers);
        drinkMenuController.setupWinesButton(drinkDrinkFoodViewerController, wines);
        drinkMenuController.setupCocktailsButton(drinkDrinkFoodViewerController, cocktails);
        drinkMenuController.setupStrongDrinksButton(drinkDrinkFoodViewerController, strong_drinks);
        drinkMenuController.setupCoffeesButton(drinkDrinkFoodViewerController, coffees);
        drinkMenuController.setupTeasButton(drinkDrinkFoodViewerController, teas);
        drinkMenuController.setupJuicesButton(drinkDrinkFoodViewerController, juices);
        drinkMenuController.setupSoftDrinksButton(drinkDrinkFoodViewerController, soft_drinks);

        foodMenuController.setupSaladButton(foodDrinkFoodViewerController, salads);
        foodMenuController.setupSoupButton(foodDrinkFoodViewerController, soups);
        foodMenuController.setupSmallBitesButton(foodDrinkFoodViewerController, smallBites);
        foodMenuController.setupMeatButton(foodDrinkFoodViewerController, meat);
        foodMenuController.setupPastaButton(foodDrinkFoodViewerController, pasta);
        foodMenuController.setupSeaFoodButton(foodDrinkFoodViewerController, seafood);
        foodMenuController.setupPizzaButton(foodDrinkFoodViewerController, pizza);
        foodMenuController.setupCakesButton(foodDrinkFoodViewerController, cakes);
        foodMenuController.setupIceScreamButton(foodDrinkFoodViewerController, iceScreams);
        foodMenuController.setupOtherDessertsButton(foodDrinkFoodViewerController, otherDesserts);

        drinkDrinkFoodViewerController.setupAddButton(true);
        foodDrinkFoodViewerController.setupAddButton(false);

        drinkDrinkFoodViewerController.setupSearchButton(beers, wines, cocktails, strong_drinks, coffees, teas, juices, soft_drinks,
                                                        salads, soups, smallBites, meat, pasta, seafood, pizza, cakes, iceScreams, otherDesserts);
        foodDrinkFoodViewerController.setupSearchButton(beers, wines, cocktails, strong_drinks, coffees, teas, juices, soft_drinks,
                                                        salads, soups, smallBites, meat, pasta, seafood, pizza, cakes, iceScreams, otherDesserts);

        drinkDrinkFoodViewerController.setupUndoButton(borderPane, yourOrderViewController);
        foodDrinkFoodViewerController.setupUndoButton(borderPane, yourOrderViewController);

        yourOrderViewController.setupRemoveButton(rateFoodController);
        yourOrderViewController.setupPlaceOrderButton(borderPane, rateFoodController);
        yourOrderViewController.setupBackButton(borderPane, drinksActionBox, foodActionBox, startMenuController);
        yourOrderViewController.setupCashButton(borderPane);
        yourOrderViewController.setupCardButton(borderPane);
        yourOrderViewController.setupModifyPaymentMethod(borderPane);
        yourOrderViewController.setupViewOrderButton(borderPane);
        yourOrderViewController.setupOkButton(borderPane);
        yourOrderViewController.setupModifyButton(borderPane, startMenuController);
        yourOrderViewController.setupSubmitButton(borderPane);
        yourOrderViewController.setupBackToMenuButton(borderPane, startMenuController, rateFoodController);

        rateFoodController.setupRateFoodButton(borderPane, yourOrderViewController);
        rateFoodController.setupBackButton(borderPane);

        borderPane.setStyle("-fx-background-color: #01fdcf;");

        Scene scene = new Scene(borderPane, 1200, 720);
        scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());
        stage.setTitle("CLIENT APPLICATION");
        stage.setScene(scene);
        stage.show();

        double initialWidth = stage.getWidth();
        double initialHeight = stage.getHeight();

        stage.setMinWidth(initialWidth);
        stage.setMinHeight(initialHeight);
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}