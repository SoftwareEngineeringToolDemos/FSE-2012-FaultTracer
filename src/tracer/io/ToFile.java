package tracer.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tracer.callgraph.parser.Byte2Source;

public class ToFile {
	public static BufferedWriter writer;
	public static String path="D:\\log";

	public static void write(String toWrite, String path) throws IOException {
		toWrite=Byte2Source.run(toWrite);
		File log = new File(path);
		FileWriter w = new FileWriter(log, true);
		writer = new BufferedWriter(w);
		writer.write(toWrite);
		writer.newLine();
		writer.flush();
		writer.close();
	}
	public static void writeTag(String toWrite,String path) throws IOException {
		File log = new File(path);
		FileWriter w = new FileWriter(log, true);
		writer = new BufferedWriter(w);
		writer.write("<"+toWrite+">");
		writer.flush();
		writer.close();
	}

}
