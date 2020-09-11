package com.anavidev.meslistes.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {
    private static DB instance = new DB();
    private ObservableList<TodoList> myLists;
    private String url = "jdbc:mysql://localhost:3306/meslistes?serverTimezone=UTC";
    private Connection myConnection = null;

    //Public method to get instance of this singleton
    public static DB getInstance() {
        return instance;
    }


    //Make the constructor private to ensure that there is only one instance of this class
    private DB() {

        try{
            myConnection = DriverManager.getConnection(url,"root","123456");
            System.out.println("ok");


        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ObservableList<TodoList> getMyLists() {

        //temporary storage for lists without items
        ObservableList<TodoList> tempMyLists = FXCollections.observableArrayList();

        int tempListId = 0;

        if (myConnection != null) {
            try {
                Statement stm = myConnection.createStatement();
                ResultSet resultSet = stm.executeQuery("SELECT * FROM meslistes.item JOIN meslistes.list on item.list_id=list.id ORDER BY item.list_id");

                while (resultSet.next()) {

                    int id = resultSet.getInt("list.id");

                    if (tempMyLists == null || id > tempListId){
                        tempListId = id;

                        String name = resultSet.getNString("list.name");
                        boolean done = resultSet.getBoolean("list.done");
                        boolean important = resultSet.getBoolean("list.important");
                        String iconName = resultSet.getNString("icon_name");

                        TodoList newList = new TodoList(id, name, done, important, iconName, FXCollections.observableArrayList());
                        tempMyLists.add(newList);
                    }

                    int itemId = resultSet.getInt("item.id");
                    String title = resultSet.getNString("item.title");
                    boolean itemDone = resultSet.getBoolean("item.done");
                    boolean itemImportant = resultSet.getBoolean("item.important");
                    boolean hasImage = resultSet.getBoolean("item.has_image");
                    String imageName = resultSet.getNString("item.image_name");
                    int listId = resultSet.getInt("item.list_id");
                    Item newItem = new Item(itemId, title, itemDone, itemImportant, hasImage, imageName, listId);

                    //myItems.add(newItem);

                    tempMyLists.forEach(todoList -> {
                        if (newItem.getListId() == todoList.getId()){
                            todoList.appendItem(newItem);
                        }
                    });

                }
              this.myLists = tempMyLists;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        return this.myLists;
    }
}
