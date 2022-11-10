package de.buw.fm4se.featuremodels;

import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.GroupKind;

/**
 * class to create some simple feature models
 *
 */
public class ExampleFmCreator {

  public static FeatureModel getEshopFm() {
    FeatureModel m = new FeatureModel();
    m.setRoot(new Feature("eshop"));
    return m;
  }

  public static FeatureModel getSimpleFm() {
    FeatureModel m = new FeatureModel();

    Feature car = new Feature("car");
    m.setRoot(car);

    Feature motor = car.addChild("motor", true);
    
    Feature important1 = car.addChild("important1", true);
    important1.addChild("1stChildImportant1", true);
    important1.addChild("2ndChildImportant1", false);

    motor.setChildGroupKind(GroupKind.XOR);
    motor.addChild("gasoline", false);
    motor.addChild("gear", false);
    Feature electric = motor.addChild("electric", false);

    Feature comfort = car.addChild("comfort", false);
    comfort.setChildGroupKind(GroupKind.OR);
    Feature heating = comfort.addChild("heating", false);
    Feature entertainment = comfort.addChild("entertainment", false);

    Feature music = entertainment.addChild("radio", true);
    entertainment.addChild("tv", false);
    
    music.setChildGroupKind(GroupKind.OR);
    music.addChild("Rock", false);
    music.addChild("Jazz", false);
    music.addChild("Pop", false);
    music.addChild("RnB", false);
    music.addChild("Hip-Hop", false);


    m.addConstraint(new CrossTreeConstraint(electric, CrossTreeConstraint.Kind.REQUIRES, heating));

    return m;
  }

  /**
   * constructs a FM where the mandatory child excludes the root
   * @return
   */
  public static FeatureModel getBadFm() {
    FeatureModel m = new FeatureModel();

    Feature car = new Feature("car");
    m.setRoot(car);

    Feature motor = car.addChild("motor", true);

    m.addConstraint(new CrossTreeConstraint(motor, CrossTreeConstraint.Kind.EXCLUDES, car));

    return m;
  }

}
