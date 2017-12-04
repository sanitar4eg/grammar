package edu.lspl.grammar.printer;

import edu.lspl.grammar.analyzer.LsplTextAnalyzer;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Text;
import ru.lspl.text.attributes.AttributeKey;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public interface ResultPrinter {

    void printMatches(Text text, List<Pattern> patterns);

    void printWords(Text text);

    default String attrToString(Map<Integer, Object> attributes) {
        return attributes.entrySet().stream()
                .map(entry -> new StringJoiner(": ")
                        .add(AttributeKey.valueOf(entry.getKey()).getTitle())
                        .add(entry.getValue().toString())
                        .toString()
                ).collect(Collectors.joining(", "));
    }

    default Map<Pattern, List<Match>> getPatternListMap(Text text, List<Pattern> patterns) {
        return patterns.stream()
                .filter(pattern -> !LsplTextAnalyzer.definedPatterns.contains(pattern.name))
                .collect(Collectors.toMap(pattern -> pattern, text::getMatches));
    }
}
