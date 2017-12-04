package edu.lspl.grammar;

import edu.lspl.grammar.analyzer.TextAnalyzer;
import edu.lspl.grammar.printer.ResultPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Text;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SampleStart {

    private static final Logger log = LoggerFactory.getLogger(SampleStart.class);

    @Value("classpath:static/text.txt")
    private Resource text;

    @Value("classpath:static/patterns.txt")
    private Resource patterns;

    private final ResultPrinter printer;
    private final TextAnalyzer textAnalyzer;

    @Autowired
    public SampleStart(@Qualifier("file") ResultPrinter printer, TextAnalyzer textAnalyzer) {
        this.printer = printer;
        this.textAnalyzer = textAnalyzer;
    }

    @PostConstruct
    public void init() {
        textAnalyzer.loadPatterns(patterns);

        Text text = textAnalyzer.analyze(this.text);

        List<Pattern> patterns = textAnalyzer.loadPatterns(this.patterns);

        printer.printWords(text);

        printer.printMatches(text, patterns);
    }
}
