import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

    public static void findValidNineLetterWords(Set<String> listOfWordsToTraverse) {
        // filter words which have 9 letter or less, and they contain "I" or "A"
        // group the words in a map based on their length
        Map<Integer, Set<String>> groupedWords = listOfWordsToTraverse.stream()
                .filter(word -> word.length() <= 9
                        && (word.toLowerCase().contains("i") || word.toLowerCase().contains("a")))
                .collect(Collectors.toMap(
                        String::length,
                        word -> new HashSet<>(Set.of(word)),
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        }
                ));

        int validWordsCount = 0;
        // optional
        // Set<String> validWords = new HashSet<>();
        for (String word : groupedWords.get(9)) {
            if (isValidWord(groupedWords, word)) {
                validWordsCount++;
                // optional
                // validWords.add(word);
            }
        }

        System.out.println(validWordsCount);
        // optional
        // System.out.println(validWords);
    }

    /**
     * Search recursively into our grouped words by removing one letter at a time until we find a 2-letter word match
     * or until the word candidate doesn't match any of the grouped words
     */
    public static boolean isValidWord(Map<Integer, Set<String>> groupedWords, String word) {
        if (word.length() == 2) {
            return groupedWords.get(2).contains(word);
        }

        for (int i = 0; i < word.length(); i++) {
            String candidateWord = word.substring(0, i) + word.substring(i + 1);
            if (groupedWords.get(candidateWord.length()).contains(candidateWord)) {
                return isValidWord(groupedWords, candidateWord);
            }
        }

        return false;
    }

    public static Set<String> loadAllWords() throws IOException, URISyntaxException {
        URI wordsUrl =
                new URI("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(wordsUrl.toURL().openConnection().getInputStream()))) {
            return br.lines().skip(2).collect(Collectors.toSet());
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Set<String> listOfWordsToTraverse = loadAllWords();

        double durationForAllExecutions = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();

            findValidNineLetterWords(listOfWordsToTraverse);

            long endTime = System.nanoTime();

            double durationOfCurrentExecution = (double) (endTime - startTime) / 1000000000;
            System.out.printf("Time for execution %d: %f\n", i, durationOfCurrentExecution);

            durationForAllExecutions += durationOfCurrentExecution;
        }

        System.out.println("Average duration for 10 executions: " + durationForAllExecutions / 10);
    }
}
