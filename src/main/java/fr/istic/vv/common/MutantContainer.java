package fr.istic.vv.common;

/**
 * MutantContainer contains all informations about Mutant Object such as
 * modification description, mutated class name
 */
public interface MutantContainer {
	
	public enum MutantType {
	    ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION 
	}
	/**
	 * 
	 * @return
	 */
	public MutantType getMutationType();
	
	/**
	 * 
	 * @param mutantType
	 */
	public void setMutationType(MutantType mutantType);

	/**
	 * Gets the mutation description
	 */
	public String getMutationMethod();

	/**
	 * 
	 * @param methodName
	 */
	public void setMutationMethod(String methodName);

	/**
	 * Gets the mutated class
	 */
	public Class<?> getMutatedClass();

	/**
	 * set the mutated class to test
	 * 
	 * @param mutatedClass
	 */
	public void setMutatedClass(Class<?> mutatedClass);
}
