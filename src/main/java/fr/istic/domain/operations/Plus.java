package fr.istic.domain.operations;

import fr.istic.domain.Operation;
import fr.istic.domain.OperationException;

public class Plus implements Operation {
    /**
     * Make the addition between op1 and op2
     * @param op1
     * @param op2
     * @return the addition result
     * @throws OperationException
     */
    @Override
    public double execute(int op1, int op2) throws OperationException {
        return op1 + op2;
    }
}
