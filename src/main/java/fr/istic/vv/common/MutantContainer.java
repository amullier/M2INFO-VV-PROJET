package fr.istic.vv.common;

/**
 * MutantContainer contains all informations about Mutant Object such as
 * modification description, mutated class name
 */
public interface MutantContainer {

	// TODO add mutant object

	/**
	 * Gets the mutation description
	 */
	public String getMutationDescription();

	/**
	 * Gets the mutated class name
	 */
	public Class<?> getMutatedClassName();
	
	/**
	 * set the mutated class to test
	 * @param cls
	 */
	public void setMutatedClassName(Class<?> cls);
}
