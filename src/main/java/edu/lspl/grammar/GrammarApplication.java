package edu.lspl.grammar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import ru.lspl.text.Text;
import ru.lspl.text.attributes.AttributeKey;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SpringBootApplication
public class GrammarApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GrammarApplication.class, args);
    }
}
