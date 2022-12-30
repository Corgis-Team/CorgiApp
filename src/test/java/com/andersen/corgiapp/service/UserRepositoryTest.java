package com.andersen.corgiapp.service;

import com.andersen.corgiapp.connection.DatabaseConnection;
import com.andersen.corgiapp.connection.DatabaseProperties;
import com.andersen.corgiapp.entity.User;
import com.andersen.corgiapp.exception.FileNotValidException;
import com.andersen.corgiapp.exception.ModelNotFoundException;
import com.andersen.corgiapp.repository.UserRepository;
import com.andersen.corgiapp.repository.UserRepositoryImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryTest {

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users\n" +
            "(\n" +
            "    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,\n" +
            "    name    VARCHAR(100)                            NOT NULL,\n" +
            "    surname VARCHAR(100)                            NOT NULL,\n" +
            "    age     INT                                     NOT NULL,\n" +
            "    CONSTRAINT pk_user PRIMARY KEY (id)\n" +
            ");";
    Connection connection;
    UserRepository userRepository;

    @BeforeEach
    public void before() {
        try {
            DatabaseConnection.setDatabaseProperties(new TestDataProperties());
            this.connection = DriverManager.getConnection("jdbc:h2:mem:corgi", "root", "root");
            this.userRepository = new UserRepositoryImpl();
            PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveUser() {
        User user = new User("Peter", "Parker", 18);
        userRepository.save(user);
        long userId = user.getId();
        Assertions.assertEquals(userRepository.get(userId), user);
    }

    @Test
    public void getUser() {
        User user = new User("Peter", "Parker", 18);
        userRepository.save(user);
        long userId = user.getId();
        Assertions.assertNotNull(userRepository.get(userId));
    }

    @Test
    public void deleteUser() {
        User user = new User("Peter", "Parker", 18);
        userRepository.save(user);
        long userId = user.getId();
        userRepository.delete(userId);
        Assertions.assertThrows(ModelNotFoundException.class, () -> userRepository.get(userId));
    }

    @Test
    public void getAll() {
        User user = new User("Peter", "Parker", 18);
        userRepository.save(user);
        List<User> users = userRepository.getAll();
        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void updateUser() {
        User user = new User("Peter", "Parker", 18);
        userRepository.save(user);
        long userId = user.getId();
        User updatedUser = new User(userId, "Steven", "Strange", 40);
        userRepository.update(updatedUser);
        User userDb = userRepository.get(userId);
        Assertions.assertEquals(userDb, updatedUser);
    }

    @AfterEach
    public void after() throws SQLException {
        this.connection.close();
    }

    static class TestDataProperties extends DatabaseProperties {

        public TestDataProperties() {
            super();
        }

        @Override
        public void init() {
            PropertiesConfiguration config = new PropertiesConfiguration();
            try {
                config.load("src/test/resources/test-db.properties");
            } catch (ConfigurationException e) {
                throw new FileNotValidException("Something wrong with db.properties file");
            }
            this.url = config.getString("db.url");
            this.username = config.getString("db.user");
            this.password = config.getString("db.password");
        }
    }
}