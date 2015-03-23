package tracer.faultlocalization.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import tracer.faultlocalization.core.RankingEdit;

public class RankingEditLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RankingEdit) {
			RankingEdit edit = (RankingEdit) element;
			if (columnIndex == 0)
				return edit.getType();
			else if (columnIndex == 1)
				return edit.getElement();
			else if (columnIndex == 2)
				return edit.getSusp_val()+"";
			else if (columnIndex == 3)
				return edit.getHeu_val()+"";
		}
		return null;
	}

}
