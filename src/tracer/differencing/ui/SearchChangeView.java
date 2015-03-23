package tracer.differencing.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
import tracer.differencing.core.data.AtomicChange;

public class SearchChangeView extends FilteredItemsSelectionDialog {
	private static final String DIALOG_SETTINGS = "FilteredResourcesSelectionDialogExampleSettings";
	private static ArrayList<GraphNode> resources = new ArrayList<GraphNode>();

	public SearchChangeView(Shell shell, boolean multi) {
		super(shell, multi);
		setTitle("Search atomic changes:");
		setSelectionHistory(new ResourceSelectionHistory());
	}

	public void initialize(List<GraphNode> list) {
		resources.addAll(list);
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
