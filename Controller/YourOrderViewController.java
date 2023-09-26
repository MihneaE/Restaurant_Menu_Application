package Controller;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class YourOrderViewController {
    private ListView<Object> order;
    private Label totalSumLabel;
    private Label thankYouLabel;
    private Label placedOrderLabel;
    private Label finalLabel;
    private Label paymentMethodLabel;
    private Label emptyOrderLabel;
    private Button placeOrderButton;
    private Button removeButton;
    private Button cashButton;
    private Button cardButton;
    private Button undoButton;
    private Button viewOrderButton;
    private Button modifyPaymentButton;
    private Button submitButton;
    private Button OkButton;
    private Button modifyButton;
    private Button rateFoodButton;
    private Button backToMenuButton;
    private VBox orderBox;
    private HBox payBox;
    private HBox payBoxWithoutBack;
    private VBox finalPayBox;
    private VBox finalLabelsBox;
    private HBox finalOrderButtons;
    private VBox viewFinalOrderBox;
    private VBox submitButtonBox;
    private float totalSum;
    private String paymentType;
    private Pane labelWrapper;
    private boolean isModifyButtonPressed;

    public YourOrderViewController()
    {
        this.order = new ListView<>();
        this.totalSumLabel = new Label("0.00 RON");
        this.thankYouLabel = new Label("THANK YOU!");
        this.placedOrderLabel = new Label("Your order was placed!");
        this.paymentMethodLabel = new Label();
        this.finalLabel = new Label("Thank you for choosing our restaurant!");
        this.emptyOrderLabel = new Label("Your order");
        this.placeOrderButton = new Button("PLACE ORDER");
        this.removeButton = new Button("REMOVE");
        this.cashButton = new Button("PAY BY CASH");
        this.cardButton = new Button("PAY BY CARD");
        this.undoButton = new Button("BACK");
        this.viewOrderButton = new Button("VIEW YOUR ORDER");
        this.modifyPaymentButton = new Button("MODIFY PAYMENT METHOD");
        this.submitButton = new Button("SUBMIT");
        this.OkButton = new Button("OK");
        this.modifyButton = new Button("MODIFY");
        this.rateFoodButton = new Button("RATE FOOD");
        this.backToMenuButton = new Button("BACK TO START MENU");
        this.totalSum = 0;
        this.paymentType = "";
        this.isModifyButtonPressed = false;

        this.setupDesignAndStylesOfOrderBox();
    }

    public void setupDesignAndStylesOfOrderBox()
    {
        labelWrapper = new Pane(totalSumLabel);

        labelWrapper.setPrefHeight(50);
        labelWrapper.setStyle("-fx-background-color: #b833ff;");

        totalSumLabel.layoutXProperty().bind(labelWrapper.widthProperty().subtract(totalSumLabel.widthProperty()).divide(2));
        totalSumLabel.layoutYProperty().bind(labelWrapper.heightProperty().subtract(totalSumLabel.heightProperty()).divide(2));

        this.orderBox = new VBox(5, order, labelWrapper, removeButton , placeOrderButton);

        labelWrapper.setMaxWidth(Double.MAX_VALUE);

        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);

        placeOrderButton.setPrefHeight(50);
        removeButton.setPrefHeight(50);
        totalSumLabel.setPrefHeight(50);
        totalSumLabel.setAlignment(Pos.CENTER);

        order.setPrefHeight(550);
        order.setPrefWidth(400);

        order.setPlaceholder(emptyOrderLabel);

        {
            placeOrderButton.getStyleClass().add("menu-button");
            removeButton.getStyleClass().add("menu-button");

            totalSumLabel.getStyleClass().add("welcome-label");
            totalSumLabel.setStyle("-fx-text-fill: white;");

            order.getStyleClass().add("order-view");

            emptyOrderLabel.getStyleClass().add("order-text");
        }

        order.setCellFactory(lv -> {
            ListCell<Object> cell = new ListCell<>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        getStyleClass().remove("order-view-filled");
                        getStyleClass().add("list-cell-empty");
                    } else if (item instanceof HBox) {
                        setGraphic((HBox) item);
                        getStyleClass().add("order-view-filled");
                        getStyleClass().remove("list-cell-empty");
                    }
                }
            };
            cell.getStyleClass().add("list-cell");
            return cell;
        });

        order.getStyleClass().add("order-view-empty");

        order.getItems().addListener((ListChangeListener<Object>) c -> {
            if (order.getItems().isEmpty()) {
                order.getStyleClass().remove("order-view-filled");
                order.getStyleClass().add("order-view-empty");
            } else {
                order.getStyleClass().add("order-view-filled");
                order.getStyleClass().remove("order-view-empty");
            }
        });
    }

    public void addItemToOrder(HBox item)
    {
        this.order.getItems().add(item);
    }

    public void updateTotalSum(float sum)
    {
        this.totalSum += sum;
        this.totalSumLabel.setText(totalSum + " LEI");
    }

    public void resetTotalSum()
    {
        this.totalSum = 0;
        this.totalSumLabel.setText(totalSum + " LEI");
    }

    public void setupRemoveButton(RateFoodController rateFoodController)
    {
        this.removeButton.setOnAction(e -> {
            int selectedIndex = this.order.getSelectionModel().getSelectedIndex();

            if (selectedIndex != -1)
            {
                Object selectedItem = this.order.getItems().get(selectedIndex);

                HBox selectedBox = (HBox)selectedItem;

                if (selectedBox.getChildren().size() == 3)
                {
                    Label priceLabel = (Label) selectedBox.getChildren().get(selectedBox.getChildren().size() - 1);
                    float sumToRemove = Float.parseFloat(priceLabel.getText().replace("LEI", ""));

                    this.updateTotalSum(-sumToRemove);
                }
                else if (selectedBox.getChildren().size() == 4)
                {
                    Label priceLabel = (Label) selectedBox.getChildren().get(selectedBox.getChildren().size() - 2);
                    float sumToRemove = Float.parseFloat(priceLabel.getText().replace("LEI", ""));

                    this.updateTotalSum(-sumToRemove);
                }

                this.order.getItems().remove(selectedItem);
                this.order.getSelectionModel().clearSelection();
                rateFoodController.getOrderedFood().remove(selectedIndex);
            }
        });
    }

    public void setupPayWithoutBackButton()
    {
        cashButton.setPrefSize(400, 400);
        cardButton.setPrefSize(400,400);

        {
            cashButton.getStyleClass().add("menu-button");
            cardButton.getStyleClass().add("menu-button");
        }

        payBoxWithoutBack = new HBox(40, cashButton, cardButton);
        payBoxWithoutBack.setAlignment(Pos.CENTER);
    }

    public void setPayMethods()
    {
        cashButton.setPrefSize(400, 400);
        cardButton.setPrefSize(400,400);
        undoButton.setPrefSize(840, 100);

        {
            cashButton.getStyleClass().add("menu-button");
            cardButton.getStyleClass().add("menu-button");
            undoButton.getStyleClass().add("menu-button");
        }

        payBox = new HBox(40, cashButton, cardButton);
        payBox.setAlignment(Pos.CENTER);

        finalPayBox = new VBox(40, payBox, undoButton);
        finalPayBox.setAlignment(Pos.CENTER);
    }

    public void setupPaymentLabel()
    {
        if (paymentType.equals("Cash"))
            paymentMethodLabel.setText("Payment method: Cash");
        else if (paymentType.equals("Card"))
            paymentMethodLabel.setText("Payment method: Card");
    }

    public void setupFinalStage()
    {
        this.setupPaymentLabel();

        viewOrderButton.setPrefSize(300, 100);
        modifyPaymentButton.setPrefSize(300, 100);
        submitButton.setPrefSize(300, 100);
        rateFoodButton.setPrefSize(300, 100);

        {
            viewOrderButton.getStyleClass().add("menu-button");
            modifyPaymentButton.getStyleClass().add("menu-button");
            submitButton.getStyleClass().add("menu-button");
            rateFoodButton.getStyleClass().add("menu-button");

            thankYouLabel.getStyleClass().add("thankYou-label");
            placedOrderLabel.getStyleClass().add("thankYou-label");
            paymentMethodLabel.getStyleClass().add("drinks-label");
        }

        HBox orderButtonsBox = new HBox(40, viewOrderButton, modifyPaymentButton);
        orderButtonsBox.setAlignment(Pos.CENTER);

        HBox rateFoodBox = new HBox(40, rateFoodButton, submitButton);
        rateFoodBox.setAlignment(Pos.CENTER);

        finalLabelsBox = new VBox(40, paymentMethodLabel , orderButtonsBox, thankYouLabel, placedOrderLabel, rateFoodBox);
        finalLabelsBox.setAlignment(Pos.CENTER);
    }

    public void setupViewFinalOrder()
    {
        {
            OkButton.getStyleClass().add("menu-button");
            modifyButton.getStyleClass().add("menu-button");
        }

        OkButton.setPrefSize(150, 50);
        modifyButton.setPrefSize(150, 50);

        finalOrderButtons = new HBox(10, OkButton, modifyButton);
        finalOrderButtons.setAlignment(Pos.CENTER);

        viewFinalOrderBox = new VBox(5, order, labelWrapper,finalOrderButtons);
        viewFinalOrderBox.setAlignment(Pos.CENTER);

        order.setPrefHeight(500);
        order.setPrefWidth(300);
    }

    public void setupSubmitButtonBox()
    {
        {
            finalLabel.getStyleClass().add("welcome-label");
            backToMenuButton.getStyleClass().add("menu-button");
        }

        backToMenuButton.setPrefSize(500, 150);

        submitButtonBox = new VBox(40, finalLabel, backToMenuButton);
        submitButtonBox.setAlignment(Pos.CENTER);
    }

    public void resetOrderBox()
    {
        order.setPrefHeight(550);
        order.setPrefWidth(400);

        labelWrapper.setMaxWidth(Double.MAX_VALUE);

        //totalSumLabel.setMaxWidth(Double.MAX_VALUE);
        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);

        this.orderBox.getChildren().clear();
        this.orderBox.getChildren().addAll(order, labelWrapper, removeButton, placeOrderButton);
    }

    public void clearOrder() {
        this.order.getItems().clear();
    }

    public void setupPlaceOrderButton(BorderPane borderPane, RateFoodController rateFoodController)
    {
        this.placeOrderButton.setOnAction(e -> {

            if (!isModifyButtonPressed)
            {
                if (order.getItems().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("EMPTY ORDER");
                    alert.setHeaderText(null);
                    alert.setContentText("Your order is empty!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
                else
                {
                    this.setPayMethods();
                    borderPane.setLeft(null);
                    borderPane.setRight(null);
                    borderPane.setCenter(this.getFinalPayBox());
                }
            }
            else
            {
                borderPane.setLeft(null);
                borderPane.setRight(null);
                borderPane.setCenter(this.getFinalLabelsBox());
                rateFoodController.setupFoodBoxAfterModify();
            }
        });
    }

    public void setupBackButton(BorderPane borderPane, HBox drinkActionBox, HBox foodActionBox, StartMenuController startMenuController)
    {
        this.undoButton.setOnAction(e -> {
            borderPane.setCenter(null);

            if (startMenuController.getCurrentContext().equals("drinks"))
            {
                borderPane.setLeft(drinkActionBox);
                borderPane.setRight(this.getOrderBox());
            }
            else if (startMenuController.getCurrentContext().equals("foods"))
            {
                borderPane.setLeft(foodActionBox);
                borderPane.setRight(this.getOrderBox());
            }
        });
    }

    public void setupCashButton(BorderPane borderPane)
    {
        this.cashButton.setOnAction(e -> {
            this.paymentType = "Cash";
            this.setupFinalStage();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupCardButton(BorderPane borderPane)
    {
        this.cardButton.setOnAction(e -> {
            this.paymentType = "Card";
            this.setupFinalStage();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupViewOrderButton(BorderPane borderPane)
    {
        this.viewOrderButton.setOnAction(e -> {
            this.setupViewFinalOrder();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getViewFinalOrderBox());
        });
    }

    public void setupModifyPaymentMethod(BorderPane borderPane)
    {
        this.modifyPaymentButton.setOnAction(e -> {
            this.setupPayWithoutBackButton();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getPayBoxWithoutBack());
        });
    }

    public void setupOkButton(BorderPane borderPane)
    {
        this.OkButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupModifyButton(BorderPane borderPane, StartMenuController startMenuController)
    {
        this.modifyButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());

            this.resetOrderBox();
            borderPane.setRight(this.getOrderBox());

            this.isModifyButtonPressed = true;
        });
    }

    public void setupSubmitButton(BorderPane borderPane)
    {
        this.submitButton.setOnAction(e -> {
            this.setupSubmitButtonBox();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getSubmitButtonBox());

            BorderPane.setAlignment(this.getSubmitButtonBox(), Pos.CENTER);
        });
    }

    public void setupBackToMenuButton(BorderPane borderPane, StartMenuController startMenuController, RateFoodController rateFoodController)
    {
        this.backToMenuButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());

            this.resetTotalSum();
            this.clearOrder();
            this.resetOrderBox();
            rateFoodController.resetOrderedFoodList();
            rateFoodController.setRatedFoodButtonPressed(false);
            this.isModifyButtonPressed = false;

            borderPane.setRight(this.getOrderBox());
        });
    }

    public VBox getSubmitButtonBox() {
        return submitButtonBox;
    }

    public VBox getViewFinalOrderBox() {
        return viewFinalOrderBox;
    }

    public HBox getPayBoxWithoutBack() {
        return payBoxWithoutBack;
    }

    public VBox getFinalLabelsBox() {
        return finalLabelsBox;
    }

    public VBox getOrderBox() {
        return orderBox;
    }

    public VBox getFinalPayBox() {
        return finalPayBox;
    }

    public Button getRateFoodButton() {
        return rateFoodButton;
    }
}
