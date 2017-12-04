package edu.lspl.grammar.analyzer;

import org.springframework.core.io.Resource;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Text;

import java.util.List;

public interface TextAnalyzer {

    Text analyze(String text);

    Text analyze(Resource file);

    List<Pattern> loadPatterns(List<String> patterns);

    List<Pattern> loadPatterns(Resource file);

}
