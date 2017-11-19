package fr.istic.vv.common;

public class MutantContainerImpl implements MutantContainer{
	
	private Class<?> mutantClass;
	private String methodName;
	private MutantType mutantType;

	@Override
	public Class<?> getMutatedClass() {
		return mutantClass;
	}

	@Override
	public void setMutatedClass(Class<?> mutatedClass) {
		this.mutantClass = mutatedClass;		
	}

	@Override
	public MutantType getMutationType() {
		return mutantType;
	}

	@Override
	public void setMutationType(MutantType mutantType) {
		this.mutantType = mutantType;		
	}

	@Override
	public String getMutationMethod() {
		return methodName;
	}

	@Override
	public void setMutationMethod(String methodName) {
		this.methodName = methodName;
	}

}
