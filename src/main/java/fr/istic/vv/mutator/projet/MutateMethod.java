package fr.istic.vv.mutator.projet;

import java.util.ArrayList;
import java.util.List;

public class MutateMethod {

	private String name;
	private List<Integer> content;
	
	public MutateMethod(String name) {
		this.name = name;
		this.content = new ArrayList<Integer>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addBytecode(Integer op) {
		this.content.add(op);
	}
	
	public List<Integer> getContent() {
		return content;
	}

	public void setContent(List<Integer> content) {
		this.content = content;
	}
	
	public boolean contains(Integer bytecode) {
		return content.contains(bytecode);
	}
}
