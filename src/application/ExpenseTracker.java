package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

 
//This code demonstrates polymorphism by returning an ObservableList of type PieChart.Data from the getPie() method.

public class ExpenseTracker extends Application {
 
 TableView<ExpenseTable> tableview;
 TableView<ExpenseCategory> tableview1;
 String semaphore = "";
 String database = "jdbc:mysql://localhost/ExpenseTracker?useUnicode=true&serverTimezone=UTC";
 String username = "root";
 String password = "";
 public String Formate(double value) {
     DecimalFormat myformat = new DecimalFormat("$###,###.0##");
     String Output = myformat.format(value);
     return Output;
 }
 public ObservableList<PieChart.Data> getPie(){
     ObservableList<PieChart.Data> PiechartData = FXCollections.observableArrayList();
     try{
         Connection conn = DriverManager.getConnection(database,username,password);
         Statement stt = conn.createStatement();
         String query = "SELECT DISTINCT Category FROM tdtb";
         ResultSet res = stt.executeQuery(query);
         String arr[] = new String[12];
         for(int x =0;x<12;x++) arr[x] = null;
         int i =0;
         while(res.next()) {
             arr[i] = res.getString("Category");
             i++;
         }
         res.close();
         for(int y=0;y<i;y++) {
             if(arr[y].equals("Salary")) continue;
             String Query = "SELECT SUM(amount) AS 'sum' FROM tdtb where Category = '"+arr[y]+"'";
             ResultSet res1 = stt.executeQuery(Query);
             res1.next();
             double price = res1.getDouble("sum");
             PiechartData.add(new PieChart.Data(arr[y],price));
             res1.close();
         }
         i=0;
     } catch(SQLException f){
         System.out.print("Not Connected to the Database - Error:"+f);
     }
     return PiechartData;
     
 }

// This Java code retrieves ordered data from a database table and stores it in an ObservableList of ExpenseTable objects.    
    public ObservableList<ExpenseTable> getExpense(){
    	ObservableList<ExpenseTable> Expense = FXCollections.observableArrayList();
    	try{
			Connection conn = DriverManager.getConnection(database,username,password);
			Statement stt = conn.createStatement();
			String query = "SELECT * FROM tdtb ORDER BY date DESC";
			ResultSet res = stt.executeQuery(query);
			while(res.next()) {
					Expense.add(new ExpenseTable(res.getString("Category"), Formate(res.getDouble("amount")),res.getDate("date").toString()));
				}
			conn.close();
		} catch(SQLException f){
			System.out.print("Not Connected to the Database - Error:"+f);
			}
		return Expense;
    }
// List Over 

// ObservableList of ExpenseCategory objects retrieved the expense information from a database table and to display the content in a UI.
    public ObservableList<ExpenseCategory> getCategory(){
    	ObservableList<ExpenseCategory> Category = FXCollections.observableArrayList();
    	try{
			Connection conn = DriverManager.getConnection(database,username,password);
			Statement stt = conn.createStatement();
			String query = "SELECT DISTINCT Category FROM tdtb";
			ResultSet res = stt.executeQuery(query);
			String arr[] = new String[12];
			for(int x =0;x<12;x++) arr[x] = null;
			double Total = 0;
			int i =0;
			while(res.next()) {
				arr[i] = res.getString("Category");
				i++;
			}
			res.close();
			for(int y=0;y<i;y++) {
				if(arr[y].equals("Salary")) continue;
				String Query = "SELECT SUM(amount) AS 'sum' FROM tdtb where Category = '"+arr[y]+"'";
				ResultSet res1 = stt.executeQuery(Query);
				res1.next();
				double price = res1.getDouble("sum");
				Category.add(new ExpenseCategory(arr[y],Formate(price)));
				Total += price;
				res1.close();
			}
			i=0;
			query = "SELECT SUM(amount) AS 'sum' FROM tdtb where Category = 'Salary'";
			ResultSet res2 = stt.executeQuery(query);
			res2.next();
			double salary = 0;
			salary = res2.getDouble("sum");
			Category.add(new ExpenseCategory("Complete Expense  ",Formate(Total)));
			Category.add(new ExpenseCategory("Balance",Formate(salary)));
			Category.add(new ExpenseCategory("Complete Savings  ",Formate(salary-Total)));
			Total = 0;
			conn.close();
		} catch(SQLException f){
			System.out.print("Not Connected to the Database - Error:"+f);
			}
		return Category;
    }
 // List Over <<<<<<<<<<<<<<
	public void start(Stage basestage) throws Exception {

		Scene Basescene,ExpenseType,IncomeType,Entryscene;
		
//  THIS  is --> Basescene	<-- 
		
		
	//Creating a TabPane and tabs with icons for Spending, Transaction, and Graph.
		Image[] Tabimage = {new Image("Spending.png"),new Image("Transaction.png"),new Image("Graph.png")};
		ImageView[] Tabview = {new ImageView(Tabimage[0]),new ImageView(Tabimage[1]),new ImageView(Tabimage[2])}; 
		
		for(int x=0;x<3;x++) {
			Tabview[x].setFitHeight(25);
			Tabview[x].setFitWidth(25);
		}
		
		TabPane tabpane = new TabPane();
		tabpane.setTabMinWidth(112);
		tabpane.setTabMaxHeight(100);
		tabpane.getStyleClass().add("tab-pane");
		
		Tab[] tab = {new Tab("Spending"),new Tab("Transaction"),new Tab("Graph")};
		for(int x=0;x<3;x++) {
			tab[x].setClosable(false);
			tab[x].setGraphic(Tabview[x]);
			tab[x].getStyleClass().add("tab-sett");
		}
		tabpane.getTabs().addAll(tab[0],tab[1],tab[2]);
	// TABS set
		
		Insets RBinset = new Insets(22,60,20,0);
		Insets LBinset = new Insets(22,0,20,60);
		
		Button Expense = new Button("+ Expense");
		Expense.getStyleClass().add("Basescene-button");
		Expense.requestFocus();
		Button Income = new Button("+ Income");
		Income.getStyleClass().add("Basescene-button");
		

		
	// TABLE VIEW 1 with two columns for displaying expense categories and amounts
		tableview1 = new TableView<>();
		TableViewSelectionModel<ExpenseCategory> selectionmodel = tableview1.getSelectionModel();
		TableColumn<ExpenseCategory,String> column3 = new TableColumn<>();
		column3.setCellValueFactory(new PropertyValueFactory<>("category"));
		column3.setSortable(false);
		column3.setMinWidth(199);
		TableColumn<ExpenseCategory,String> column4 = new TableColumn<>();
		column4.setCellValueFactory(new PropertyValueFactory<>("amount"));
		column4.setSortable(false);
		column4.setMinWidth(199);
		
		tableview1.setItems(getCategory());
		tableview1.getColumns().add(column3);
		tableview1.getColumns().add(column4);
		tableview1.setMaxHeight(340);
		tableview1.setMaxWidth(400);
		tableview1.getStyleClass().add("table-vie");
		selectionmodel.clearSelection();
		VBox table_container1 = new VBox(tableview1);
		table_container1.getStylesheets().add("tableview1.css");
		table_container1.setStyle("-fx-background-color:#353839");
	// END TABLE VIEW 1	
		BorderPane gridpane = new BorderPane();
		gridpane.setStyle("-fx-background-color: #ece4db");
		
		gridpane.setRight(Expense);
		BorderPane.setAlignment(Expense,Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(Expense,RBinset);
		gridpane.setLeft(Income);
		BorderPane.setAlignment(Income,Pos.BOTTOM_LEFT);
		BorderPane.setMargin(Income,LBinset);
		
		VBox Spending_pane = new VBox();
		Spending_pane.getChildren().addAll(table_container1,gridpane);
		Spending_pane.setStyle("-fx-background-color:#353839");
		
		Insets deleteset = new Insets(0,0,0,335);
		Button Delete = new Button("Delete");
		Delete.getStyleClass().add("Basescene-button");
		PieChart Piechart = new PieChart();
		
// 		TABLE VIEW with columns and sets its items, style, and container
		tableview = new TableView<>();
		TableViewSelectionModel<ExpenseTable> selectionmodel1 = tableview.getSelectionModel();
		tableview.setPlaceholder(new Label("NO Transaction Has Been Completed"));
		TableColumn<ExpenseTable,String> column1 = new TableColumn<>("");
		column1.setCellValueFactory(new PropertyValueFactory<>("first"));
		column1.setSortable(false);
		column1.setMinWidth(130);
		TableColumn<ExpenseTable,String> column2 = new TableColumn<>("");
		column2.setCellValueFactory(new PropertyValueFactory<>("sec"));
		column2.setSortable(false);
		column2.setMinWidth(138);
		TableColumn<ExpenseTable,String> column5 = new TableColumn<>("");
		column5.setCellValueFactory(new PropertyValueFactory<>("date"));
		column5.setSortable(false);
		column5.setMinWidth(130);
		
		tableview.setItems(getExpense());
		tableview.getColumns().add(column1);
		tableview.getColumns().add(column5);
		tableview.getColumns().add(column2);
		tableview.setMinHeight(350);
		tableview.getStyleClass().add("table-view");
		VBox table_container = new VBox();
		VBox.setMargin(Delete, deleteset);
		table_container.getChildren().addAll(tableview,Delete);
		
		table_container.getStylesheets().add("tableview.css");
		table_container.setStyle("-fx-background-color:#ece4db");
	// TABLE VIEW OVER
		
		Delete.setOnAction(e -> {
			ExpenseTable select = selectionmodel1.getSelectedItem();
			if(select != null) {
			String deleteamount = select.getSec().toString();
			double deleted = Double.parseDouble(deleteamount.replaceAll("[,$]*",""));
			try{
				Connection conn = DriverManager.getConnection(database,username,password);
				Statement stt = conn.createStatement();
				String query = "DELETE FROM tdtb where date = '"+select.getDate()+"' AND"+" Category = '"+select.getFirst()+"' AND"+" amount = '"+deleted+"'";
				int p = stt.executeUpdate(query);
				System.out.println(p+"row affected");
				conn.close();
				tableview1.setItems(getCategory());
				tableview.setItems(getExpense());
				Piechart.setData(getPie());
			} catch(SQLException fx){
				System.out.print("Not Connected to the Database - Error:"+fx);
				}
			}
		});
		selectionmodel1.clearSelection();
		
		
	// PIE CHART
		
		Piechart.setData(getPie());
		Piechart.getStyleClass().add("Pie-chart");
		Piechart.getStylesheets().add("Pie.css");
		Piechart.setTitle("Spending Chart");
		Piechart.setLabelLineLength(10);
	
		Pane PiePane = new Pane();
		PiePane.getChildren().add(Piechart);
	
		PiePane.setStyle("-fx-background-color:#ffffff");
	// PIE CHART OVER
		
		tab[0].setContent(Spending_pane);
		tab[1].setContent(table_container);
		tab[2].setContent(PiePane);
		tabpane.setStyle("-fx-cursor:default");
		Basescene = new Scene(tabpane,400,450);
		Basescene.getStylesheets().add("JavaFx.css");
		
		basestage.setTitle("Expense Tracker");
		basestage.setScene(Basescene);
		
//	Basescene Over 
// Creating buttons, button container, and entry pane with style properties
		Button Backbutton = new Button("  Back");
		Backbutton.setPrefHeight(43);
		Backbutton.getStyleClass().add("back-button");
		Backbutton.setOnAction(e -> {
			basestage.setScene(Basescene);
		});
		Button Done = new Button("Done");
		Done.setPrefHeight(43);
		Done.getStyleClass().add("back-button");

		
		Insets doneset = new Insets(0,0,0,277);
		HBox button_container = new HBox();
		HBox.setMargin(Done,doneset);
		button_container.getChildren().addAll(Backbutton,Done);
		button_container.setStyle("-fx-cursor:default;-fx-background-color: #faedcd");
		button_container.getStylesheets().add("JavaFx.css");
		
		GridPane entrypane = new GridPane();
		entrypane.setStyle("-fx-cursor:default;-fx-background-color: #faedcd");
		entrypane.setHgap(10);
		entrypane.setVgap(10);
		entrypane.setAlignment(Pos.CENTER);
		
		
//	DatePicker
		Label Date = new Label("Date         :");
		Date.getStyleClass().add("label-entry");
		DatePicker datepicker = new DatePicker();
		datepicker.setConverter(new StringConverter<LocalDate>() {
		     String pattern = "dd-MM-yyyy";
		     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

		     {datepicker.setPromptText(pattern.toLowerCase());}
		     @Override public String toString(LocalDate date) {
		         if (date != null) { return dateFormatter.format(date);
		         } else {
		             return "";}
		     }
		     @Override public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;}
		     }
		 });
		datepicker.setValue(LocalDate.now());
		entrypane.add(Date, 0, 3);
		entrypane.add(datepicker, 1, 3);
//	DatePcker End
		
