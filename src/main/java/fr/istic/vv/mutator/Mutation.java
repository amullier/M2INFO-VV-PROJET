package fr.istic.vv.mutator;

import fr.istic.vv.common.MutantContainer;

/**
 * Mutation class describes mutation information use by the mutator
 */
public class Mutation {

    //Bytecode operation information might be mutated
    private String targetOperation;
    private int targetOperationCode;

    //Bytecode operation information of mutation results
    private String mutationOperation;
    private int mutationOperationCode;

    //Mutation type
    private MutantContainer.MutantType mutationType;

    public Mutation(String targetOperation, int targetOperationCode, String mutationOperation, int mutationOperationCode, MutantContainer.MutantType mutationType) {
        this.targetOperation = targetOperation;
        this.targetOperationCode = targetOperationCode;

        this.mutationOperation = mutationOperation;
        this.mutationOperationCode = mutationOperationCode;

        this.mutationType = mutationType;
    }

    public String getTargetOperation() {
        return targetOperation;
    }

    public int getTargetOperationCode() {
        return targetOperationCode;
    }

    public int getMutationOperationCode() {
        return mutationOperationCode;
    }

    public MutantContainer.MutantType getMutationType() {
        return mutationType;
    }

    @Override
    public String toString() {
        return "Mutation{  "+targetOperation+"#"+targetOperationCode+" -> "+mutationOperation+"#"+mutationOperationCode+" }";
    }
}
