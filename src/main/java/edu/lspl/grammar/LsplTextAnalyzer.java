package edu.lspl.grammar;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.lspl.patterns.Pattern;
import ru.lspl.patterns.PatternBuilder;
import ru.lspl.patterns.PatternBuildingException;
import ru.lspl.text.Text;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Service
public class LsplTextAnalyzer implements TextAnalyzer {

    public static final Set<String> definedPatterns = new HashSet<>(
//            Arrays.asList("One", "Two", "Three", "Four", "Five", "BornPlace")
            Arrays.asList("BornPlace", "Locative", "MaterialFeature")
    );


    private static final Logger log = LoggerFactory.getLogger(LsplTextAnalyzer.class);

    private PatternBuilder patternBuilder;

    private List<Pattern> patterns = new ArrayList<>();

    @PostConstruct
    private void init() {
        patternBuilder = PatternBuilder.create();
    }

    @Override
    public Text analyze(String text) {
        Text result = Text.create(text);
        result.getMatches(patternBuilder.getDefinedPatterns());
        return result;
    }

    @Override
    public Text analyze(Resource resource) {
        return analyze(openResource(resource));
    }

    @Override
    public List<Pattern> loadPatterns(List<String> patterns) {
        patterns.forEach(this::buildPattern);
        return patternBuilder.getDefinedPatterns();
    }

    @Override
    public List<Pattern> loadPatterns(Resource file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<String> rawPatterns = IOUtils.readLines(inputStream);
            return loadPatterns(rawPatterns);
        } catch (IOException e) {
            log.error("Error while load patterns", e);
            throw new RuntimeException(e);
        }
    }

    private void buildPattern(String line) {
        try {
            patternBuilder.build(line);
        } catch (PatternBuildingException e) {
            log.error("Error while build pattern", e);
            throw new RuntimeException(e);
        }
    }

    private static String openResource(Resource file) {
        try (InputStream is = file.getInputStream()) {
            return IOUtils.toString(is);
        } catch (IOException e) {
            log.error("Error while opening file", e);
            throw new RuntimeException(e);
        }
    }
}
