package gui.translator;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractBuilder {

	private Composite parent;
	
	public AbstractBuilder(Composite parent) {
		this.parent = parent;
	}

	public abstract void build();
	
	public Composite getParent() {
		return parent;
	}
	
}
