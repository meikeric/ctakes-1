/*
 * Copyright: (c) 2012  Children's Hospital Boston, Regents of the University of Colorado 
 *
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Steven Bethard
 */

package org.chboston.cnlp.ctakes.relationextractor.ae.features;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.cleartk.classifier.Feature;

import edu.mayo.bmi.uima.core.type.textsem.IdentifiedAnnotation;

/**
 * Define an interface for people to implement feature extractors.
 */
public interface RelationFeaturesExtractor {

  /**
   * Extract features for the pair of named entity mentions.
   * 
   * @param jCas
   *          The JCas containing the two named entity mentions.
   * @param arg1
   *          The first identified annotation in the text.
   * @param arg2
   *          The second identified annotation in the text.
   * @return A list of features indicative of the relation between the named entities
   */
  public List<Feature> extract(JCas jCas, IdentifiedAnnotation arg1, IdentifiedAnnotation arg2)
      throws AnalysisEngineProcessException;
}