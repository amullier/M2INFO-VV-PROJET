package fr.istic.domain.operations;

import fr.istic.domain.Operation;
import fr.istic.domain.OperationException;

public class Minus implements Operation {
    /**
     * Makes the substraction between operands op1 and op2
     *
     * @param op1
     * @param op2
     * @return the substraction result
     * @throws OperationException
     */
    @Override
    public double execute(int op1, int op2) throws OperationException {
        return 0;
    }
}
