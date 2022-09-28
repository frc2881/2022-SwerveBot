// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Launcher.adjustableHood;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Hood Motor 18 tooth, gear for the hood 270 tooth - setpostitionconversionfactor
//every rotation of the motor the 

public class Hood extends SubsystemBase {
  private final CANSparkMax m_hood;
  private final RelativeEncoder m_hoodEncoder;
  private final SparkMaxPIDController m_hoodPID;
  private final double degreesPerRotation = 24; //Teeth on driving gear devided by teeth on driven gear multiplied by 360 degres (18/270*360=24)
  private final double p = 1;
  
  public Hood() {
    m_hood = new CANSparkMax(adjustableHood, MotorType.kBrushless);
    m_hood.restoreFactoryDefaults();

    m_hoodPID = m_hood.getPIDController();
    m_hoodPID.setP(p);

    m_hoodEncoder = m_hood.getEncoder();
    m_hoodEncoder.setPositionConversionFactor(degreesPerRotation);
    m_hoodEncoder.setVelocityConversionFactor(degreesPerRotation / 60);
  
  }

  @Override
  public void periodic() {
    

    // This method will be called once per scheduler run
  }

  public void setTarget(double goToAngle) {
      m_hoodPID.setReference(goToAngle, CANSparkMax.ControlType.kPosition);
  }
  
  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("hood", m_hoodEncoder::getPosition, null);  // 1.21 from launchpad  
  }
}
