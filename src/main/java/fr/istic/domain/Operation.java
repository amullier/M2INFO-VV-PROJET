package fr.istic.domain;

public interface Operation {

    /**
     * Makes the operation between operands op1 and op2
     * @param op1
     * @param op2
     * @return the operation result
     * @throws OperationException
     */
    double execute(int op1,int op2) throws OperationException;
}
