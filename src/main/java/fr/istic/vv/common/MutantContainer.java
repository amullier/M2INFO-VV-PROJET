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
	 * Sets the mutation description
	 * 
	 * @param description
	 */
	public void setMutationDescription(String description);

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
