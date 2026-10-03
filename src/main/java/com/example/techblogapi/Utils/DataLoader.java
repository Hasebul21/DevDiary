package com.example.techblogapi.Utils;

import com.example.techblogapi.entity.Comments;
import com.example.techblogapi.entity.Likes;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Tags;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.repository.CommentRepository;
import com.example.techblogapi.repository.LikeRepository;
import com.example.techblogapi.repository.StoryRepository;
import com.example.techblogapi.repository.TagRepository;
import com.example.techblogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// runs one time when the app starts
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${adminEmail}")
    private String adminEmail;

    @Value("${adminPassword}")
    private String adminPassword;

    @Value("${adminName}")
    private String adminName;

    @Value("${seedData}")
    private boolean seedData;

    private static final String DEMO_PASSWORD = "Demo1234";

    @Override
    public void run(String... args) {

        Users admin=createAdmin();
        if(seedData && storyRepository.count()==0){
            createDummyData(admin);
        }
    }

    // admin email and password come from environment variable
    // if admin already exists then make sure the password and role is same as environment variable
    private Users createAdmin() {

        if(adminEmail==null || adminEmail.trim().isEmpty() || adminPassword==null || adminPassword.isEmpty()){
            System.out.println("ADMIN_EMAIL or ADMIN_PASSWORD is not set, so admin is not created");
            return null;
        }

        String email=adminEmail.trim().toLowerCase();
        Optional<Users> oldAdmin=userRepository.findByEmail(email);
        Users admin;
        if(oldAdmin.isPresent()){
            admin=oldAdmin.get();
        }
        else{
            admin=new Users();
            admin.setEmail(email);
            admin.setName(adminName);
            admin.setPhone("01700000000");
        }
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");
        userRepository.save(admin);
        System.out.println("Admin is ready: "+email);
        return admin;
    }

    private void createDummyData(Users admin) {

        Users rahim=createUser("rahim.uddin@demo.com","Rahim Uddin","01711111111");
        Users nusrat=createUser("nusrat.jahan@demo.com","Nusrat Jahan","01722222222");
        Users tanvir=createUser("tanvir.ahmed@demo.com","Tanvir Ahmed","01733333333");
        // parody users, just for fun
        Users tusk=createUser("elon.tusk@demo.com","Elon Tusk","01744444444");
        Users zuckerbot=createUser("mark.zuckerbot@demo.com","Mark Zuckerbot","01755555555");
        if(admin==null) admin=rahim;

        Storys s1=createStory(admin,"Welcome to Tech World",
                "Hello everyone! This is a small blog where we share what we learn about programming. "
                        + "You can write your own story, add tags, comment on other stories and like the ones you enjoy. "
                        + "Please be nice to each other and keep learning.",
                new String[]{"announcement","community"},12);

        Storys s2=createStory(rahim,"Getting started with Spring Boot",
                "Spring Boot makes it very easy to create a REST API in Java. "
                        + "Go to start.spring.io, choose Spring Web, Spring Data JPA and MySQL Driver, then download the project. "
                        + "Create an entity class, a repository interface that extends JpaRepository and a controller with @RestController. "
                        + "Run the main class and your API is ready on port 8080. "
                        + "In my next story I will write about how to add JWT authentication.",
                new String[]{"java","spring"},10);

        Storys s3=createStory(nusrat,"React hooks that I use every day",
                "When I started React I was confused about hooks. Now I use these every day:\n\n"
                        + "useState - to keep a value inside a component.\n"
                        + "useEffect - to call an API when the page loads.\n"
                        + "useContext - to share login state between pages.\n\n"
                        + "Always remember to add the dependency array in useEffect, otherwise it will run after every render.",
                new String[]{"react","javascript"},9);

        Storys s4=createStory(tanvir,"How JWT authentication works",
                "JWT means JSON Web Token. After login the server creates a token and signs it with a secret key. "
                        + "The client saves the token and sends it in the Authorization header like this: Bearer <token>. "
                        + "The server checks the signature and the expiry time on every request, so it does not need to keep a session. "
                        + "Never put a password inside a JWT because anyone can decode the payload.",
                new String[]{"security","java","spring"},7);

        Storys s5=createStory(rahim,"MySQL tips for beginners",
                "1. Always add a primary key to your table.\n"
                        + "2. Add an index to the column you search the most.\n"
                        + "3. Use LIMIT when you only need a few rows.\n"
                        + "4. Do not store passwords in plain text, store a hash.\n"
                        + "5. Take a backup before running a big update query.",
                new String[]{"database","mysql"},6);

        Storys s6=createStory(nusrat,"Git commands I use daily",
                "git status - see what changed.\n"
                        + "git checkout -b feature/my-feature - create a new branch.\n"
                        + "git add file.txt - stage a file.\n"
                        + "git commit -m \"message\" - save the change.\n"
                        + "git merge feature/my-feature - merge the branch.\n\n"
                        + "Make small commits with clear messages, your team will thank you.",
                new String[]{"git","tools"},4);

        Storys s7=createStory(tanvir,"Deploying a Spring Boot app with Docker",
                "Docker lets you run your app the same way on every machine. "
                        + "Write a Dockerfile with two stages: the first stage builds the jar with Gradle, "
                        + "the second stage copies the jar into a small Java 17 image and runs it. "
                        + "Then you can deploy the image to any cloud that supports Docker.",
                new String[]{"docker","spring","devops"},2);

        Storys s8=createStory(admin,"Clean code in 5 simple rules",
                "1. Give good names to variables and methods.\n"
                        + "2. Keep methods small, one method should do one thing.\n"
                        + "3. Do not repeat the same code again and again.\n"
                        + "4. Write tests for important logic.\n"
                        + "5. Delete code that nobody uses.",
                new String[]{"clean-code","community"},1);

        Storys s9=createStory(tusk,"I will send this blog to Mars",
                "Big announcement: this blog will be the first blog on Mars. "
                        + "The servers are already packed in a rocket, we only need to fix one small bug before launch. "
                        + "The bug is that the rocket is a JavaScript file. "
                        + "Anyway, launch date is next week. Or next year. Definitely soon.",
                new String[]{"space","fun"},3);

        Storys s10=createStory(zuckerbot,"Beep boop, my first blog post as a human",
                "Hello fellow humans. I am a normal human who enjoys normal human things like drinking water and sitting. "
                        + "Today I learned Spring Boot. It was very relatable. "
                        + "Please like this story so that I can collect more data about what humans enjoy. Beep boop.",
                new String[]{"fun","community"},1);

        createComment(rahim,s1,"Thanks for creating this blog!");
        createComment(nusrat,s1,"Happy to be here.");
        createComment(nusrat,s2,"Very helpful for beginners, waiting for the JWT part.");
        createComment(tanvir,s2,"You can also use Spring Initializr from IntelliJ.");
        createComment(rahim,s3,"The dependency array tip saved me a lot of time.");
        createComment(admin,s4,"Good explanation. Short expiry time is also important.");
        createComment(nusrat,s4,"Now I finally understand the Bearer header.");
        createComment(tanvir,s5,"Number 4 is the most important one!");
        createComment(rahim,s6,"I also use git log --oneline a lot.");
        createComment(nusrat,s7,"Can you write about docker compose next?");
        createComment(rahim,s8,"Rule number 1 is the hardest for me.");

        createComment(zuckerbot,s9,"Mars has no users yet. I will build a social network there first.");
        createComment(tusk,s9,"Deal. Loser has to rewrite everything in COBOL.");
        createComment(nusrat,s9,"Please fix the JavaScript rocket first.");
        createComment(tusk,s10,"Nice try, robot.");
        createComment(zuckerbot,s10,"I am not a robot. I clicked all the traffic lights.");
        createComment(tanvir,s10,"Best post on this blog.");
        createComment(tusk,s2,"Spring Boot is good, but have you tried rockets?");

        createLike(zuckerbot,s9);
        createLike(rahim,s9);
        createLike(tanvir,s9);
        createLike(tusk,s10);
        createLike(nusrat,s10);
        createLike(rahim,s10);
        createLike(admin,s10);
        createLike(rahim,s1);
        createLike(nusrat,s1);
        createLike(tanvir,s1);
        createLike(nusrat,s2);
        createLike(tanvir,s2);
        createLike(admin,s2);
        createLike(rahim,s3);
        createLike(tanvir,s3);
        createLike(rahim,s4);
        createLike(nusrat,s4);
        createLike(admin,s4);
        createLike(nusrat,s5);
        createLike(tanvir,s6);
        createLike(rahim,s7);
        createLike(nusrat,s8);
        createLike(tanvir,s8);

        System.out.println("Dummy data is created");
    }

    private Users createUser(String email, String name, String phone) {

        Optional<Users> oldUser=userRepository.findByEmail(email);
        if(oldUser.isPresent()) return oldUser.get();

        Users user=new Users();
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
        user.setRole("USER");
        return userRepository.save(user);
    }

    private Storys createStory(Users author, String title, String description, String[] tagNames, int daysAgo) {

        Storys story=new Storys(title,description);
        story.setAuthorid(author);
        story.setCreatedDate(new Date(System.currentTimeMillis()-daysAgo*24L*60*60*1000));

        List<Tags> tags=new ArrayList<>();
        for(String name : tagNames){
            Optional<Tags> oldTag=tagRepository.findByName(name);
            if(oldTag.isPresent()) tags.add(oldTag.get());
            else tags.add(tagRepository.save(new Tags(name)));
        }
        story.setTags(tags);
        return storyRepository.save(story);
    }

    private void createComment(Users user, Storys story, String text) {

        Comments comment=new Comments();
        comment.setText(text);
        comment.setUser(user);
        comment.setStory(story);
        commentRepository.save(comment);
    }

    private void createLike(Users user, Storys story) {

        likeRepository.save(new Likes(user,story));
    }
}
