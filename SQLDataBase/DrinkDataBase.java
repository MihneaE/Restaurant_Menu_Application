package SQLDataBase;

import Model.Drink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDataBase {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DrinkDataBase() throws SQLException {
        this.connection = null;
        this.statement = null;
        this.resultSet = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:drinks.db");
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " " + e.getMessage());
        }

        System.out.println("Connected to: " + connection.getMetaData().getURL());
    }

    public Connection connect()
    {
        String url = new String("jdbc:sqlite:drinks.db");
        Connection conn = null;

        try
        {
            conn = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return conn;
    }

    public void addDrinkToDataBase(String table, Drink drink)
    {
        String sql = "INSERT INTO " + table + "(id, name, volume, price, quantity) VALUES(?, ?, ?, ?, ?)";

        try
        {
            PreparedStatement preparedStatement = this.connect().prepareStatement(sql);

            preparedStatement.setInt(1, drink.getId());
            preparedStatement.setString(2, drink.getName());
            preparedStatement.setDouble(3, drink.getVolume());
            preparedStatement.setInt(4, drink.getPrice());
            preparedStatement.setInt(5, drink.getQuantity());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (this.connect() != null)
                this.connect().close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteDrinkFromDataBase(String table, String name)
    {
        String sql = "DELETE FROM " + table + " WHERE name = ? ";

        try(PreparedStatement preparedStatement = this.connect().prepareStatement(sql);)
        {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (this.connect() != null)
                this.connect().close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateDrinkFromDataBase(String table, String name, Drink drink)
    {
        String sql = "UPDATE " + table + " SET id = ?, name = ?, volume = ?, price = ?, quantity = ? WHERE name = ?";

        try(PreparedStatement preparedStatement = this.connect().prepareStatement(sql))
        {
            preparedStatement.setInt(1, drink.getId());
            preparedStatement.setString(2, drink.getName());
            preparedStatement.setDouble(3, drink.getVolume());
            preparedStatement.setInt(4, drink.getPrice());
            preparedStatement.setInt(5, drink.getQuantity());
            preparedStatement.setString(6, name);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (this.connect() != null)
                this.connect().close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Drink> loadDrinks(String table)
    {
        List<Drink> drinks = new ArrayList<>();

        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + table);

            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                float volume = resultSet.getFloat("volume");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");

                Drink drink = new Drink(id, name, volume, price, quantity);
                drinks.add(drink);
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + " " + e.getMessage());
            System.exit(0);
        }

        return drinks;
    }

    public void close()
    {
        try
        {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
