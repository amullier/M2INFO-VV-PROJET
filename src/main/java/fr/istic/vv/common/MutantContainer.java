package fr.istic.vv.common;

/**
 * MutantContainer contains all informations about Mutant Object such as
 * modification description, mutated class name
 */
public interface MutantContainer {
	
	public enum MutantType {
	    ADDITION("+ is replaced by -"),
		SUBTRACTION("- is replaced by +"),
		DIVISION("/ is replaced by *"),
		MULTIPLICATION("* is replaced by /"),
		CONDITION_EQ("ifeq is replaced by ifneq"),
		CONDITION_NEQ("ifNeq is replaced by ifeq");

		private final String text;

		/**
		 * @param text
		 */
		private MutantType(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
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
	public String getMutatedClass();

	/**
	 * set the mutated class to test
	 * 
	 * @param mutatedClass
	 */
	public void setMutatedClass(String mutatedClass);
}
