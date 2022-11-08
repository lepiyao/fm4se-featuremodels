package de.buw.fm4se.featuremodels;

import java.util.ArrayList;
import java.util.List;

import de.buw.fm4se.featuremodels.exec.LimbooleExecutor;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;

/**
 * This code needs to be implemented by translating FMs to input for Limboole
 * and interpreting the output
 *
 */
public class FeatureModelAnalyzer {

  public static boolean checkConsistent(FeatureModel fm) {
    String formula = FeatureModelTranslator.translateToFormula(fm);
    
    String result;
    try {
      result = LimbooleExecutor.runLimboole(formula, true);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    if (result.contains("UNSATISFIABLE")) {
      return false;
    }
    return true;
  }

  public static List<String> deadFeatureNames(FeatureModel fm) {
    List<String> deadFeatures = new ArrayList<>();

    // TODO check for dead features

    return deadFeatures;
  }

  public static List<String> mandatoryFeatureNames(FeatureModel fm) {
    List<String> mandatoryFeatures = new ArrayList<>();

    mandatoryFeatures.add(fm.getRoot().getName());
    checkMandatoryFM(fm.getRoot(), fm.getRoot().getChildren(), mandatoryFeatures, fm.getRoot().getName());

    return mandatoryFeatures;
  }

  public static void checkMandatoryFM(Feature feature, List<Feature> listChild, List<String> mandatoryFeatures, String rootName){
    for (Feature child : feature.getChildren()){
      if(feature.getName() == rootName){
        if(child.isMandatory()){
          mandatoryFeatures.add(child.getName());
        }
      }else{
        if(child.isMandatory() && feature.isMandatory()){
          mandatoryFeatures.add(child.getName());
        }
      }
    }

    for (Feature f : feature.getChildren()){
      checkMandatoryFM(f, f.getChildren(), mandatoryFeatures, rootName);
    }
  }

}