	// Error Message
		Label Error = new Label();
		Error.setStyle("-fx-alignment:CENTER;-fx-text-fill:Red;");
	// Error END
		
// 		Creating UI elements and handling actions for adding new expenses to a database	
		Label Category = new Label("Category  :");
		Category.getStyleClass().add("label-entry");
		TextField textfield = new TextField();
		entrypane.add(Category, 0, 4);
		entrypane.add(textfield, 1, 4);
		
		Label Amount = new Label("Amount    :");
		Amount.getStyleClass().add("label-entry");
		TextField Amountfield = new TextField();
		entrypane.add(Amount, 0, 5);
		entrypane.add(Amountfield, 1, 5);
		Backbutton.setOnAction(e -> {
			basestage.setScene(Basescene);
			textfield.clear();
			Amountfield.clear();
		});
		Done.setOnAction(e->{
			LocalDate b = datepicker.getValue();
			String s = textfield.getText();
			String ddd = Amountfield.getText();
			double D = 0;
			if(!ddd.isEmpty())D = Double.parseDouble(ddd);
			
			if(!s.isEmpty() && !ddd.isEmpty()){
			try{
				Connection conn = DriverManager.getConnection(database,username,password);
				PreparedStatement pre = conn.prepareStatement("insert into tdtb values(?,?,?)");
				pre.setString(1, s);
				pre.setDate(2, java.sql.Date.valueOf(b));
				pre.setDouble(3, D);
				int o = pre.executeUpdate();
				System.out.println(o+"row affected");
			
				conn.close();
				tableview1.setItems(getCategory());
				tableview.setItems(getExpense());
				Piechart.setData(getPie());
			} catch(SQLException ex){
				System.out.print("Not Connected to the Database - Error:"+ex);
				}
			textfield.clear();
			Amountfield.clear();
			basestage.setScene(Basescene);
		  } else {
			  if(s.isEmpty())Error.setText("Input Category Here");
			  if(ddd.isEmpty())Error.setText("Please Insert Amount");
			  if(s.isEmpty() && ddd.isEmpty())Error.setText("Please Insert Amount & Select Cetegory");
			}
		});
		
