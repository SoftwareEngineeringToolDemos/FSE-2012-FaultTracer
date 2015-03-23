package tracer.differencing.core.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;

class CompareItem implements IStreamContentAccessor, ITypedElement,
		IModificationDate {
	private String contents, name;

	CompareItem(String name, String contents) {
		this.name = name;
		this.contents = contents;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(contents.getBytes());
	}

	public Image getImage() {
		return null;
	}

	public String getName() {
		return name;
	}

	public String getString() {
		return contents;
	}

	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}

	@Override
	public long getModificationDate() {
		// TODO Auto-generated method stub
		return 0;
	}
}
