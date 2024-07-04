package org.example;

import java.util.regex.Pattern;

public class UserService {
    private User loggedInUser;

    public boolean register(String username, String email, String password) {
        if (isValidEmail(email)) {
            loggedInUser = new User(username, email, password);
            System.out.println("Registration successful.");
            return true;
        } else {
            System.out.println("Invalid email address.");
            return false;
        }
    }

    public boolean login(String username, String password) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(username) && loggedInUser.getPassword().equals(password)) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
