package Admin;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class adminControlloer implements Initializable {
    @FXML
    private TableView<stusentData> studentTable;
    @FXML
    private TableColumn<stusentData, String> idcolumn;

    @FXML
    private TableColumn<stusentData, String> firstnamecolumn;

    @FXML
    private TableColumn<stusentData, String> lastnamecolumn;

    @FXML
    private TableColumn<stusentData, String> emailcolumn;

    @FXML
    private TableColumn<stusentData, String> dobcolumn;

    @FXML
    private TextField searchTxt;

    private dbConnection db;
    private ObservableList<stusentData> data;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.db = new dbConnection();
    }//initialize

    @FXML
    private void loadStudentData(ActionEvent event){
        try {
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            String sql = "select * from student";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                this.data.add(new stusentData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)));
            }
        } //try
        catch (SQLException e) {
            e.printStackTrace();
        }
        //load data into tableView
        this.idcolumn.setCellValueFactory(
                new PropertyValueFactory<stusentData,String>("ID"));
        this.firstnamecolumn.setCellValueFactory(
                new PropertyValueFactory<stusentData,String>("firstName"));
        this.lastnamecolumn.setCellValueFactory(
                new PropertyValueFactory<stusentData,String>("lastName"));
        this.emailcolumn.setCellValueFactory(
                new PropertyValueFactory<stusentData,String>("email"));
        this.dobcolumn.setCellValueFactory(
                new PropertyValueFactory<stusentData,String>("DOB"));

        this.studentTable.setItems(null);
        this.studentTable.setItems(data);

        //Filter Data in TableView
        FilteredList<stusentData> filteredData =
                new FilteredList<>(data, e -> true);
        searchTxt.setOnKeyReleased(e -> {
            searchTxt.textProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        filteredData.setPredicate(stusentData -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();
                            if (stusentData.getID().contains(newValue)) {
                                return true;
                            } else if
                                    (stusentData.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if
                                    (stusentData.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });
                    });
            SortedList<stusentData> sortedData =
                    new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(
                    studentTable.comparatorProperty());
            studentTable.setItems(sortedData);

        });



    }//loadStudentData




}//class