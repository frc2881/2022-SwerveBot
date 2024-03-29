// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Feeder.frontCargoFeeder;
import static frc.robot.Constants.Feeder.rearCargoFeeder;
import static frc.robot.Constants.Feeder.defaultSpeed;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
  private final CANSparkMax m_frontRoller;
  private final CANSparkMax m_rearRoller;
  private final DigitalInput m_beamBreak = new DigitalInput(0);

  /** Creates a new Feeder. */
  public Feeder() {
    m_frontRoller = new CANSparkMax(frontCargoFeeder, MotorType.kBrushless);
    m_rearRoller = new CANSparkMax(rearCargoFeeder, MotorType.kBrushless);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * 
   * @param state
   * Put "HOLD" for holding a ball.
   * Put "FEED" for feeding a ball into the flywheel.
   * Put "STOP" for stopping the feeder.
   */
  public void run(String state) {
    if(state == "HOLD"){
      m_frontRoller.set(defaultSpeed);
      m_rearRoller.set(defaultSpeed);
    }
    else if(state == "FEED"){
      m_frontRoller.set(defaultSpeed);
      m_rearRoller.set(-defaultSpeed);
    }
  }

  public void stop() {
    m_frontRoller.set(0);
    m_rearRoller.set(0);
  }

  public boolean isBall(){
    if(m_beamBreak.get() == false){
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addBooleanProperty("Cargo Present", () -> !m_beamBreak.get(), null);
  }
}