		Label TDetail = new Label("  Transaction Information  ");
		TDetail.setMinWidth(400);
		TDetail.getStyleClass().add("transaction-label");
		
		Text TText = new Text();
		TText.setText("These details are required to be entered:"
				+ "\n\nDate:\nTransaction Date."
				+ "\n\nCategory:\nWhich Category the Transaction Belongs To "
				+ "into e.g. Travel.\n\nAmount:\nThe sum of money.");
		TText.setFont(new Font(14));
		TText.setFill(Color.BLACK);
		TText.setFontSmoothingType(FontSmoothingType.LCD);
		VBox Text_container = new VBox(TText);
		Text_container.setStyle("-fx-background-color:	#7a6889;-fx-padding:10px;-fx-border-color:rgb(252,223,169);-fx-border-width:5px;-fx-border-radius:20px");
		
		VBox entry_pane = new VBox();
		entry_pane.setStyle("-fx-background-color: #faedcd");
		Insets inset = new Insets(20,5,20,5);
		VBox.setMargin(Text_container,inset);
		
		entry_pane.getChildren().addAll(button_container,TDetail,entrypane,Error,Text_container);
		Entryscene = new Scene(entry_pane,400,450);
		Entryscene.getStylesheets().add("JavaFx.css");
		
//	THIS IS --> ExpenseType <-- 
		Button backbutton = new Button("  <-");
		backbutton.setPrefWidth(400);
		backbutton.getStyleClass().add("back-button");
		backbutton.setPrefHeight(43);
		backbutton.setOnAction(e -> {
			basestage.setScene(Entryscene);
		});
		
