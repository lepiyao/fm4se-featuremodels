package de.buw.fm4se.featuremodels;

import java.util.ArrayList;
import java.util.List;

import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.GroupKind;

public class FeatureModelTranslator {
  public static String translateToFormula(FeatureModel fm) {
    
    // TODO implement a real translation
    List<String> listStatement = new ArrayList<>();
    List<String> listConstraintStatement = new ArrayList<>();

    String totalStatement = "";
    String countCloseBracket ="";
    String textTemp = "";
    String constraint = "";

    if (fm.getRoot().getChildren().size() != 0){
      listStatement = checkChild(fm.getRoot());
      listConstraintStatement = checkConstraint(fm);

      for(int i = 0; i<listConstraintStatement.size(); i++){
        constraint = "( " + listConstraintStatement.get(i) + " )"; 

        for(int j = 0; j<listStatement.size(); j++){
          countCloseBracket+= ")";
        }
  
        for(int k = 0; k<listStatement.size(); k++){
          String temp = "("+listStatement.get(k)+")";
          textTemp += "( " + temp + " & " ;
        }
        totalStatement = textTemp + constraint + countCloseBracket;
      }
      System.out.println("totalStatement ==> ");
      System.out.println(totalStatement);
      
    }else{
      System.out.println("No Child");
      totalStatement = fm.getRoot().getName();
    }
    return totalStatement;
  }

  public static List<String> checkChild(Feature feature){
    List<String> listStatement = new ArrayList<>();
    
    listStatement.add(feature.getName());
    checkGroupKind(feature, listStatement);
    
    return listStatement;
  }

  public static void checkGroupKind(Feature feature, List<String> listStatement){
    String head = feature.getName();

    // Check if its XOR or NONE
    if (feature.getChildGroupKind().equals(GroupKind.XOR)){
      // XOR mode===================================================
      // Since XOR is rarely used for more than 2
      // But it is better for me to throw an exception and break the process if its more than 2
      // because it might be a bit confusing and a lot to count/write all of them
      String body = "";
      List<String> list2ndChild = new ArrayList<>();
      for(int j =0; j<feature.getChildren().size();j++){
        list2ndChild.add(feature.getChildren().get(j).getName());
      }

      body =  list2ndChild.get(0) + " & !" + list2ndChild.get(1) + " | !" + list2ndChild.get(0) + " & " + list2ndChild.get(1) ;
      
      String stateXOR = head + " -> " + body;
      listStatement.add(stateXOR);

    } else if (feature.getChildGroupKind().equals(GroupKind.OR)){
      // OR mode===================================================
      String body = "";
      for (Feature f : feature.getChildren()){
        if(f.equals(feature.getChildren().get(feature.getChildren().size()-1))){
          body += f.getName() + " ";
        }else{
          body += f.getName() + " | " ;
        }
      }
      String stateOR = head + " -> " + body;
      listStatement.add(stateOR);
      
    } else{
      // NONE mode===================================================
      for (Feature f : feature.getChildren()) {
        checkIsMandatory(feature, f, listStatement);
      }
    }

    for (Feature f : feature.getChildren()){
      checkGroupKind(f,listStatement);
    }
    
  }

  public static void checkIsMandatory(Feature feature, Feature child, List<String> listStatement){
    // Check if it mandatory or not mandatory
    String head = feature.getName();
    if(child.isMandatory() == true){
      // (child) -> (root) & (root) -> (child)
      String stateM ="("+ child.getName() + " -> " + head + ") & (" + head+ " -> " + child.getName() +")";
      listStatement.add(stateM);
    } else{
      // (child) -> (root)
      String stateNM = child.getName() + " -> " + head;
      listStatement.add(stateNM);
    }
  }

  public static List<String> checkConstraint(FeatureModel featureModel){
    List<String> listConstraintStatement = new ArrayList<>();
    String constraintStatement = "";
    for(int i=0; i<featureModel.getConstraints().size(); i++){
     if(featureModel.getConstraints().get(i).getKind().name() == "REQUIRES"){
        constraintStatement = featureModel.getConstraints().get(i).getLeft().getName() + " -> " + featureModel.getConstraints().get(i).getRight().getName();
      } else{
        constraintStatement = "!"+featureModel.getConstraints().get(i).getLeft().getName() + " | !" + featureModel.getConstraints().get(i).getRight().getName();
      }
      listConstraintStatement.add(constraintStatement);
    }

    return listConstraintStatement;
  }
}
