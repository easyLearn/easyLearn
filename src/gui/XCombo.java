package gui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Simple erweiterte Combo (mit Vorsicht verwenden), da sehr naiv programmiert.
 * @author Tobias Wolf
 *
 * @param <T>
 */
public class XCombo<T> {

	private Map<String, Object> items = new HashMap<String, Object>();
	private Combo combo;
	private int selected;
	private String selectedString;
	
	public XCombo(Composite arg0, int arg1) {
		combo = new Combo(arg0, arg1);
		addSelectionListener();
	}

	public void addItem(T item) {
		combo.add(item.toString());
		items.put(item.toString(), item);
	}
	
	public void select(int index) {
		combo.select(index);
	}
	
	public T getSelectedItem() {
		return (T) items.get(selectedString);
	}
	
	private void addSelectionListener() {
		combo.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selected = combo.getSelectionIndex();
				selectedString = combo.getItem(selected);
			}
		});
	}
}
