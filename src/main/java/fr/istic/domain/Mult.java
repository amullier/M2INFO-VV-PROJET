package fr.istic.domain;

public class Mult implements Operation {
    /**
     * Makes the multiplication between operands op1 and op2
     *
     * @param op1
     * @param op2
     * @return the multiplication result
     * @throws OperationException
     */
    @Override
    public int execute(int op1, int op2) throws OperationException {
        return op1*op2;
    }
}
