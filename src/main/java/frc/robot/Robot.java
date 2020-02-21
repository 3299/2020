/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.nio.ByteBuffer;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

//import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.util.Color;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  private Spark armMotor = new Spark(6);
  private Talon pistonMotor = new Talon(1);
  private Talon motor3 = new Talon(2);
  private Spark motor4 = new Spark(3);
  private VictorSPX motor1 = new VictorSPX(5);

  private Joystick joy1 = new Joystick(0);
  //private XboxController button = new XboxController(0);
  private Joystick joy2 = new Joystick(1);

  private DigitalInput forwardLimitSwitch = new DigitalInput(2);
  private DigitalInput reverseLimitSwitch = new DigitalInput(1);
  private DigitalInput leftLowerLimitSwitch = new DigitalInput(3);
  private DigitalInput rightLowerLimitSwitch = new DigitalInput(0);
  private DigitalInput leftUpperLimitSwitch = new DigitalInput(5);
  private DigitalInput rightUpperLimitSwitch = new DigitalInput(4);
  static int output = 1;
 
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private ByteBuffer buffer = ByteBuffer.allocate(5);
  @Override
  public void robotInit() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);    

  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    
    double speed = -joy1.getRawAxis(1) * 0.8;
    double turn = joy1.getRawAxis(4) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    motor3.set(left);
    motor4.set(-right);

    double armspeed = joy2.getRawAxis(1);
    double pistonspeed = joy2.getRawAxis(0) * 0.6;
    
   
/*
    motor3.set(-left);
    motor4.set(right);
*/
    if(forwardLimitSwitch.get() == true && pistonspeed < 0){
      pistonMotor.set(0);
    }
    else if(reverseLimitSwitch.get() == true && pistonspeed > 0){
      pistonMotor.set(0);
    }
    else{
      pistonMotor.set(pistonspeed);
    }

      if(leftLowerLimitSwitch.get() == true && rightLowerLimitSwitch.get() == true && armspeed < 0){
        armMotor.set(0);
      }
      else if((leftUpperLimitSwitch.get() == true || rightUpperLimitSwitch.get() == true) && armspeed >0){
        armMotor.set(0);
      }
      else{
      armMotor.set(armspeed);
      }
    
    
      Color detectedColor = m_colorSensor.getColor();

    /**
     * Run the color match algorithm on our detected color
     */
    String colorString;
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

  System.out.println(colorString);
    /*
    if(ColorSensor.read(0x16, 1, buffer)){
      System.out.println("red");
    }
    else if(ColorSensor.read(0x18, 1, buffer)){
      System.out.println("green");
    }
    else if(ColorSensor.read(0x1A, 1, buffer)){
      System.out.println("blue");
    }
    else{
      System.out.println("error");
    }
    */
    /*
    if(button.getXButton() == true){
      if(forwardLimitSwitch.get() == true){
        pistonMotor.set(0);
      }
      else{
      pistonMotor.set(-1);
      }
    }
    else{
      pistonMotor.set(0);
    }

    
    if(button.getYButton() == true){
      if(reverseLimitSwitch.get() == true){
        pistonMotor.set(0);
      }
      else{
      pistonMotor.set(0.5);
      }
    }
    else{
      pistonMotor.set(0);
    }
 
  if(button.getAButton() == true){
    if(leftLowerLimitSwitch.get() == true && rightLowerLimitSwitch.get() == true){
      armMotor.set(0);
    }
    else{
    armMotor.set(-1);
    }
  }  
  else{
    armMotor.set(0);
  }
  if(button.getBButton() == true){
    if(leftUpperLimitSwitch.get() == true || rightUpperLimitSwitch.get() == true)
    {
      armMotor.set(0);
    }
    else{
    armMotor.set(1);
  }  
}
  else{
    armMotor.set(0);
  }

  */
   /*
    
    if(button.getYButton() == true) {
      pistonMotor.set(1);  
      }
    else {
      pistonMotor.set(0);
    }
    */
    /*
  if(forwardLimitSwitch.get() == true){
    output = Math.min(output, 0);
  }
 if(reverseLimitSwitch.get() == true){
    output = Math.max(0,output);
  }
  */
/*

  if(forwardLimitSwitch.get() == true){
  pistonMotor.set(0);
}
else{
  pistonMotor.set(-0.5);
}

if(reverseLimitSwitch.get() == true){
  pistonMotor.set(0);
}
else{
  pistonMotor.set(1);
}

  pistonMotor.set(output);

    int output = joy1.getRawAxis();
    if(forwardLimitSwitch.get()) {
      output = Math.min(output, 0);
    } else if(reverseLimitSwitch.get()) {
      output = Math.max(output, 0);
    }
    pistonMotor.set(output);
    */
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
