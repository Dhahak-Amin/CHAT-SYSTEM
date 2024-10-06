package fr.insa.maven.demo.demoMavenProject;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();

    public void registerUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }
}