package tracer.differencing.core.diff;

import org.eclipse.jdt.core.JavaModelException;

import tracer.differencing.core.pairs.ComUnitPair;
import tracer.differencing.core.pairs.FieldPair;
import tracer.differencing.core.pairs.MethodPair;
import tracer.differencing.core.pairs.PkgPair;
import tracer.differencing.core.pairs.PkgRootPair;
import tracer.differencing.core.pairs.ProjectPair;
import tracer.differencing.core.pairs.TypePair;



public interface IDiffVisitor {

	public void visit(ProjectPair projPair) throws JavaModelException;
	public void visit(PkgRootPair pkgRootPair)throws JavaModelException;
	public void visit(PkgPair pkgPair)throws JavaModelException;
	public void visit(ComUnitPair comUnitPair)throws JavaModelException;
	public void visit(TypePair typePair)throws JavaModelException;
	public void visit(MethodPair methodPair)throws JavaModelException;
	public void visit(FieldPair fieldPair) throws JavaModelException;
	
}
