package edu.lspl.grammar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchVariant;
import ru.lspl.text.Text;
import ru.lspl.text.Transition;
import ru.lspl.text.attributes.AttributeKey;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class SampleStart {

    private static final Logger log = LoggerFactory.getLogger(SampleStart.class);

    @Value("classpath:static/text.txt")
    private Resource text;

    @Value("classpath:static/patterns.txt")
    private Resource patterns;

    private final TextAnalyzer textAnalyzer;
    private List<Pattern> patterns1;

    @Autowired
    public SampleStart(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    @PostConstruct
    public void init() {
        textAnalyzer.loadPatterns(patterns);

        Text text = textAnalyzer.analyze(this.text);

        List<Pattern> patterns = textAnalyzer.loadPatterns(this.patterns);

        printWords(text);

        printMatches(text, patterns);
    }

    private void printMatches(Text text, List<Pattern> patterns) {
        Map<Pattern, List<Match>> map = patterns.stream()
                .filter(pattern -> !LsplTextAnalyzer.definedPatterns.contains(pattern.name))
                .collect(Collectors.toMap(
                pattern -> pattern,
                text::getMatches));

        map.forEach((pattern, matches) -> log.info("\nPattern: {},\nMatches: {}",
                new StringJoiner("= ").add(pattern.name).add(pattern.getSource()).toString(),
                matches.stream()
                        .map(Transition::getContent)
                        .collect(Collectors.joining("\n ", "\n[", "]"))));

    }

    private void printWords(Text text) {
        text.getWords()
                .forEach(word -> log.info("\nWord: {}, Form: {}, Speech: {}, Attrs: {}",
                        word.base, word.form, word.speechPart, attrToString(word.getAttributes())));
    }

    private String attrToString(Map<Integer, Object> attributes) {
        return attributes.entrySet().stream()
                .map(entry -> new StringJoiner(": ")
                        .add(AttributeKey.valueOf(entry.getKey()).getTitle())
                        .add(entry.getValue().toString())
                        .toString()
                ).collect(Collectors.joining(", "));
    }
}
