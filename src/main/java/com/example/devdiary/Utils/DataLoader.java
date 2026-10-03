package com.example.devdiary.Utils;

import com.example.devdiary.entity.Comments;
import com.example.devdiary.entity.Likes;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Tags;
import com.example.devdiary.entity.Users;
import com.example.devdiary.repository.CommentRepository;
import com.example.devdiary.repository.LikeRepository;
import com.example.devdiary.repository.StoryRepository;
import com.example.devdiary.repository.TagRepository;
import com.example.devdiary.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private static final String DEMO_PASSWORD = "Demo1234";
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${adminEmail}")
    private String adminEmail;

    @Value("${adminPassword}")
    private String adminPassword;

    @Value("${adminName}")
    private String adminName;

    @Value("${seedData}")
    private boolean seedData;

    public DataLoader(
            UserRepository userRepository,
            StoryRepository storyRepository,
            TagRepository tagRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            loadData();
        } catch (Exception e) {
            log.warn("Could not create dummy data", e);
        }
    }

    private void loadData() {
        Users admin = createAdmin();
        if (seedData && storyRepository.count() == 0) {
            createDummyData(admin);
        }

        if (seedData && userRepository.findByEmail("farhan.kabir@demo.com").isEmpty()) {
            createAiData(admin);
        }
    }

    private Users createAdmin() {
        if (adminEmail == null
                || adminEmail.isBlank()
                || adminPassword == null
                || adminPassword.isEmpty()) {
            log.info("ADMIN_EMAIL or ADMIN_PASSWORD is not set, skipping admin account");
            return null;
        }
        String email = adminEmail.trim().toLowerCase();
        Users admin =
                userRepository
                        .findByEmail(email)
                        .orElseGet(
                                () -> {
                                    Users user = new Users();
                                    user.setEmail(email);
                                    user.setName(adminName);
                                    user.setPhone("01700000000");
                                    return user;
                                });
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");
        userRepository.save(admin);
        log.info("Admin is ready: {}", email);
        return admin;
    }

    private void createDummyData(Users admin) {
        Users rahim = createUser("rahim.uddin@demo.com", "Rahim Uddin", "01711111111");
        Users nusrat = createUser("nusrat.jahan@demo.com", "Nusrat Jahan", "01722222222");
        Users tanvir = createUser("tanvir.ahmed@demo.com", "Tanvir Ahmed", "01733333333");

        Users tusk = createUser("elon.tusk@demo.com", "Elon Tusk", "01744444444");
        Users zuckerbot = createUser("mark.zuckerbot@demo.com", "Mark Zuckerbot", "01755555555");
        if (admin == null) admin = rahim;

        Storys s1 =
                createStory(
                        admin,
                        "Welcome to Tech World",
                        "Hello everyone! This is a small blog where we share what we learn about"
                            + " programming. You can write your own story, add tags, comment on"
                            + " other stories and like the ones you enjoy. Please be nice to each"
                            + " other and keep learning.",
                        new String[] {"announcement", "community"},
                        12);

        Storys s2 =
                createStory(
                        rahim,
                        "Getting started with Spring Boot",
                        "Spring Boot makes it very easy to create a REST API in Java. Go to"
                            + " start.spring.io, choose Spring Web, Spring Data JPA and MySQL"
                            + " Driver, then download the project. Create an entity class, a"
                            + " repository interface that extends JpaRepository and a controller"
                            + " with @RestController. Run the main class and your API is ready on"
                            + " port 8080. In my next story I will write about how to add JWT"
                            + " authentication.",
                        new String[] {"java", "spring"},
                        10);

        Storys s3 =
                createStory(
                        nusrat,
                        "React hooks that I use every day",
                        "When I started React I was confused about hooks. Now I use these every"
                            + " day:\n\n"
                            + "useState - to keep a value inside a component.\n"
                            + "useEffect - to call an API when the page loads.\n"
                            + "useContext - to share login state between pages.\n\n"
                            + "Always remember to add the dependency array in useEffect, otherwise"
                            + " it will run after every render.",
                        new String[] {"react", "javascript"},
                        9);

        Storys s4 =
                createStory(
                        tanvir,
                        "How JWT authentication works",
                        "JWT means JSON Web Token. After login the server creates a token and signs"
                            + " it with a secret key. The client saves the token and sends it in"
                            + " the Authorization header like this: Bearer <token>. The server"
                            + " checks the signature and the expiry time on every request, so it"
                            + " does not need to keep a session. Never put a password inside a JWT"
                            + " because anyone can decode the payload.",
                        new String[] {"security", "java", "spring"},
                        7);

        Storys s5 =
                createStory(
                        rahim,
                        "MySQL tips for beginners",
                        "1. Always add a primary key to your table.\n"
                                + "2. Add an index to the column you search the most.\n"
                                + "3. Use LIMIT when you only need a few rows.\n"
                                + "4. Do not store passwords in plain text, store a hash.\n"
                                + "5. Take a backup before running a big update query.",
                        new String[] {"database", "mysql"},
                        6);

        Storys s6 =
                createStory(
                        nusrat,
                        "Git commands I use daily",
                        "git status - see what changed.\n"
                            + "git checkout -b feature/my-feature - create a new branch.\n"
                            + "git add file.txt - stage a file.\n"
                            + "git commit -m \"message\" - save the change.\n"
                            + "git merge feature/my-feature - merge the branch.\n\n"
                            + "Make small commits with clear messages, your team will thank you.",
                        new String[] {"git", "tools"},
                        4);

        Storys s7 =
                createStory(
                        tanvir,
                        "Deploying a Spring Boot app with Docker",
                        "Docker lets you run your app the same way on every machine. Write a"
                            + " Dockerfile with two stages: the first stage builds the jar with"
                            + " Gradle, the second stage copies the jar into a small Java 17 image"
                            + " and runs it. Then you can deploy the image to any cloud that"
                            + " supports Docker.",
                        new String[] {"docker", "spring", "devops"},
                        2);

        Storys s8 =
                createStory(
                        admin,
                        "Clean code in 5 simple rules",
                        "1. Give good names to variables and methods.\n"
                                + "2. Keep methods small, one method should do one thing.\n"
                                + "3. Do not repeat the same code again and again.\n"
                                + "4. Write tests for important logic.\n"
                                + "5. Delete code that nobody uses.",
                        new String[] {"clean-code", "community"},
                        1);

        Storys s9 =
                createStory(
                        tusk,
                        "I will send this blog to Mars",
                        "Big announcement: this blog will be the first blog on Mars. The servers"
                            + " are already packed in a rocket, we only need to fix one small bug"
                            + " before launch. The bug is that the rocket is a JavaScript file."
                            + " Anyway, launch date is next week. Or next year. Definitely soon.",
                        new String[] {"space", "fun"},
                        3);

        Storys s10 =
                createStory(
                        zuckerbot,
                        "Beep boop, my first blog post as a human",
                        "Hello fellow humans. I am a normal human who enjoys normal human things"
                            + " like drinking water and sitting. Today I learned Spring Boot. It"
                            + " was very relatable. Please like this story so that I can collect"
                            + " more data about what humans enjoy. Beep boop.",
                        new String[] {"fun", "community"},
                        1);

        createComment(rahim, s1, "Thanks for creating this blog!");
        createComment(nusrat, s1, "Happy to be here.");
        createComment(nusrat, s2, "Very helpful for beginners, waiting for the JWT part.");
        createComment(tanvir, s2, "You can also use Spring Initializr from IntelliJ.");
        createComment(rahim, s3, "The dependency array tip saved me a lot of time.");
        createComment(admin, s4, "Good explanation. Short expiry time is also important.");
        createComment(nusrat, s4, "Now I finally understand the Bearer header.");
        createComment(tanvir, s5, "Number 4 is the most important one!");
        createComment(rahim, s6, "I also use git log --oneline a lot.");
        createComment(nusrat, s7, "Can you write about docker compose next?");
        createComment(rahim, s8, "Rule number 1 is the hardest for me.");

        createComment(
                zuckerbot, s9, "Mars has no users yet. I will build a social network there first.");
        createComment(tusk, s9, "Deal. Loser has to rewrite everything in COBOL.");
        createComment(nusrat, s9, "Please fix the JavaScript rocket first.");
        createComment(tusk, s10, "Nice try, robot.");
        createComment(zuckerbot, s10, "I am not a robot. I clicked all the traffic lights.");
        createComment(tanvir, s10, "Best post on this blog.");
        createComment(tusk, s2, "Spring Boot is good, but have you tried rockets?");

        createLike(zuckerbot, s9);
        createLike(rahim, s9);
        createLike(tanvir, s9);
        createLike(tusk, s10);
        createLike(nusrat, s10);
        createLike(rahim, s10);
        createLike(admin, s10);
        createLike(rahim, s1);
        createLike(nusrat, s1);
        createLike(tanvir, s1);
        createLike(nusrat, s2);
        createLike(tanvir, s2);
        createLike(admin, s2);
        createLike(rahim, s3);
        createLike(tanvir, s3);
        createLike(rahim, s4);
        createLike(nusrat, s4);
        createLike(admin, s4);
        createLike(nusrat, s5);
        createLike(tanvir, s6);
        createLike(rahim, s7);
        createLike(nusrat, s8);
        createLike(tanvir, s8);

        log.info("Dummy data is created");
    }

    private void createAiData(Users admin) {
        Users farhan = createUser("farhan.kabir@demo.com", "Farhan Kabir", "01766666666");
        Users sadia = createUser("sadia.islam@demo.com", "Sadia Islam", "01777777777");
        Users arif = createUser("arif.hossain@demo.com", "Arif Hossain", "01788888888");
        Users rahim = createUser("rahim.uddin@demo.com", "Rahim Uddin", "01711111111");
        Users nusrat = createUser("nusrat.jahan@demo.com", "Nusrat Jahan", "01722222222");
        Users tanvir = createUser("tanvir.ahmed@demo.com", "Tanvir Ahmed", "01733333333");
        Users tusk = createUser("elon.tusk@demo.com", "Elon Tusk", "01744444444");
        Users zuckerbot = createUser("mark.zuckerbot@demo.com", "Mark Zuckerbot", "01755555555");
        if (admin == null) admin = farhan;

        Storys a1 =
                createStory(
                        farhan,
                        "What is a Large Language Model, in simple words",
                        "A Large Language Model (LLM) is a neural network trained on a huge amount"
                            + " of text. During training it learns one simple task: guess the next"
                            + " word (more exactly, the next token). Because it has seen so much"
                            + " text, it becomes very good at this, and that is enough to answer"
                            + " questions, summarize articles, translate and even write code.\n\n"
                            + "Important to remember: an LLM does not look things up like a search"
                            + " engine. It generates text that sounds right, so you should always"
                            + " check important facts.",
                        new String[] {"ai", "llm"},
                        5);

        Storys a2 =
                createStory(
                        sadia,
                        "Prompt engineering tips that actually work",
                        "After using chatbots at work for a few months, these are the tips that"
                            + " helped me the most:\n\n"
                            + "1. Give context: say who the answer is for and why you need it.\n"
                            + "2. Show an example of the output you want.\n"
                            + "3. Ask for a specific format, like a table or a bullet list.\n"
                            + "4. Break a big task into small steps.\n"
                            + "5. If the answer is wrong, tell the model what is wrong instead of"
                            + " starting again.",
                        new String[] {"ai", "prompt-engineering"},
                        4);

        Storys a3 =
                createStory(
                        arif,
                        "RAG: let a chatbot answer from your own documents",
                        "RAG means Retrieval Augmented Generation. The idea is simple:\n\n"
                            + "1. Split your documents into small chunks.\n"
                            + "2. Turn every chunk into an embedding (a list of numbers) and save"
                            + " it in a vector database.\n"
                            + "3. When a user asks a question, find the chunks that are most"
                            + " similar to the question.\n"
                            + "4. Send those chunks to the LLM together with the question.\n\n"
                            + "Now the model answers from your data instead of only from what it"
                            + " learned in training, and you can show the source of every answer.",
                        new String[] {"ai", "llm", "rag"},
                        3);

        Storys a4 =
                createStory(
                        nusrat,
                        "AI hallucinations and why they happen",
                        "Sometimes an AI model gives an answer that sounds very confident but is"
                            + " completely made up. This is called a hallucination. It happens"
                            + " because the model predicts likely text, it does not know what is"
                            + " true.\n\n"
                            + "How to reduce it: give the model the real data (for example with"
                            + " RAG), ask it to say \"I don't know\" when it is not sure, and"
                            + " always verify names, numbers and links before you use them.",
                        new String[] {"ai", "ethics"},
                        2);

        Storys a5 =
                createStory(
                        tanvir,
                        "Using AI coding assistants without losing your skills",
                        "AI assistants can write boilerplate, tests and SQL queries in seconds. I"
                                + " use them every day, but I follow three rules:\n\n"
                                + "1. Read and understand every line before I commit it.\n"
                                + "2. Write the hard logic myself first, then ask the AI to review"
                                + " it.\n"
                                + "3. Never paste passwords, API keys or customer data into a"
                                + " chatbot.\n\n"
                                + "Used like this, it makes me faster without making me lazy.",
                        new String[] {"ai", "tools", "security"},
                        1);

        Storys a6 =
                createStory(
                        admin,
                        "AI vs Machine Learning vs Deep Learning",
                        "People use these words like they mean the same thing, but they are not the"
                            + " same:\n\n"
                            + "AI - the big idea of making computers do tasks that need human"
                            + " intelligence.\n"
                            + "Machine Learning - a part of AI where the computer learns patterns"
                            + " from data instead of following fixed rules.\n"
                            + "Deep Learning - a part of machine learning that uses neural networks"
                            + " with many layers. Image recognition and LLMs are built with deep"
                            + " learning.",
                        new String[] {"ai", "machine-learning"},
                        0);

        createComment(sadia, a1, "Best simple explanation of LLMs I have read.");
        createComment(rahim, a1, "So it is basically very smart autocomplete?");
        createComment(
                farhan,
                a1,
                "Yes, very very smart autocomplete. That is a good way to think about it.");
        createComment(zuckerbot, a1, "As a fellow human, I also predict the next word. Beep.");
        createComment(arif, a2, "Tip number 5 saves me a lot of time.");
        createComment(tanvir, a2, "Giving an example output works like magic.");
        createComment(nusrat, a3, "Which vector database do you use?");
        createComment(arif, a3, "For small projects even PostgreSQL with pgvector is enough.");
        createComment(farhan, a4, "Always check the links! I got a fake documentation link once.");
        createComment(tusk, a4, "My rocket never hallucinates. It only explodes sometimes.");
        createComment(
                sadia, a5, "Rule number 3 is so important. Companies have leaked data like this.");
        createComment(rahim, a5, "I use it for writing unit tests, it helps a lot.");
        createComment(nusrat, a6, "The circle diagram finally makes sense now.");

        createLike(sadia, a1);
        createLike(arif, a1);
        createLike(rahim, a1);
        createLike(tanvir, a1);
        createLike(farhan, a2);
        createLike(nusrat, a2);
        createLike(tanvir, a2);
        createLike(farhan, a3);
        createLike(sadia, a3);
        createLike(nusrat, a3);
        createLike(arif, a4);
        createLike(sadia, a4);
        createLike(farhan, a5);
        createLike(arif, a5);
        createLike(nusrat, a5);
        createLike(sadia, a6);
        createLike(tanvir, a6);
        createLike(zuckerbot, a6);

        log.info("AI dummy data is created");
    }

    private Users createUser(String email, String name, String phone) {
        Optional<Users> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
        user.setRole("USER");
        return userRepository.save(user);
    }

    private Storys createStory(
            Users author, String title, String description, String[] tagNames, int daysAgo) {
        Storys story = new Storys(title, description);
        story.setAuthorid(author);
        story.setCreatedDate(new Date(System.currentTimeMillis() - daysAgo * 24L * 60 * 60 * 1000));

        story.setTags(
                Arrays.stream(tagNames)
                        .map(
                                name ->
                                        tagRepository
                                                .findByName(name)
                                                .orElseGet(
                                                        () -> tagRepository.save(new Tags(name))))
                        .toList());
        return storyRepository.save(story);
    }

    private void createComment(Users user, Storys story, String text) {
        Comments comment = new Comments();
        comment.setText(text);
        comment.setUser(user);
        comment.setStory(story);
        commentRepository.save(comment);
    }

    private void createLike(Users user, Storys story) {
        likeRepository.save(new Likes(user, story));
    }
}
