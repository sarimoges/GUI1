package my;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
//import DB.DBConnectionC;
import DBConnectionC.DBConnectionC;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;

public class SARA extends Application {

    Connection con = null;

    @Override
    public void start(Stage stage) throws IOException {
        String s = null;

        Label sidLabel = new Label("SID");
        TextField sidText = new TextField();
        Label stdLabel = new Label("StudId");
        TextField stdText = new TextField();
        Label fnameLabel = new Label("Firstname");
        TextField fnameText = new TextField();
        Label lnameLabel = new Label("Lastname");
        TextField lnameText = new TextField();
        Label secLabel = new Label("Section");
        TextField secText = new TextField();
        Label deptLabel = new Label("Department");
        TextField deptText = new TextField();

        Button Insert = new Button("Insert");
        TableView table = new TableView();

        Insert.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                DBConnectionC db = new DBConnectionC();
                Connection con = null;
                try {
                    con = db.connMethod();
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(SARA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                if (con != null) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("you are connected sucssfuly");
                    a.showAndWait();
                } else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("you are not connected sucssfuly");
                    a.showAndWait();
                }

                try {
                    con = db.connMethod();
                    String id = sidText.getText();
                    String sid = stdText.getText();
                    String fn = fnameText.getText();
                    String ln = lnameText.getText();
                    String sec = secText.getText();
                    String dep = deptText.getText();

                    //Connection conn= DriverManager.getConnection(db.con);
                    String query = "insert into dept_tb2 values('" + id + "','" + sid + "','" + fn + "','" + ln + "','" + sec + "','" + dep + "')";
                    if (id.equals("") || sid.equals("") || fn.equals("") || ln.equals("") || sec.equals("") || dep.equals("")) {
                        JOptionPane.showMessageDialog(null, "every field is required");

                    } else {
                        PreparedStatement ps = con.prepareStatement(query);

                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "inserted successfuly");
                    }

//                    ps.setString(1, sidText.getText());
//                    ps.setString(2, stdText.getText());
//                    ps.setString(3, fnameText.getText());
//                    ps.setString(4, lnameText.getText());
//                    ps.setString(5, secText.getText());
//                    ps.setString(6, deptText.getText());
                    //System.out.println("inserted successfuly");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "insertion failed");
                    //System.out.println(ex.getMessage());
                }

            }
        });

        Button buttonDisplay = new Button("Display");

        Button buttonUpdate = new Button("Update");
        buttonUpdate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Alert A = new Alert(Alert.AlertType.INFORMATION);
                DBConnectionC db = new DBConnectionC();
                Connection con = null;
                try {

                    Scanner input = new Scanner(System.in);
                    System.out.print("Enter an updated name:fom dept_tb2 ");
                    String vall = input.nextLine();
                    System.out.print("Enter an new  name: ");
                    String vall1 = input.nextLine();
                    // closing the scanner object
                    input.close();
                    String sql = "UPDATE dept_tb1 SET fristname='" + vall1 + "' WHERE fristname='" + vall + "'";

                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    A.setContentText("Updated successfuly");
                    A.showAndWait();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        buttonDisplay.setOnAction(new EventHandler<ActionEvent>() {
            private ObservableList<ObservableList> data;
            //private TableView tbl;

            @Override
            public void handle(ActionEvent event) {

                DBConnectionC obj1;
                Connection c;
                ResultSet rs;
                PreparedStatement prs;
                data = FXCollections.observableArrayList();
                try {

                    table.setStyle("-fx-background-color:yellow; -fx-font-color:yellow ");
                    obj1 = new DBConnectionC();
                    c = obj1.connMethod();
                    String SQL = "SELECT * from dept_tb2";
                    // prs = c.prepareStatement(SQL);
                    rs = (ResultSet) c.createStatement().executeQuery(SQL);
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        final int j = i;
                        TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                        col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                        table.getColumns().addAll(col);
                        System.out.println("Column [" + i + "] ");

                    }

                    while (rs.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            row.add(rs.getString(i));
                        }
                        System.out.println("Row[1]added " + row);
                        data.add(row);

                    }

                    table.setItems(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error ");
                }
            }
        });

        GridPane gridpane = new GridPane();

        gridpane.setMinSize(300, 200);

        gridpane.setPadding(new Insets(10, 10, 10, 10));

        gridpane.setVgap(2);
        gridpane.setHgap(2);

        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(sidLabel, 0, 0);
        gridpane.add(sidText, 1, 0);

        gridpane.add(stdLabel, 0, 1);
        gridpane.add(stdText, 1, 1);

        gridpane.add(fnameLabel, 0, 2);
        gridpane.add(fnameText, 1, 2);

        gridpane.add(lnameLabel, 0, 3);
        gridpane.add(lnameText, 1, 3);

        gridpane.add(secLabel, 0, 4);
        gridpane.add(secText, 1, 4);

        gridpane.add(deptLabel, 0, 5);
        gridpane.add(deptText, 1, 5);

        gridpane.add(Insert, 0, 6);
        gridpane.add(buttonDisplay, 0, 7);

        gridpane.add(buttonUpdate, 0, 9);
        gridpane.addColumn(3,
                 table);
        ;

        Insert.setStyle("-fx-background-color:gray; -fx-textfill:white;");
        buttonDisplay.setStyle("-fx-background-color:gray; -fx-textfill:white;");

        buttonUpdate.setStyle("-fx-background-color:gray; -fx-textfill:white;");
        gridpane.setStyle("-fx-background-color:azure;");
        sidLabel.setStyle("-fx-font:normal bold 15px 'serif'");
        stdLabel.setStyle("-fx-font:normal bold 15px 'serif'");
        fnameLabel.setStyle("-fx-font:normal bold 15px 'serif'");
        lnameLabel.setStyle("-fx-font:normal bold 15px 'serif'");
        secLabel.setStyle("-fx-font:normal bold 15px 'serif'");
        deptLabel.setStyle("-fx-font:normal bold 15px 'serif'");

        Scene scene = new Scene(gridpane);

        stage.setTitle("Registration");

        stage.setScene(scene);

        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
