package it.unive.lisa.test.soap;

import it.unive.lisa.AnalysisException;
import it.unive.lisa.LiSA;
import it.unive.lisa.program.Program;
import it.unive.lisa.test.imp.IMPFrontend;
import it.unive.lisa.test.imp.ParsingException;
import org.junit.Test;

public class SOAPCFG {

	@Test
	public void dumpCFG() throws ParsingException, AnalysisException {
		Program program = IMPFrontend.processFile("imp-testcases/soap/example.imp");
		LiSA lisa = new LiSA();
		lisa.setProgram(program);
		lisa.setDumpCFGs(true);
		lisa.setWorkdir("soap-output/cfg");
		lisa.run();
	}
}
