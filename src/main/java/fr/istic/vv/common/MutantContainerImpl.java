package fr.istic.vv.common;

public class MutantContainerImpl implements MutantContainer{
	
	private Class<?> mutantClass;
	private String description;

	@Override
	public String getMutationDescription() {
		return description;
	}

	@Override
	public void setMutationDescription(String description) {
		this.description = description;
	}

	@Override
	public Class<?> getMutatedClass() {
		return mutantClass;
	}

	@Override
	public void setMutatedClass(Class<?> mutatedClass) {
		this.mutantClass = mutatedClass;		
	}

}
