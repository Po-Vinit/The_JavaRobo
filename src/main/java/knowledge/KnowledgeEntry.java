package knowledge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KnowledgeEntry {

    // Common filler words jo ignore honge matching ke time
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "am", "was", "were",
            "what", "who", "where", "when", "why", "how",
            "do", "does", "did", "you", "your", "i", "me", "my",
            "to", "of", "in", "on", "at", "can", "could", "please"
    ));

    private final String question;
    private final String answer;
    private final Set<String> keywords;

    public KnowledgeEntry(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.keywords = extractKeywords(question);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    /**
     * Question se sirf meaningful words nikalta hai, filler words hata ke.
     */
    private Set<String> extractKeywords(String text) {
        Set<String> words = new HashSet<>();
        String[] tokens = text.toLowerCase().trim().split("\\s+");

        for (String token : tokens) {
            String cleaned = token.replaceAll("[^a-zA-Z0-9]", "");
            if (!cleaned.isEmpty() && !STOP_WORDS.contains(cleaned)) {
                words.add(cleaned);
            }
        }
        return words;
    }

    /**
     * User input ke keywords nikaal ke check karta hai
     * kitne humare question ke keywords se match hote hain.
     * Agar kam se kam ek meaningful keyword match ho jaye, true return karega.
     */
    public boolean matches(String userInput) {
        Set<String> inputKeywords = extractKeywords(userInput);

        if (keywords.isEmpty() || inputKeywords.isEmpty()) {
            return false;
        }

        int matchCount = 0;
        for (String keyword : keywords) {
            if (inputKeywords.contains(keyword)) {
                matchCount++;
            }
        }

        // Kam se kam 50% keywords match hone chahiye (ya kam se kam 1 agar sirf 1 keyword hai)
        int required = Math.max(1, keywords.size() / 2);
        return matchCount >= required;
    }

    /**
     * Score deta hai — kitne keywords match hue, taaki best match choose kar sakein.
     */
    public int matchScore(String userInput) {
        Set<String> inputKeywords = extractKeywords(userInput);
        int score = 0;
        for (String keyword : keywords) {
            if (inputKeywords.contains(keyword)) {
                score++;
            }
        }
        return score;
    }
}