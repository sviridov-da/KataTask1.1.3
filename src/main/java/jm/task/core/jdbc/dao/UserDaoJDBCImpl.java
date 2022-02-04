package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public void createUsersTable() {
        String query = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS users (\n")
                .append("  `id` BIGINT NOT NULL AUTO_INCREMENT,\n")
                .append("  `name` VARCHAR(64) NULL,\n")
                .append("  `lastname` VARCHAR(128) NULL,\n")
                .append("  `age` TINYINT NULL,\n")
                .append("  PRIMARY KEY (`id`));").toString();

        try (Connection connection=Util.getConnection()){
            connection.setAutoCommit(false);
            try(Statement statement = connection.createStatement()) {
                statement.execute(query);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Не удалось создать таблицу");
        }

    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS users";

        try (Connection connection=Util.getConnection()){
            connection.setAutoCommit(false);
            try(Statement statement = connection.createStatement()) {
                statement.execute(query);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить таблицу");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String preparedQuery = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";

        try (Connection connection=Util.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.execute();
                connection.commit();
                System.out.println("User с именем - " + name + " добавлен в базу данных");
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить таблицу");
        }


    }

    public void removeUserById(long id) {
        String preparedQuery = "DELETE FROM users WHERE id = ?";

        try (Connection connection=Util.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
                statement.setLong(1, id);
                statement.execute();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        User user;

        try(Connection connection = Util.getConnection(); Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);
            ResultSet resultSet =statement.executeQuery(query);
            //connection.commit();
            while (resultSet.next()){
                user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                users.add(user);
            }
        } catch (Exception e) {
            System.out.println("Не удалось получить таблицу User");
        }
        return users;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM users";

        try (Connection connection=Util.getConnection()){
            connection.setAutoCommit(false);
            try(Statement statement = connection.createStatement()) {
                statement.execute(query);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Не удалось очистить таблицу");
        }
    }
}
