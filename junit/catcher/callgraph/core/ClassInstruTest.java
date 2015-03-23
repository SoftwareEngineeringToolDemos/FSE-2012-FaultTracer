package catcher.callgraph.core;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import tracer.callgraph.instr.InstruClassAdapter;


public class ClassInstruTest {

	@Test
	public void test1() throws IOException {
		ClassWriter cw = new ClassWriter(0);
		ClassReader cr = new ClassReader("java.lang.Runnable");
		InstruClassAdapter ia = new InstruClassAdapter(cw);
		cr.accept(ia, 0);
		byte[] b = cw.toByteArray();
		assertEquals(b.length,90);

	}


}
