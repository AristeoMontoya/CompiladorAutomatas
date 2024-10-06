package com.compiler.process;

import com.compiler.model.dto.SemanticAnalyzerResults;
import com.compiler.model.structures.Quadruplet;
import com.compiler.model.structures.Token;

import com.compiler.process.analizers.AnalyzerPipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Pipeline that executes each compilation stage in order, taking the output
 * from one stage as the input from the next stage.
 *
 * @param <I> input
 * @param <O> output
 */
public class CompilerPipeline<I, O> {
    private String inputCode;
    private List<String> errors;
    private ArrayList<String[]> table;
    private ArrayList<Token> tokens;
    private String quadruplets;

    private Collection<AnalyzerPipe<?, ?>> compilationPipeline;

    private CompilerPipeline(AnalyzerPipe<I, O> pipe) {
        this.compilationPipeline = Collections.singletonList(pipe);
    }

    private CompilerPipeline(Collection<AnalyzerPipe<I, O>> pipes) {
        this.compilationPipeline = new ArrayList<>(pipes);
    }

    /**
     * Create a new {@link CompilerPipeline} with a single pipe.
     *
     * @param pipe to add to the pipeline.
     * @param <I>  input
     * @param <O>  output
     * @return a new instance of {@link CompilerPipeline}
     */
    public static <I, O> CompilerPipeline<I, O> of(AnalyzerPipe<I, O> pipe) {
        return new CompilerPipeline<>(pipe);
    }

    /**
     * Creates a new {@link CompilerPipeline} with the new {@link AnalyzerPipe}
     * appended to the list of existing pipes.
     *
     * @param pipe to add to the pipeline.
     * @param <N>  new output type from pipeline.
     * @return a new instance of {@link CompilerPipeline}.
     */
    public <N> CompilerPipeline<I, N> withNextPipe(AnalyzerPipe<O, N> pipe) {
        final ArrayList<AnalyzerPipe<?, ?>> newPipeline = new ArrayList<>(compilationPipeline);
        newPipeline.add(pipe);
        return new CompilerPipeline(newPipeline);
    }

    public void initialize() {
        errors = new ArrayList<>();
        table = new ArrayList<>();
    }

    /**
     * Executes each stage in the pipeline in sequential order
     *
     * @param inputCode
     * @return
     */
    public O runPipeline(I inputCode) {
//        runLexicalAnalyzer();
//        runSyntacticAnalyzer();        //TODO: Rehacer esto. Pero después
//        runSemanticAnalyzer();         //TODO: Hacer esto correctamente. Después también
//        generateIntermediateCode();

        /*
        Lexical Analyzer   - Takes String returns a list of tokens
        syntactic analyzer - Takes a list of tokens returns a list of tokens. In reality this should be a syntax tree
        semantic analyzer  - Takes the syntax tree as an input and outputs a decorated syntax tree with data types
         */
        initialize();

        Object pipeOutput = inputCode;
        for (final AnalyzerPipe pipe : compilationPipeline) {
            pipeOutput = pipe.runAnalyzer(pipeOutput);
            this.errors.addAll(pipe.getErrors());
        }

        // Hacky way to preserve the poorly designed implementation I had before
        // TODO: Refactor this
        if (pipeOutput instanceof SemanticAnalyzerResults) {
            generateTable(((SemanticAnalyzerResults) pipeOutput).getTableData());
        }
        return (O) pipeOutput;
    }

//    private void runLexicalAnalyzer() {
//        LexicalAnalyzer lexer = new LexicalAnalyzer(inputCode);
//        if (!lexer.analyze()) {
//            errors += lexer.getErrorMessage() + "\n";
//        } else {
//            tokens = lexer.getTokens();
//        }
//    }
//
//    private void runSyntacticAnalyzer() {
//        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(tokens);
//        if (!syntacticAnalyzer.analyze()) {
//            errors += syntacticAnalyzer.getOutput() + "\n";
//        } else {
//            tokens = syntacticAnalyzer.getTokens();
//        }
//    }
//
//    private void runSemanticAnalyzer() {
//        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(tokens);
//        if (!semanticAnalyzer.analyze()) {
//            errors += semanticAnalyzer.getErrors();
//        }
//        generateTable(semanticAnalyzer.getTable());
//    }
//
//    private void generateIntermediateCode() {
//        IntermediateCode intermediateCode = new IntermediateCode(tokens);
//        formatQuadrupletsAsString(intermediateCode.getQuadruplets());
//    }

    private void optimizeIntermediateCode() {
        throw new UnsupportedOperationException("Operation not yet implemented");
    }

    private void buildObjectCode() {
        throw new UnsupportedOperationException("Operation not yet implemented");
    }

    public void generateTable(HashMap<String, Token> tempTable) {
        Collection<Token> rows = tempTable.values();
        for (Token token : rows) {
            String[] registry = new String[]{
                    token.getSymbol(),
                    token.getValue(),
                    token.getDataType(),
                    String.valueOf(token.getLine()),
                    token.getScope()
            };
            table.add(registry);
        }
    }

    public ArrayList<String[]> getTable() {
        return table;
    }

    private void formatQuadrupletsAsString(ArrayList<Quadruplet> quadruplets) {
        String identifier = null;
        if (quadruplets.get(0).getIdentifier() != null)
            identifier = quadruplets.get(0).getIdentifier();
        int count = 1;
        if (quadruplets.size() > 1)
            this.quadruplets = "Cuadruplo 1";
        for (int i = 0; i < quadruplets.size(); i++) {
            Quadruplet currentQuadruplet = quadruplets.get(i);
            if (identifier.equals(currentQuadruplet.getIdentifier())) {
                this.quadruplets += "\n" + currentQuadruplet.getFormat();
            } else {
                count++;
                this.quadruplets += "Resultado de " + quadruplets.get(i - 1).getIdentifier() + " = " + quadruplets.get(i - 1).getResult() + "\nCuadruplo " + count;
                this.quadruplets += "\n" + currentQuadruplet.getFormat();
            }
        }
        this.quadruplets += "\nResultado de " + quadruplets.get(quadruplets.size() - 1).getIdentifier() + " = " + quadruplets.get(quadruplets.size() - 1).getResult();
    }

    public String getQuadruplets() {
        return quadruplets;
    }

    public List<String> getErrors() {
        return errors;
    }
}
