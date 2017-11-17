package fr.istic.vv.report;

/**
 * Report describes informations from execution of test classes with a mutated
 * class
 */
public class Report {

	private boolean isMutantAlive;

	private String mutationDescription;

	public Report(boolean isMutantAlive, String mutationDescription) {
		super();
		this.isMutantAlive = isMutantAlive;
		this.mutationDescription = mutationDescription;
	}

	/**
	 * @return the isMutantAlive
	 */
	public boolean isMutantAlive() {
		return isMutantAlive;
	}

	/**
	 * @param isMutantAlive
	 *            the isMutantAlive to set
	 */
	public void setMutantAlive(boolean isMutantAlive) {
		this.isMutantAlive = isMutantAlive;
	}

	/**
	 * @return the mutationDescription
	 */
	public String getMutationDescription() {
		return mutationDescription;
	}

	/**
	 * @param mutationDescription
	 *            the mutationDescription to set
	 */
	public void setMutationDescription(String mutationDescription) {
		this.mutationDescription = mutationDescription;
	}

}
