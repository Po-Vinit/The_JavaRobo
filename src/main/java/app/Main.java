package app;

import chatbot.ChatEngine;
import exception.KnowledgeBaseException;
import knowledge.KnowledgeBase;
import services.FileLogger;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileLogger logger = new FileLogger();
        KnowledgeBase knowledgeBase = new KnowledgeBase();

        try {
            knowledgeBase.load();
            logger.info("Knowledge base loaded with " + knowledgeBase.size() + " entries.");
        } catch (KnowledgeBaseException e) {
            logger.error("Failed to load knowledge base", e);
            System.out.println("Fatal error: could not start TheJavaRobo. Check logs.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println(" TheJavaRobo — Core Java Chatbot ");
        System.out.println("=================================");

        System.out.print("Robo: Hi! What's your name? ");
        String userName = scanner.nextLine().trim();

        if (userName.isEmpty()) {
            userName = "Friend";
        }

        ChatEngine chatEngine = new ChatEngine(knowledgeBase, logger, userName);

        System.out.println("Robo: Nice to meet you, " + userName + "! Type 'exit' or 'bye' to quit.\n");

        while (true) {
            try {
                System.out.print(userName + ": ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Robo: Goodbye, " + userName + "! Have a great day.");
                    System.out.println("Robo: " + chatEngine.getSessionSummary());
                    break;
                }

                String response = chatEngine.respond(input);
                System.out.println("Robo: " + response);

            } catch (Exception e) {
                logger.error("Unexpected error in main loop", e);
                System.out.println("Robo: Something went wrong, but I'm still here! Try again.");
            }
        }

        scanner.close();
    }
}