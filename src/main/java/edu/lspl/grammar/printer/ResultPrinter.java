package edu.lspl.grammar.printer;

import edu.lspl.grammar.analyzer.LsplTextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Text;
import ru.lspl.text.attributes.AttributeKey;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public interface ResultPrinter {

    Logger log = LoggerFactory.getLogger(ResultPrinter.class);

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

    default Map<Pattern, List<String>> getPatternListMap(Text text, List<Pattern> patterns) {

        return patterns.stream()
                .filter(pattern -> !LsplTextAnalyzer.definedPatterns.contains(pattern.name))
                .collect(toMap(identity(),
                        pattern -> text.getMatches(pattern).stream()
                                .map(Match::getContent)
                                .collect(toList())));

//                .map(pattern ->
//                        new SimpleEntry<>(pattern, text.getMatches(pattern)))
//                .map(entry ->
//                        new SimpleEntry<>(entry.getKey(), countAndOrder(entry.getValue())))
//                .collect(Collectors.toMap((SimpleEntry::getKey), (SimpleEntry::getValue)));
    }

    default List<String> countAndOrder(Object value) {
        List<Match> matches = (List<Match>) value;

        return matches.stream()
                .map(Match::getContent)
                .map(String::toLowerCase)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(reverseOrder()))
                .map(entry ->
                        String.format("%s: %s", entry.getValue(), entry.getKey()))
                .collect(toList());
    }
}
