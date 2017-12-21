package fr.istic.vv.common;

/**
 * MutantContainer contains all informations about Mutant Object such as
 * modification description, mutated class name
 */
public interface MutantContainer {
	
	enum MutantType {
	    ADDITION("+ is replaced by -"),
		SUBTRACTION("- is replaced by +"),
		DIVISION("/ is replaced by *"),
		MULTIPLICATION("* is replaced by /"),
		CONDITION_EQ("ifeq is replaced by ifneq"),
		CONDITION_NEQ("ifneq is replaced by ifeq");

		private final String text;

		/**
		 * Private constructor for String description of MutantType enumeration
		 * @param text
		 */
		MutantType(final String text) {
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
	MutantType getMutationType();
	
	/**
	 * 
	 * @param mutantType
	 */
	void setMutationType(MutantType mutantType);

	/**
	 * Gets the mutation description
	 */
	String getMutationMethod();

	/**
	 * 
	 * @param methodName
	 */
	void setMutationMethod(String methodName);

	/**
	 * Gets the mutated class
	 */
	String getMutatedClass();

	/**
	 * set the mutated class to test
	 * 
	 * @param mutatedClass
	 */
	void setMutatedClass(String mutatedClass);
}
