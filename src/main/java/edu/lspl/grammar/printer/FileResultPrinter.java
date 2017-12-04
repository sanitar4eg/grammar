package edu.lspl.grammar.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Text;
import ru.lspl.text.Transition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Qualifier("file")
public class FileResultPrinter implements ResultPrinter {

    private static final Logger log = LoggerFactory.getLogger(FileResultPrinter.class);
    private static final String WORD_FORMAT = "Word: %s, Form: %s, Speech: %s, Attrs: %s";
    private static final String WORDS = "results/words.txt";
    private static final String DELIMITER = "= ";
    private static final String RESULTS = "results";
    private static final Function<String, String> fileName = (name) -> "results/" + name + ".txt";

    public void printMatches(Text text, List<Pattern> patterns) {
        Map<Pattern, List<Match>> map = getPatternListMap(text, patterns);

        map.forEach(this::printMatchesToFile);

    }

    private void printMatchesToFile(Pattern pattern, List<Match> matches) {
        try {
            checkResultsDir();

            Path file = Paths.get(fileName.apply(pattern.name));

            String header = new StringJoiner(DELIMITER)
                    .add(pattern.name).add(pattern.getSource()).toString();
            Files.write(file, header.getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            Files.write(file, "\n".getBytes(), StandardOpenOption.APPEND);

            List<String> strings = matches.stream()
                    .map(Transition::getContent)
                    .collect(Collectors.toList());
            Files.write(file, strings, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (Exception e) {
            log.error("Error while create file", e);
        }
    }

    public void printWords(Text text) {
        try {
            checkResultsDir();

            Path file = Paths.get(WORDS);

            List<String> lines = text.getWords().stream()
                    .map(word -> String.format(WORD_FORMAT,
                            word.base, word.form, word.speechPart, attrToString(word.getAttributes())))
                    .collect(Collectors.toList());

            Files.write(file, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (Exception e) {
            log.error("Error while create file", e);
        }
    }

    private void checkResultsDir() throws IOException {
        Path results = Paths.get(RESULTS);
        if (!Files.exists(results))
            Files.createDirectories(results);
    }
}
