package it.unive.lisa.test.soap;

import it.unive.lisa.AnalysisException;
import it.unive.lisa.LiSA;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.HeapDomain;
import it.unive.lisa.analysis.dataflow.DefiniteForwardDataflowDomain;
import it.unive.lisa.analysis.dataflow.impl.AvailableExpressions;
import it.unive.lisa.program.Program;
import it.unive.lisa.test.imp.IMPFrontend;
import it.unive.lisa.test.imp.ParsingException;
import org.junit.Test;

public class SOAPAvailableExpressions {

	@Test
	public void availableExpressionsAnalysis() throws ParsingException, AnalysisException {
		Program program = IMPFrontend.processFile("imp-testcases/soap/example.imp");
		LiSA lisa = new LiSA();
		lisa.setProgram(program);
		lisa.setAbstractState(LiSAFactory.getDefaultFor(
				AbstractState.class,
				LiSAFactory.getDefaultFor(HeapDomain.class),
				new DefiniteForwardDataflowDomain<>(new AvailableExpressions())));
		lisa.setDumpAnalysis(true);
		lisa.setWorkdir("soap-output/ae");
		lisa.run();
	}
}
