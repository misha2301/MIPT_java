package edu.phystech.hw2.analyzer;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

enum Label {
    SPAM, NEGATIVE, TOO_LONG, OK
}

interface TextAnalyzer {
    Label processText(String input);
}

abstract class KeywordAnalyzer implements TextAnalyzer {
    private final String[] triggers;

    protected KeywordAnalyzer(String[] triggers) {
        this.triggers = triggers;
    }

    @Override
    public Label processText(String input) {
        for (String trigger : triggers) {
            if (input.equals(trigger) ||
                input.startsWith(trigger + " ") ||
                input.endsWith(" " + trigger) ||
                input.contains(" " + trigger + " ")) {
                return labelType();
            }
        }
        return Label.OK;
    }

    protected abstract Label labelType();
}

class SpamAnalyzer extends KeywordAnalyzer {
    public SpamAnalyzer(List<String> spamWords) {
        super(spamWords.toArray(new String[0]));
    }

    @Override
    protected Label labelType() {
        return Label.SPAM;
    }
}

class NegativeTextAnalyzer extends KeywordAnalyzer {
    private static final String[] NEGATIVE_MARKERS = {":(", "=(", ":|"};

    public NegativeTextAnalyzer() {
        super(NEGATIVE_MARKERS);
    }

    @Override
    protected Label labelType() {
        return Label.NEGATIVE;
    }
}

class TooLongTextAnalyzer implements TextAnalyzer {
    private final int maxAllowed;

    public TooLongTextAnalyzer(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    @Override
    public Label processText(String input) {
        return input.length() > maxAllowed ? Label.TOO_LONG : Label.OK;
    }
}

public class TextAnalyzerTest {

    @Test
    public void tooLongTextTest() {
        TooLongTextAnalyzer analyzer = new TooLongTextAnalyzer(3);
        Assertions.assertEquals(Label.TOO_LONG, analyzer.processText("123123"));
        Assertions.assertEquals(Label.OK, analyzer.processText("12"));
    }

    @Test
    public void spamTextTest() {
        SpamAnalyzer analyzer = new SpamAnalyzer(List.of("kek", "lol"));
        Assertions.assertEquals(Label.SPAM, analyzer.processText("kek 123"));
        Assertions.assertEquals(Label.OK, analyzer.processText("123"));
        Assertions.assertEquals(Label.SPAM, analyzer.processText("123 lol"));
    }

    @Test
    public void negativeTextTest() {
        NegativeTextAnalyzer analyzer = new NegativeTextAnalyzer();
        Assertions.assertEquals(Label.NEGATIVE, analyzer.processText("hello :("));
        Assertions.assertEquals(Label.NEGATIVE, analyzer.processText(":) =("));
        Assertions.assertEquals(Label.NEGATIVE, analyzer.processText("))) :|"));
        Assertions.assertEquals(Label.OK, analyzer.processText("))) :||"));
    }
}
