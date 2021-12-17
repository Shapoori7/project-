package controller;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class DataBaseController {

    private ArrayList<User> loadUsersList() {
        File f = new File("src/db/Users.txt");

        try(FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<User>) ois.readObject();
        }
        catch (IOException e) {
            System.err.println("couldn't open the file");
        }
        catch (ClassNotFoundException e) {
            System.err.println("couldn't get the list");
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user: Objects.requireNonNull(loadUsersList())) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (User user: Objects.requireNonNull(loadUsersList())) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void saveUser(User user) {
        ArrayList<User> users = loadUsersList();
        assert users != null;
        users.removeIf(userInDb -> userInDb.equals(user));
        users.add(user);

        File f = new File("src/db/Users.txt");

        try(FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);

        }
        catch (IOException e) {
            System.err.println("couldn't open the file");

        }

    }

    public void dbInitializer() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("test", "pass", "test@gmail.com");
        users.add(user);
        File f = new File("src/db/Users.txt");

        try(FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);

        }
        catch (IOException e) {
            System.err.println("couldn't open the file");

        }

    }

    public static void main(String[] args) {
        DataBaseController dbCtrl = new DataBaseController();
        dbCtrl.dbInitializer();
    }

}
