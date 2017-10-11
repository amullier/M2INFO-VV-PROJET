package fr.istic.domain;

public class Plus implements Operation {
    /**
     * Make the addition between op1 and op2
     * @param op1
     * @param op2
     * @return the addition result
     * @throws OperationException
     */
    @Override
    public int execute(int op1, int op2) throws OperationException {
        return op1 + op2;
    }
}