		Image[] Fuelimage = {new Image("Fuel.png"),new Image("Gifts.png")
							,new Image("Shopping.png"),new Image("Clothes.png")
							,new Image("Eating Out.png"),new Image("Entertainment.png")
							,new Image("General.png"),new Image("Holidays.png")
							,new Image("Kids.png"),new Image("Sports.png")
							,new Image("Travel.png")};
		
		ImageView[] imageview = new ImageView[11];
		for(int x=0;x<11;x++) {
			imageview[x] = new ImageView(Fuelimage[x]);
			imageview[x].setFitHeight(25);
			imageview[x].setFitWidth(25);
		}
		imageview[0].setFitHeight(23);
		imageview[0].setFitWidth(23);
		imageview[10].setFitHeight(30);
		imageview[10].setFitWidth(30);
		Button[] ExpenseCategory = {new Button("  Gas",imageview[0]),new Button("  Gifts",imageview[1])
									,new Button("  Shopping",imageview[2]),new Button("  Clothes",imageview[3])
									,new Button("  Dinner",imageview[4]),new Button("  Fun",imageview[5])
									,new Button("  General",imageview[6]),new Button("  Holidays",imageview[7])
									,new Button("  Children",imageview[8]),new Button("  Sports",imageview[9])
									,new Button(" Travel",imageview[10])};
		ExpenseCategory[0].setStyle("-fx-padding:2px 10px 2px 12px");
		for(Button Ex:ExpenseCategory) {
			Ex.setPrefWidth(400);
			Ex.setAlignment(Pos.BASELINE_LEFT);
			Ex.getStyleClass().add("Expensescene-button");
			// 11 buttons
		}
		VBox Expensetype = new VBox();
		Expensetype.setStyle("-fx-background-color:#f4f7f5;-fx-cursor:default");
		Expensetype.getChildren().add(backbutton);
		for(Button Ex:ExpenseCategory) {
			Expensetype.getChildren().add(Ex);
		}
		
