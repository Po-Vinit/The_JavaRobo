package knowledge;

import config.AppConfig;
import exception.KnowledgeBaseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KnowledgeBase {

    private final List<KnowledgeEntry> entries;
    private final String filePath;

    public KnowledgeBase() {
        this.entries = new ArrayList<>();
        this.filePath = AppConfig.getInstance().getKnowledgeBasePath();
    }

    public void load() throws KnowledgeBaseException {
        entries.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\|", 2);
                if (parts.length != 2) {
                    throw new KnowledgeBaseException(
                            "Malformed line " + lineNumber + " in knowledge base: " + line);
                }

                entries.add(new KnowledgeEntry(parts[0].trim(), parts[1].trim()));
            }

        } catch (IOException e) {
            throw new KnowledgeBaseException(
                    "Could not read knowledge base file: " + filePath, e);
        }
    }

    public Optional<String> findAnswer(String userInput) {
        return entries.stream()
                .filter(entry -> entry.matches(userInput))
                .max((e1, e2) -> Integer.compare(e1.matchScore(userInput), e2.matchScore(userInput)))
                .map(KnowledgeEntry::getAnswer);

    }

    public int size() {
        return entries.size();
    }
}