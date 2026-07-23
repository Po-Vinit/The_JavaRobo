package services;

import config.AppConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String logFilePath;

    public FileLogger() {
        this.logFilePath = AppConfig.getInstance().getLogsPath();
    }

    public void info(String message) {
        write("INFO", message);
    }

    public void error(String message) {
        write("ERROR", message);
    }

    public void error(String message, Throwable cause) {
        write("ERROR", message + " | Cause: " + cause.getMessage());
    }

    private synchronized void write(String level, String message) {
        File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        String line = String.format("[%s] [%s] %s",
                LocalDateTime.now().format(TIME_FORMAT), level, message);

        System.out.println(line);

        try (FileWriter fw = new FileWriter(logFilePath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(line);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src\main\java).FullName
java -cp out app.Main