		ExpenseType = new Scene(Expensetype,400,450);
		ExpenseType.getStylesheets().add("JavaFx.css");
		
		// Expense Button event open ExspenseType
		Expense.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				semaphore = "Expense";
				basestage.setScene(Entryscene);
				System.out.println("Expense category");	
			}
		});
		
		// ALL BUTTON EVENT
		ExpenseCategory[0].setOnAction(e->{textfield.setText("Gas");basestage.setScene(Entryscene);});
		ExpenseCategory[1].setOnAction(e->{textfield.setText("Gifts");basestage.setScene(Entryscene);});
		ExpenseCategory[2].setOnAction(e->{textfield.setText("Shopping");basestage.setScene(Entryscene);});
		ExpenseCategory[3].setOnAction(e->{textfield.setText("Clothes");basestage.setScene(Entryscene);});
		ExpenseCategory[4].setOnAction(e->{textfield.setText("Dinner");basestage.setScene(Entryscene);});
		ExpenseCategory[5].setOnAction(e->{textfield.setText("Entertainment");basestage.setScene(Entryscene);});
		ExpenseCategory[6].setOnAction(e->{textfield.setText("General");basestage.setScene(Entryscene);});
		ExpenseCategory[7].setOnAction(e->{textfield.setText("Holidays");basestage.setScene(Entryscene);});
		ExpenseCategory[8].setOnAction(e->{textfield.setText("Kids");basestage.setScene(Entryscene);});
		ExpenseCategory[9].setOnAction(e->{textfield.setText("Sports");basestage.setScene(Entryscene);});
		ExpenseCategory[10].setOnAction(e->{textfield.setText("Travel");basestage.setScene(Entryscene);});

		// BUTTON EVENT OVER
//	ExpenseType Over	
//	THIS IS --> IncomeType <-- 
		Button bacKbutton = new Button("  <-");
		bacKbutton.setPrefWidth(400);
		bacKbutton.setPrefHeight(43);
		bacKbutton.getStyleClass().add("back-button");
		bacKbutton.setOnAction(e -> {
			basestage.setScene(Entryscene);
		});
		
		Button Salary = new Button("Salary");
		Salary.setAlignment(Pos.BASELINE_LEFT);
		Salary.setPrefWidth(400);
		Salary.getStyleClass().add("Expensescene-button");
		
		VBox Incometype = new VBox();
		Incometype.getChildren().addAll(bacKbutton,Salary);
		Incometype.setStyle("-fx-background-color:#f4f7f5;-fx-cursor:default");
		
		IncomeType = new Scene(Incometype,400,450);
		IncomeType.getStylesheets().add("JavaFx.css");
		// Income Button event open IncomeType
		
		Income.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				semaphore = "Income";
				basestage.setScene(Entryscene);
				System.out.println("Income category");
			}
		});
		Salary.setOnAction(e->{textfield.setText("Salary");basestage.setScene(Entryscene);});
//		IncomeType Over  
		textfield.setOnMouseClicked(e ->{
			if(semaphore.equals("Expense"))basestage.setScene(ExpenseType);
			if(semaphore.equals("Income"))basestage.setScene(IncomeType);
		});
		basestage.setResizable(false);
		basestage.show();
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
