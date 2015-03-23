package tracer.callgraph.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import tracer.Activator;

public class ChooseTestView extends FilteredItemsSelectionDialog {
	private static final String DIALOG_SETTINGS = "FilteredResourcesSelectionDialogExampleSettings";
	private static ArrayList<String> resources = new ArrayList<String>();

	public ChooseTestView(Shell shell, boolean multi) {
		super(shell, multi);
		setTitle("Choose a test:");
		setSelectionHistory(new ResourceSelectionHistory());
	}

	public void initialize(Set<String> set) {
		for(String s: set)
		resources.add(s);
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings()
				.getSection(DIALOG_SETTINGS);
		if (settings == null) {
			settings = Activator.getDefault().getDialogSettings()
					.addNewSection(DIALOG_SETTINGS);
		}
		return settings;

	}

	@Override
	protected IStatus validateItem(Object item) {
		// TODO Auto-generated method stub

		System.out.println("item:" + item.toString());
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {
			public boolean matchItem(Object item) {
				return matches(item.toString());
			}

			public boolean isConsistentItem(Object item) {
				return true;
			}
		};

	}

	@Override
	protected Comparator getItemsComparator() {
		return new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		};

	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		progressMonitor.beginTask("Searching", resources.size()); //$NON-NLS-1$
		for (Iterator iter = resources.iterator(); iter.hasNext();) {
			contentProvider.add(iter.next(), itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();

	}

	@Override
	public String getElementName(Object item) {
		// TODO Auto-generated method stub
		return item.toString();
	}

	private class ResourceSelectionHistory extends SelectionHistory {

		@Override
		protected Object restoreItemFromMemento(IMemento memento) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void storeItemToMemento(Object item, IMemento memento) {
			// TODO Auto-generated method stub

		}
	}

}
