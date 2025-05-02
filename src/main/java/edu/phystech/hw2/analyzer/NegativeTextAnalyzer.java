package edu.phystech.hw2.analyzer;

import java.util.List;

public class NegativeTextAnalyzer extends KeywordAnalyzer {
    public NegativeTextAnalyzer() {
        super(List.of(":(", "=(", ":|"), Label.NEGATIVE);
    }
}
