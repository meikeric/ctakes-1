package org.apach.ctakes.temporal.ae;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory;
import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory.CopyNPChunksToLookupWindowAnnotations;
import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory.RemoveEnclosedLookupWindows;
import org.apache.ctakes.dependency.parser.ae.ClearNLPDependencyParserAE;
import org.apache.ctakes.dictionary.lookup.ae.UmlsDictionaryLookupAnnotator;
import org.apache.ctakes.temporal.ae.BackwardsTimeAnnotator;
import org.apache.ctakes.temporal.ae.EventAnnotator;
import org.apache.ctakes.typesystem.type.textsem.EventMention;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.ctakes.typesystem.type.textsem.TimeMention;
import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.cleartk.classifier.CleartkAnnotator;
import org.cleartk.classifier.jar.GenericJarClassifierFactory;
import org.junit.Test;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.util.JCasUtil;

public class EventAnnotatorTest {

	// LOG4J logger based on class name
	private Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@Test
	public void testPipeline() throws UIMAException, IOException {
		
		String note = "The patient is a 55-year-old man referred by Dr. Good for recently diagnosed colorectal cancer.  "
				+ "The patient was well till 6 months ago, when he started having a little blood with stool.";
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(note);
  
		//Get the default pipeline with umls dictionary lookup
	    AggregateBuilder builder = new AggregateBuilder();
	    builder.add(ClinicalPipelineFactory.getTokenProcessingPipeline());
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(CopyNPChunksToLookupWindowAnnotations.class));
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(RemoveEnclosedLookupWindows.class));
	    builder.add(UmlsDictionaryLookupAnnotator.createAnnotatorDescription());
	    builder.add(ClearNLPDependencyParserAE.createAnnotatorDescription());
		
		//Add EventAnnotator
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	    		EventAnnotator.class,
	            CleartkAnnotator.PARAM_IS_TRAINING,
	            false,
	            GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH,
	            "/org/apache/ctakes/temporal/ae/eventannotator/model.jar"));
	    
	    SimplePipeline.runPipeline(jcas, builder.createAggregateDescription());

	    Collection<EventMention> mentions = JCasUtil.select(jcas, EventMention.class);

	    ArrayList<String> temp = new ArrayList<>();
	    for(IdentifiedAnnotation entity : mentions){
	    	LOGGER.info("Entity: " + entity.getCoveredText());
	    	temp.add(entity.getCoveredText());
	    }
	   	assertEquals(7, temp.size());	    
	    assertTrue(temp.contains("old"));
	    assertTrue(temp.contains("referred"));	    
	    assertTrue(temp.contains("cancer"));
	    assertTrue(temp.contains("till"));
	    assertTrue(temp.contains("blood"));
	    assertTrue(temp.contains("stool"));
	}
}
