package config;

public class AppConfig {

    private static AppConfig instance;

    private final String knowledgeBasePath;
    private final String logsPath;

    private AppConfig() {
        this.knowledgeBasePath = "docs/knowledge_base.txt";
        this.logsPath = "logs/app.log";
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getKnowledgeBasePath() {
        return knowledgeBasePath;
    }

    public String getLogsPath() {
        return logsPath;
    }
}