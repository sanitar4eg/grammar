package edu.lspl.grammar.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Text;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
@Qualifier("console")
public class ConsoleResultPrinter implements ResultPrinter {

    private static final Logger log = LoggerFactory.getLogger(ConsoleResultPrinter.class);

    public ConsoleResultPrinter() {
    }

    public void printMatches(Text text, List<Pattern> patterns) {
        Map<Pattern, List<String>> map = getPatternListMap(text, patterns);

        map.forEach((pattern, matches) -> log.info("\nPattern: {},\nMatches: {}",
                new StringJoiner("= ").add(pattern.name).add(pattern.getSource()).toString(),
                matches.stream()
                        .collect(Collectors.joining("\n ", "\n[", "]"))));

    }

    public void printWords(Text text) {
        text.getWords()
                .forEach(word -> log.info("\nWord: {}, Form: {}, Speech: {}, Attrs: {}",
                        word.base, word.form, word.speechPart, attrToString(word.getAttributes())));
    }
}