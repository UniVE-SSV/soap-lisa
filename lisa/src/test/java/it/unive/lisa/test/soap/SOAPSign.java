package it.unive.lisa.test.soap;

import it.unive.lisa.AnalysisException;
import it.unive.lisa.LiSA;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.HeapDomain;
import it.unive.lisa.analysis.impl.numeric.Sign;
import it.unive.lisa.program.Program;
import it.unive.lisa.test.imp.IMPFrontend;
import it.unive.lisa.test.imp.ParsingException;
import org.junit.Test;

public class SOAPSign {

	@Test
	public void signAnalysis() throws ParsingException, AnalysisException {
		Program program = IMPFrontend.processFile("imp-testcases/soap/example.imp");
		LiSA lisa = new LiSA();
		lisa.setProgram(program);
		lisa.setAbstractState(LiSAFactory.getDefaultFor(
				AbstractState.class,
				LiSAFactory.getDefaultFor(HeapDomain.class),
				new Sign()));
		lisa.setDumpAnalysis(true);
		lisa.setWorkdir("soap-output/sign");
		lisa.run();
	}
}
