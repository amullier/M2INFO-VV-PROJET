package fr.istic.vv.common;

/**
 * MutantContainer contains all informations about Mutant Object such as
 * modification description, mutated class name
 */
public interface MutantContainer {

	/**
	 * Gets the mutation description
	 */
	public String getMutationDescription();

	/**
	 * Gets the mutated class
	 */
	public Class<?> getMutatedClass();

	/**
	 * set the mutated class to test
	 * 
	 * @param cls
	 */
	public void setMutatedClass(Class<?> cls);
}
