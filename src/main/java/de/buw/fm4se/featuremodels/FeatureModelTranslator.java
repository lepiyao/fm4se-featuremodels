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
        System.out.println("Constraint Statement = "+listConstraintStatement.size());
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
      System.out.println("totalStatement ==> "+totalStatement);
      
    }else{
      System.out.println("No Child");
      totalStatement = fm.getRoot().getName();
    }

    System.out.println("END==========================================END");
    return totalStatement;
    // return "a & !a";
  }

  public static List<String> checkChild(Feature feature){
    List<String> listStatement = new ArrayList<>();
    listStatement.add(feature.getName());
    checkIsMandatory(feature, listStatement);
    checkGroupKind(feature, listStatement);
    // check how many children + mandatory + and GroupKind
    /*for(int i = 0; i<feature.getChildren().size(); i++ ){
      
      // Check if it mandatory or not mandatory
      if(feature.getChildren().get(i).isMandatory() == true){
        System.out.println("==========================================");
        System.out.println("This Children is mandatory");
        // (child) -> (root) & (root) -> (child)
        String stateM ="("+ feature.getChildren().get(i).getName() + " -> " + feature.getName() + ") & (" + feature.getName()+ " -> " + feature.getChildren().get(i).getName() +")"; 
        System.out.println("state M = " + stateM);
        listStatement.add(stateM);
      } else{
        System.out.println("==========================================");
        System.out.println("This Children is not mandatory");
        // (child) -> (root)
        String stateNM = feature.getChildren().get(i).getName() + " -> " + feature.getName();
        System.out.println("state NM = " + stateNM);
        listStatement.add(stateNM);
      }
      // =======================================================================
      
      // Check if it OR, XOR, NONE
      if(feature.getChildren().get(i).getChildGroupKind().equals(GroupKind.XOR)){
        System.out.println("==========================================");
        System.out.println("This Children is XOR");
        // XOR mode 
        // (root) -> (child1 & !child2 | !child1 & child2)
        String head = feature.getChildren().get(i).getName();
        String body = "";
        List<String> list2ndChild = new ArrayList<>();
        // this is quite hard if we have more than 2 possibilities
        for(int j =0; j<feature.getChildren().get(i).getChildren().size();j++){
          System.out.println("Children of the children = " + feature.getChildren().get(i).getChildren().get(j).getName());
          list2ndChild.add(feature.getChildren().get(i).getChildren().get(j).getName());
        }

        body =  list2ndChild.get(0) + " & !" + list2ndChild.get(1) + " | !" + list2ndChild.get(0) + " & " + list2ndChild.get(1) ;
        
        String stateXOR = head + " -> " + body;
        listStatement.add(stateXOR);
        System.out.println("State XOR = " + stateXOR);

      } else if(feature.getChildren().get(i).getChildGroupKind().equals(GroupKind.OR)){
        System.out.println("==========================================");
        System.out.println("This Children is OR");
        // OR mode
        // (root) -> (child1 | child2 |.... | childN)
        String head = feature.getChildren().get(i).getName();
        String body = "";

        for(int j =0; j<feature.getChildren().get(i).getChildren().size();j++){
          // set to string directly
          if(j == feature.getChildren().get(i).getChildren().size() - 1){
            body += feature.getChildren().get(i).getChildren().get(j).getName() + " ";
          }else{
            body += feature.getChildren().get(i).getChildren().get(j).getName() + " | " ;
          }
        }

        String stateOR = head + " -> " + body;
        listStatement.add(stateOR);
        System.out.println("State OR = " + stateOR);
      } else{
        System.out.println("==========================================");
        System.out.println("This Children is NONE");
        // NONE mode
      }
      // =======================================================================

    }*/
    return listStatement;
  }

  public static void checkIsMandatory(Feature feature, List<String> listStatement){
    // Check if it mandatory or not mandatory
    String head = feature.getName();

    for (Feature f : feature.getChildren()){
      if(f.isMandatory() == true){
        System.out.println("==========================================");
        System.out.println("This Children is mandatory");
        // (child) -> (root) & (root) -> (child)
        String stateM ="("+ f.getName() + " -> " + head + ") & (" + head+ " -> " + f.getName() +")"; 
        System.out.println("state M = " + stateM);
        listStatement.add(stateM);
      } else{
        System.out.println("==========================================");
        System.out.println("This Children is not mandatory");
        // (child) -> (root)
        String stateNM = f.getName() + " -> " + head;
        System.out.println("state NM = " + stateNM);
        listStatement.add(stateNM);
      }

      checkIsMandatory(f, listStatement);
    }
   
    System.out.println("============================check mandatory done============================");
    System.out.println("END");
  }

  public static void checkGroupKind(Feature feature, List<String> listStatement){
    String head = feature.getName();

    // Check if its XOR or NONE
    if (feature.getChildGroupKind().equals(GroupKind.XOR)){
      System.out.println("This " + feature.getName() + " has XOR");


    } else if (feature.getChildGroupKind().equals(GroupKind.OR)){
      System.out.println("==========================================");
      System.out.println("This " + feature.getName() + " has OR");
      System.out.println("This Children is OR");
      // OR mode
      // (root) -> (child1 | child2 |.... | childN)
      String body = "";
      for (Feature f : feature.getChildren()){
        if(f.equals(feature.getChildren().get(feature.getChildren().size()-1))){
          body += f.getName() + " ";
        }else{
          body += f.getName() + " | " ;
        }

        checkGroupKind(f,listStatement);
      }
      String stateOR = head + " -> " + body;
      listStatement.add(stateOR);
      System.out.println("State OR = " + stateOR);
      
    } else{
      System.out.println("This " + feature.getName() + " has NONE");
    }

    for (Feature f : feature.getChildren()){
      System.out.println("groupkind feature names = " + f.getName() + " - " + f.getChildGroupKind());
      checkGroupKind(f,listStatement);
    }

    // Check if it OR, XOR, NONE
    //   if(feature.getChildren().get(i).getChildGroupKind().equals(GroupKind.XOR)){
    //     System.out.println("==========================================");
    //     System.out.println("This Children is XOR");
    //     // XOR mode 
    //     // (root) -> (child1 & !child2 | !child1 & child2)
    //     String head = feature.getChildren().get(i).getName();
    //     String body = "";
    //     List<String> list2ndChild = new ArrayList<>();
    //     // this is quite hard if we have more than 2 possibilities
    //     for(int j =0; j<feature.getChildren().get(i).getChildren().size();j++){
    //       System.out.println("Children of the children = " + feature.getChildren().get(i).getChildren().get(j).getName());
    //       list2ndChild.add(feature.getChildren().get(i).getChildren().get(j).getName());
    //     }

    //     body =  list2ndChild.get(0) + " & !" + list2ndChild.get(1) + " | !" + list2ndChild.get(0) + " & " + list2ndChild.get(1) ;
        
    //     String stateXOR = head + " -> " + body;
    //     listStatement.add(stateXOR);
    //     System.out.println("State XOR = " + stateXOR);

    //   } 
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
      System.out.println("==========================================");
      System.out.println("Constaint= " + constraintStatement);
      listConstraintStatement.add(constraintStatement);
    }

    return listConstraintStatement;
  }
}
