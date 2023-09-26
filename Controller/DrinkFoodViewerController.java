package Controller;

import Model.Drink;
import Model.Food;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class DrinkFoodViewerController {
    private TextField searchField;
    private Button searchButton;
    private ListView<Object> container;
    private Button addButton;
    private Button undoButton;
    private HBox searchBox;
    private HBox buttonsBox;
    private VBox middleBox;
    private YourOrderViewController yourOrderViewController;
    private DrinkMenuController drinkMenuController;
    private FoodMenuController foodMenuController;
    private StartMenuController startMenuController;
    private RateFoodController rateFoodController;

    public DrinkFoodViewerController(YourOrderViewController yourOrderViewController, DrinkMenuController drinkMenuController, FoodMenuController foodMenuController, StartMenuController startMenuController, RateFoodController rateFoodController)
    {
        this.searchField = new TextField();
        this.searchButton = new Button("SEARCH");
        this.container = new ListView<>();
        this.addButton = new Button("ADD");
        this.undoButton = new Button("BACK");
        this.yourOrderViewController = yourOrderViewController;
        this.drinkMenuController = drinkMenuController;
        this.foodMenuController = foodMenuController;
        this.startMenuController = startMenuController;
        this.rateFoodController = rateFoodController;

        this.setupStylesAndDesign();

        this.setupContainerCellFactory();
    }

    public void setupStylesAndDesign()
    {
        this.searchBox = new HBox( 10,searchField, searchButton);
        this.buttonsBox = new HBox(10, undoButton, addButton);

        this.middleBox = new VBox(10, searchBox, container, buttonsBox);

        this.container.setPrefWidth(450);
        this.container.setPrefHeight(596);

        this.undoButton.setPrefHeight(50);
        this.undoButton.setPrefWidth(265);
        this.addButton.setPrefHeight(50);
        this.addButton.setPrefWidth(265);

        this.searchButton.setPrefHeight(50);
        this.searchButton.setPrefWidth(130);

        this.searchField.setPrefHeight(50);
        this.searchField.setPrefWidth(400);

        Label menuLabel = new Label("Menu");
        container.setPlaceholder(menuLabel);

        {
            this.searchButton.getStyleClass().add("menu-button");
            this.undoButton.getStyleClass().add("menu-button");
            this.addButton.getStyleClass().add("menu-button");

            this.searchField.setPromptText("Search...");
            searchField.setStyle("-fx-font-size: 20px;");
            searchField.getStyleClass().add("drinks-label");

            container.getStyleClass().add("order-view");

            menuLabel.getStyleClass().add("order-text");
            container.getStyleClass().add("order-view-empty");
        }
    }

    public void displayFood(List<Food> foods)
    {
        this.container.getItems().clear();
        this.container.getItems().addAll(foods);
    }

    public void displayDrink(List<Drink> drinks)
    {
        this.container.getItems().clear();
        this.container.getItems().addAll(drinks);
    }


    public void addDrinkToOrder()
    {
        Object selectedI = this.container.getSelectionModel().getSelectedItem();

        if (selectedI instanceof Drink)
        {
            Drink selectedItem = (Drink) selectedI;

            Label nameLabel = new Label(String.valueOf(selectedItem.getName()));
            Label volumeLabel = new Label(String.valueOf(selectedItem.getVolume()));
            Label priceLabel = new Label(String.valueOf(selectedItem.getPrice()));

            Label newNameLabel = new Label(nameLabel.getText());
            Label newVolumeLabel = new Label(volumeLabel.getText() + "ML");
            Label newPriceLabel = new Label(priceLabel.getText() + "LEI");

            {
                newNameLabel.getStyleClass().add("drinks-label");
                newVolumeLabel.getStyleClass().add("drinks-label");
                newPriceLabel.getStyleClass().add("drinks-label");
            }

            HBox itemToAdd = new HBox(10, newNameLabel, newVolumeLabel, newPriceLabel);


            yourOrderViewController.addItemToOrder(itemToAdd);
            yourOrderViewController.updateTotalSum(Float.parseFloat(newPriceLabel.getText().replace("LEI", "")));

            this.container.getSelectionModel().clearSelection();
        }
    }

    public void addFoodToOrder()
    {
        Object selectedI = this.container.getSelectionModel().getSelectedItem();

        if (selectedI instanceof Food)
        {
            Food selectedItem = (Food) selectedI;

            Label nameLabel = new Label(selectedItem.getName());
            Label gramsLabel = new Label(String.valueOf(selectedItem.getGrams()));
            Label priceLabel = new Label(String.valueOf(selectedItem.getPrice()));
            Label ratingLabel = new Label(String.valueOf(selectedItem.getRating()));

            Label newNameLabel = new Label(nameLabel.getText());
            Label newGramsLabel = new Label(gramsLabel.getText() + "GR");
            Label newPriceLabel = new Label(priceLabel.getText() + "LEI");
            Label newRatingLabel = new Label(ratingLabel.getText() + "☆");

            {
                newNameLabel.getStyleClass().add("drinks-label");
                newGramsLabel.getStyleClass().add("drinks-label");
                newPriceLabel.getStyleClass().add("drinks-label");
                newRatingLabel.getStyleClass().add("drinks-label");
            }

            HBox itemToAdd = new HBox(10, newNameLabel, newGramsLabel, newPriceLabel, newRatingLabel);

            yourOrderViewController.addItemToOrder(itemToAdd);
            yourOrderViewController.updateTotalSum(Float.parseFloat(newPriceLabel.getText().replace("LEI", "")));

            this.container.getSelectionModel().clearSelection();
            rateFoodController.getOrderedFood().add(selectedItem);
        }
    }

    public void showFilteredDrinks(List<Drink> drinks)
    {
        String query = searchField.getText().toLowerCase().trim();

        System.out.println(query);

        List<Drink> filteredDrinks = new ArrayList<>();

        for (Drink drink : drinks)
            if (drink.getName().toLowerCase().contains(query))
                filteredDrinks.add(drink);

        this.displayDrink(filteredDrinks);
    }

    public void showFilteredFood(List<Food> foods)
    {
        String query = searchField.getText().toLowerCase().trim();

        List<Food> filteredFoods = new ArrayList<>();

        for (Food food : foods)
            if (food.getName().toLowerCase().contains(query))
                filteredFoods.add(food);

        this.displayFood(filteredFoods);
    }

    public void setupAddButton(boolean displayingDrinks)
    {
        this.addButton.setOnAction(e -> {

                if (displayingDrinks)
                    addDrinkToOrder();
                else
                    addFoodToOrder();
        });
    }

    public void searchBeers(List<Drink> beers)
    {
        this.showFilteredDrinks(beers);
    }

    public void searchWines(List<Drink> wines)
    {
        this.showFilteredDrinks(wines);
    }

    public void searchCocktails(List<Drink> cocktails)
    {
        this.showFilteredDrinks(cocktails);
    }

    public void searchStrongDrinks(List<Drink> strongDrinks)
    {
        this.showFilteredDrinks(strongDrinks);
    }

    public void searchCoffees(List<Drink> coffees)
    {
        this.showFilteredDrinks(coffees);
    }

    public void searchTeas(List<Drink> teas)
    {
        this.showFilteredDrinks(teas);
    }

    public void searchJuices(List<Drink> juices)
    {
        this.showFilteredDrinks(juices);
    }

    public void searchSoftDrinks(List<Drink> softDrinks)
    {
        this.showFilteredDrinks(softDrinks);
    }

    public void searchSalad(List<Food> salads)
    {
        this.showFilteredFood(salads);
    }

    public void searchSoup(List<Food> soup)
    {
        this.showFilteredFood(soup);
    }

    public void searchSmallBites(List<Food> smallBites)
    {
        this.showFilteredFood(smallBites);
    }

    public void searchMeat(List<Food> meat)
    {
        this.showFilteredFood(meat);
    }

    public void searchPasta(List<Food> pasta)
    {
        this.showFilteredFood(pasta);
    }

    public void searchSeaFood(List<Food> seaFood)
    {
        this.showFilteredFood(seaFood);
    }

    public void searchPizza(List<Food> pizza)
    {
        this.showFilteredFood(pizza);
    }

    public void  searchCakes(List<Food> cakes)
    {
        this.showFilteredFood(cakes);
    }

    public void searchIceScream(List<Food> iceScream)
    {
        this.showFilteredFood(iceScream);
    }

    public void searchOtherDesserts(List<Food> otherDesserts)
    {
        this.showFilteredFood(otherDesserts);
    }

    public void setupSearchButton(List<Drink> beers, List<Drink> wines, List<Drink> cocktails, List<Drink> strongDrinks,
                                        List<Drink> coffees, List<Drink> teas, List<Drink> juices, List<Drink> softDrinks,
                                        List<Food> salads, List<Food> soups, List<Food> smallBites, List<Food> meat, List<Food> pasta,
                                        List<Food> seaFood, List<Food> pizza, List<Food> cakes, List<Food> iceScream, List<Food> otherDesserts)
    {
        this.searchButton.setOnAction(e -> {
            if (startMenuController.getCurrentContext().equals("drinks"))
            {
                if (drinkMenuController.getCurrentCategory().equals("beers"))
                    this.searchBeers(beers);
                else if (drinkMenuController.getCurrentCategory().equals("wines"))
                    this.searchWines(wines);
                else if (drinkMenuController.getCurrentCategory().equals("cocktails"))
                    this.searchCocktails(cocktails);
                else if (drinkMenuController.getCurrentCategory().equals("strongDrinks"))
                    this.searchStrongDrinks(strongDrinks);
                else if (drinkMenuController.getCurrentCategory().equals("coffees"))
                    this.searchCoffees(coffees);
                else if (drinkMenuController.getCurrentCategory().equals("teas"))
                    this.searchTeas(teas);
                else if (drinkMenuController.getCurrentCategory().equals("juices"))
                    this.searchJuices(juices);
                else if (drinkMenuController.getCurrentCategory().equals("softDrinks"))
                    this.searchSoftDrinks(softDrinks);
            }
            else if (startMenuController.getCurrentContext().equals("foods"))
            {
                if (foodMenuController.getCurrentCategory().equals("salad"))
                    this.searchSalad(salads);
                else if (foodMenuController.getCurrentCategory().equals("soup"))
                    this.searchSoup(soups);
                else if (foodMenuController.getCurrentCategory().equals("smallBites"))
                    this.searchSmallBites(smallBites);
                else if (foodMenuController.getCurrentCategory().equals("meat"))
                    this.searchMeat(meat);
                else if (foodMenuController.getCurrentCategory().equals("pasta"))
                    this.searchPasta(pasta);
                else if (foodMenuController.getCurrentCategory().equals("seaFood"))
                    this.searchSeaFood(seaFood);
                else if (foodMenuController.getCurrentCategory().equals("pizza"))
                    this.searchPizza(pizza);
                else if (foodMenuController.getCurrentCategory().equals("cakes"))
                    this.searchCakes(cakes);
                else if (foodMenuController.getCurrentCategory().equals("iceScream"))
                    this.searchIceScream(iceScream);
                else if (foodMenuController.getCurrentCategory().equals("otherDesserts"))
                    this.searchOtherDesserts(otherDesserts);
            }
        });
    }

    public void setupUndoButton(BorderPane borderPane, YourOrderViewController yourOrderViewController)
    {
        this.undoButton.setOnAction(e -> {
            borderPane.setLeft(startMenuController.getLeftMenu());
            borderPane.setRight(yourOrderViewController.getOrderBox());
        });
    }

    public void setupContainerCellFactory()
    {
        container.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Object> call(ListView<Object> objectListView) {
                return new ListCell<Object>() {
                    HBox cellBox = new HBox();
                    ImageView imageView = new ImageView();
                    Label nameHeaderDrinkLabel = new Label("NAME");
                    Label volumeHeaderDrinkLabel = new Label("VOLUME");
                    Label priceHeaderDrinkLabel = new Label("PRICE");
                    Label nameHeaderFoodLabel = new Label("NAME");
                    Label descriptionHeaderFoodLabel = new Label("DESCRIPTION");
                    Label gramsHeaderFoodLabel = new Label("GRAMS");
                    Label priceHeaderFoodLabel = new Label("PRICE");
                    Label ratingHeaderFoodLabel = new Label("RATING");
                    Label nameDrinkLabel = new Label();
                    Label volumeDrinkLabel = new Label();
                    Label priceDrinkLabel = new Label();
                    Label nameFoodLabel = new Label();
                    Label descriptionFoodLabel = new Label();
                    Label gramsFoodLabel = new Label();
                    Label priceFoodLabel = new Label();
                    Label ratingFoodLabel = new Label();

                    {
                        nameHeaderDrinkLabel.getStyleClass().add("welcome-label");
                        volumeHeaderDrinkLabel.getStyleClass().add("welcome-label");
                        priceHeaderDrinkLabel.getStyleClass().add("welcome-label");
                        nameHeaderFoodLabel.getStyleClass().add("welcome-label");
                        descriptionHeaderFoodLabel.getStyleClass().add("welcome-label");
                        gramsHeaderFoodLabel.getStyleClass().add("welcome-label");
                        priceHeaderFoodLabel.getStyleClass().add("welcome-label");
                        ratingHeaderFoodLabel.getStyleClass().add("welcome-label");

                        this.setPrefHeight(60);
                    }

                    @Override
                    protected void updateItem(Object obj, boolean empty) {
                        super.updateItem(obj, empty);

                        if (empty) {
                            this.clearContent();
                        } else {

                            if (obj instanceof Drink) {
                                this.updateDrinkCell((Drink) obj);
                            } else if (obj instanceof Food) {
                                this.updateFoodCell((Food) obj);
                            }
                        }
                    }

                    public void clearContent()
                    {
                        setText(null);
                        setGraphic(null);
                    }

                    public void updateDrinkCell(Drink drink)
                    {
                        cellBox.getChildren().clear();
                        cellBox.getProperties().put("type", "drink");

                        if (getIndex() == 0) {
                            this.createDrinkHeaders();
                        }
                        else
                        {
                            this.createDrinkContent(drink);
                        }

                        setGraphic(cellBox);
                    }

                    public void updateFoodCell(Food food)
                    {
                        cellBox.getChildren().clear();
                        cellBox.getProperties().put("type", "food");

                        if (getIndex() == 0) {
                            this.createFoodHeaders();

                        } else {
                            this.createFoodContent(food);
                        }

                        setGraphic(cellBox);
                    }

                    private void createDrinkHeaders() {
                        cellBox.getChildren().addAll(nameHeaderDrinkLabel, volumeHeaderDrinkLabel, priceHeaderDrinkLabel);
                        cellBox.setSpacing(100);

                        cellBox.getStyleClass().add("header-cell");
                    }

                    private void createDrinkContent(Drink drink) {
                        if (drinkMenuController.getCurrentCategory().equals("coffees")) {
                            imageView = new ImageView(new Image("coffee_its_benefits_898_1_.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("teas")) {
                            imageView = new ImageView(new Image("scented-teas.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("juices")) {
                            imageView = new ImageView(new Image("istockphoto-158268808-612x612.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("softDrinks")) {
                            imageView = new ImageView(new Image("Soft-drink-health-concerns-not-yet-trickled-down-into-social-media-users-mentions-of-brands.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("beers")) {
                            imageView = new ImageView(new Image("1200px-NCI_Visuals_Food_Beer.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("wines")) {
                            imageView = new ImageView(new Image("Wine-Guide-Beaujolais-FT-BLOG0722-2000-7f1cac81f5044d3cbfeac708b66c4bea.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("cocktails")) {
                            imageView = new ImageView(new Image("Zombie_Cocktail_2667x2667_primary-4416b8395efd4a3986c209371e628e63.jpg"));
                        } else if (drinkMenuController.getCurrentCategory().equals("strongDrinks")) {
                            imageView = new ImageView(new Image("bottle-review_Jack-Daniels-Tennessee-Whiskey_1500x1500-d6c4b98c23d44bc8b3eeaaabb3d6308d.jpg"));
                        }

                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);

                        nameDrinkLabel = new Label(drink.getName());
                        volumeDrinkLabel = new Label(drink.getVolume() + " ML");
                        priceDrinkLabel = new Label(drink.getPrice() + " LEI");

                        {
                            nameDrinkLabel.getStyleClass().add("drinks2-label");
                            volumeDrinkLabel.getStyleClass().add("drinks2-label");
                            priceDrinkLabel.getStyleClass().add("drinks2-label");
                        }

                        HBox nameAndImageBox = new HBox(10, nameDrinkLabel, imageView);

                        cellBox.getChildren().addAll(nameAndImageBox, volumeDrinkLabel, priceDrinkLabel);
                        cellBox.setSpacing(75);

                        cellBox.getStyleClass().add("rest-cell");
                    }

                    private void createFoodHeaders() {
                        cellBox.getChildren().addAll(nameHeaderFoodLabel, descriptionHeaderFoodLabel, gramsHeaderFoodLabel, priceHeaderFoodLabel, ratingHeaderFoodLabel);
                        cellBox.setSpacing(30);

                        cellBox.getStyleClass().add("header-cell");
                    }

                    private void createFoodContent(Food food) {
                        if (foodMenuController.getCurrentCategory().equals("salad")) {
                            imageView = new ImageView(new Image("Big-Italian-Salad.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("soup")) {
                            imageView = new ImageView(new Image("Homemade-Vegetable-Soup-Recipe-2-1200.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("smallBites")) {
                            imageView = new ImageView(new Image("8ed6ae3208df16bda783b803f68666f8.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("meat")) {
                            imageView = new ImageView(new Image("raw-meat.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("pasta")) {
                            imageView = new ImageView(new Image("paste_italiene_cu_ierburi_800.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("seaFood")) {
                            imageView = new ImageView(new Image("photo-1615141982883-c7ad0e69fd62.jpeg"));
                        } else if (foodMenuController.getCurrentCategory().equals("pizza")) {
                            imageView = new ImageView(new Image("Pizza-3007395.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("cakes")) {
                            imageView = new ImageView(new Image("Lady-Red-Velvet-Cake---London_-Surrey_-Berkshire_1600x.jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("iceScream")) {
                            imageView = new ImageView(new Image("Ice_cream_with_whipped_cream,_chocolate_syrup,_and_a_wafer_(cropped).jpg"));
                        } else if (foodMenuController.getCurrentCategory().equals("otherDesserts")) {
                            imageView = new ImageView(new Image("Desserts.jpg"));
                        }

                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);

                        nameFoodLabel = new Label(food.getName());
                        descriptionFoodLabel = new Label(food.getDescription());
                        gramsFoodLabel = new Label(food.getGrams() + " GR");
                        priceFoodLabel = new Label(food.getPrice() + " LEI");
                        ratingFoodLabel = new Label(food.getRating() + "☆");

                        descriptionFoodLabel.setWrapText(true);
                        descriptionFoodLabel.setMaxWidth(150);

                        {
                            nameFoodLabel.getStyleClass().add("drinks2-label");
                            descriptionFoodLabel.getStyleClass().add("description-label");
                            gramsFoodLabel.getStyleClass().add("drinks2-label");
                            priceFoodLabel.getStyleClass().add("drinks2-label");
                            ratingFoodLabel.getStyleClass().add("drinks2-label");
                        }

                        HBox nameAndImageBox = new HBox(10, nameFoodLabel, imageView);

                        cellBox.getChildren().addAll(nameAndImageBox, descriptionFoodLabel, gramsFoodLabel, priceFoodLabel, ratingFoodLabel);
                        cellBox.setSpacing(75);

                        cellBox.getStyleClass().add("rest-cell");
                    }
                };
            }
        });
    }

    public VBox getMiddleBox() {
        return middleBox;
    }
}
