// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Launcher extends SubsystemBase {
    private final CANSparkMax m_mainFlywheel = new CANSparkMax(Constants.Launcher.mainFlywheel, MotorType.kBrushless);
    private final CANSparkMax m_topRollers = new CANSparkMax(Constants.Launcher.topRollers , MotorType.kBrushless);
  /** Creates a new Launcher. */
  public Launcher() {
    m_mainFlywheel.setOpenLoopRampRate(1.25);
    m_mainFlywheel.setIdleMode(IdleMode.kCoast);
    
    m_topRollers.setOpenLoopRampRate(1.25);
    m_topRollers.setIdleMode(IdleMode.kCoast);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void run (double speed, double rollerSpeed){
    m_mainFlywheel.set(speed);
    m_topRollers.set(-rollerSpeed);
  }
}
