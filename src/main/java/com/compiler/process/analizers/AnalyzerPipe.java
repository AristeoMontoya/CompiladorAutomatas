package com.compiler.process.analizers;

import java.util.List;

public interface AnalyzerPipe<INPUT, OUTPUT> {

    OUTPUT runAnalyzer(INPUT input);

    List<String> getErrors();
}
