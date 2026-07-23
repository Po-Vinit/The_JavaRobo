package chatbot;

import knowledge.KnowledgeBase;
import model.Message;
import services.FileLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Random;

public class ChatEngine {

    private final KnowledgeBase knowledgeBase;
    private final FileLogger logger;
    private final String userName;
    private final String historyFilePath = "logs/chat_history.txt";
    private final Random random = new Random();
    private int messageCount = 0;

    // Alag-alag greeting variations for randomness
    private final String[] helloVariations = {
            "Hi there!", "Hello!", "Hey! What's up?", "Hey there!", "Greetings!"
    };

    public ChatEngine(KnowledgeBase knowledgeBase, FileLogger logger, String userName) {
        this.knowledgeBase = knowledgeBase;
        this.logger = logger;
        this.userName = userName;
    }

    public String respond(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Please type something so I can help you, " + userName + ".";
        }

        try {
            messageCount++;

            Message userMessage = new Message(userInput, Message.Sender.USER);
            logger.info(userName + " said: " + userMessage.getContent());
            saveToHistory(userName + ": " + userInput);

            String lowerInput = userInput.toLowerCase();

            // Naam pucha to
            if (lowerInput.contains("my name")) {
                String reply = "Your name is " + userName + "!";
                saveToHistory("Robo: " + reply);
                return reply;
            }

            // Time-based greeting
            if (lowerInput.contains("good morning") || lowerInput.contains("good afternoon")
                    || lowerInput.contains("good evening") || lowerInput.contains("good night")) {
                String reply = getTimeBasedGreeting();
                saveToHistory("Robo: " + reply);
                return reply;
            }

            // Random hello variation
            if (lowerInput.equals("hello") || lowerInput.equals("hi") || lowerInput.equals("hey")) {
                String reply = helloVariations[random.nextInt(helloVariations.length)] + " I'm TheJavaRobo.";
                saveToHistory("Robo: " + reply);
                return reply;
            }

            // Normal knowledge base lookup
            Optional<String> answer = knowledgeBase.findAnswer(userInput);

            String reply = answer.orElse(
                    "Sorry " + userName + ", I don't know the answer to that yet. I'm still learning!"
            );

            Message botMessage = new Message(reply, Message.Sender.BOT);
            logger.info("Bot replied: " + botMessage.getContent());
            saveToHistory("Robo: " + reply);

            return reply;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid message content", e);
            return "Sorry, something went wrong processing that. Try again?";
        } catch (Exception e) {
            logger.error("Unexpected error in ChatEngine", e);
            return "Oops, something unexpected happened. Please try again.";
        }
    }

    /**
     * System ke current time ke hisaab se greeting deta hai.
     */
    private String getTimeBasedGreeting() {
        int hour = LocalTime.now().getHour();

        if (hour >= 5 && hour < 12) {
            return "Good morning, " + userName + "! Hope you have a great day.";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon, " + userName + "! How's your day going?";
        } else if (hour >= 17 && hour < 21) {
            return "Good evening, " + userName + "! Hope you had a productive day.";
        } else {
            return "Good night, " + userName + "! Sleep well.";
        }
    }

    /**
     * Session end pe summary return karta hai.
     */
    public String getSessionSummary() {
        return "During this session, we exchanged " + messageCount + " message(s). Thanks for chatting, " + userName + "!";
    }

    private void saveToHistory(String line) {
        try (FileWriter fw = new FileWriter(historyFilePath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(line);
        } catch (IOException e) {
            logger.error("Failed to save chat history", e);
        }
    }
}