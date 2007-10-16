/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.tests.statushandlers.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.tests.statushandlers.views.StatusHandlingComponent.DisplayedItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * This component is responsible for testing statuses.
 */
public class StatusHandlingRunnable extends TestBedRunnable {

	public static final String STATUS_HANDLING_TYPE = "statusHandling";

	/**
	 * @since 3.3
	 * TODO: properties for statusAdapter
	 */
	private final class StatusHandlingPropertySource implements
			IPropertySource {

		private final IPropertyDescriptor[] PROPERTY_DESCRIPTOR = new IPropertyDescriptor[] {
				new PropertyDescriptor(STATUS, "Status"),
				new PropertyDescriptor(FLAG, "Handle flag"),
				new PropertyDescriptor(WRAPPED, "Wrapped in StatusAdapter"),
				new PropertyDescriptor(EXPLANATION, "Explanation"),
				new PropertyDescriptor(ACTION, "Action")};

		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			PropertyDescriptor[] descriptors = new PropertyDescriptor[items.size()];
			
			for(int i = 0; i < items.size(); i++){
				descriptors[i] = new PropertyDescriptor("status" + i, "Status Handling #" + i);
			}
			
			return descriptors;
		}

		public Object getPropertyValue(Object id) {
			String stringId = (String) id;
			stringId = stringId.replaceAll("status", "");
			final int index = Integer.parseInt(stringId);
			//TODO: consider nesting class here
			return new IPropertySource(){

				public Object getEditableValue() {
					return null;
				}

				public IPropertyDescriptor[] getPropertyDescriptors() {
					return PROPERTY_DESCRIPTOR;
				}

				public Object getPropertyValue(Object id) {
					DisplayedItem di = (DisplayedItem) items.get(index);
					
					if(id.equals(STATUS)){
						return di.getStatus();
					}
					if(id.equals(FLAG)){
						String result = "";
						if((di.getHint() & 2) > 0){
							result += "SHOW";
						}
						if((di.getHint() & 1) > 0){
							result += "LOG";
						}
						if((di.getHint() & 4) > 0){
							result += "BLOCK";
						}
						if(result.equals("")){
							result += "NONE";
						}
						return result;
					}
					if(id.equals(WRAPPED)){
						return "" + di.isWrapped();
					}
					if(id.equals(EXPLANATION)){
						return di.getExplanation();
					}
					if(id.equals(ACTION)){
						return di.getAction();
					}
					return null;
				}

				public boolean isPropertySet(Object id) { return true; }

				public void resetPropertyValue(Object id) {}

				public void setPropertyValue(Object id, Object value) { }
			};
		}

		public boolean isPropertySet(Object id) {
			return false;
		}

		public void resetPropertyValue(Object id) {
			
		}

		public void setPropertyValue(Object id, Object value) {
			
		}
	}

	private static final String STATUS = "status";
	private static final String FLAG = "handleFlag";
	private static final String WRAPPED = "wrapped";
	private static final String EXPLANATION = "explanation";
	private static final String ACTION = "action";
	List items;
	
	
	/**
	 * 
	 * @param statusItems
	 */
	public StatusHandlingRunnable(List statusItems) {
		items = statusItems;
	}

	StatusHandlingRunnable() {
		items = new ArrayList();
	}

	public void run() {
		for (Iterator it = items.iterator(); it.hasNext();) {
			DisplayedItem di = (DisplayedItem) it.next();
			// see SampleStatusHandler
			// hints and statuses could be easily modified there
			if (di.isWrapped()) {
				StatusAdapter adapter = new StatusAdapter(di.getStatus());
				if (di.getAction() != null) {
					adapter.setProperty(
							StatusAdapter.HINT_PROPERTY, di
									.getAction());
				}
				if (di.getExplanation() != null) {
					adapter.setProperty(StatusAdapter.EXPLANATION_PROPERTY, di
							.getExplanation());
				}
				StatusManager.getManager().handle(adapter, di.getHint());
			} else {
				StatusManager.getManager().handle(di.getStatus(), di.getHint());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.tests.statushandlers.views.TestBedRunnable#toString()
	 */
	public String toString() {
		return "Status Handling";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if(adapter == IPropertySource.class){
			return new StatusHandlingPropertySource();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.tests.statushandlers.views.TestBedRunnable#getTypeId()
	 */
	public String getTypeId() {
		return STATUS_HANDLING_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.tests.statushandlers.views.TestBedRunnable#saveState(org.eclipse.ui.IMemento)
	 */
	protected void saveState(IMemento memento) {
		for(int i = 0; i < items.size(); i++){
			DisplayedItem di = (DisplayedItem) items.get(i);
			di.saveState(memento);
		}
	}

	/**
	 * Creates {@link StatusHandlingRunnable} from the memento.
	 */
	public static TestBedRunnable create(IMemento memento) {
		StatusHandlingRunnable runnable = new StatusHandlingRunnable();
		IMemento children[] = memento.getChildren(DisplayedItem.STATUS);
		for(int i = 0; i < children.length; i++){
			runnable.items.add(DisplayedItem.create(children[i]));
		}
		return runnable;
	}
	
}
