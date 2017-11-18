package fr.istic.vv.mutator.projet;

import java.util.ArrayList;
import java.util.List;

public class MutateClass {
	private String name;
	private List<MutateMethod> methods;
	
	public MutateClass(String name) {
		this.setName(name);
		this.methods = new ArrayList<MutateMethod>();
	}
	
	/**
	 * 
	 * @return class name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set the class name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * add methods
	 * @param method
	 */
	public void addMethods(MutateMethod method) {
		this.methods.add(method);
	}
	
	/**
	 * 
	 * @param name
	 * @return a MutateMethod by name
	 */
	public MutateMethod getMethod(String name) {
		for(MutateMethod method : methods) {
			if(method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<MutateMethod> getMethods() {
		return this.methods;
	}
}
