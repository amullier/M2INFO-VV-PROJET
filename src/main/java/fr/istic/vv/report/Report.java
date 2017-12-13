package fr.istic.vv.report;

import fr.istic.vv.common.MutantContainer;

/**
 * Report describes informations from execution of test classes with a mutated
 * class
 */
public class Report {

	private boolean isMutantAlive;

	private MutantContainer mutantContainer;

	public Report(boolean isMutantAlive, MutantContainer mutantContainer) {
		super();
		this.isMutantAlive = isMutantAlive;
		this.mutantContainer = mutantContainer;
	}

	public boolean isMutantAlive() {
		return isMutantAlive;
	}

	public void setMutantAlive(boolean isMutantAlive) {
		this.isMutantAlive = isMutantAlive;
	}

	public MutantContainer getMutantContainer() {
		return mutantContainer;
	}

	public void setMutantContainer(MutantContainer mutantContainer) {
		this.mutantContainer = mutantContainer;
	}
}
