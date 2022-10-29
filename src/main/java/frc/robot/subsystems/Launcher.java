// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Launcher extends SubsystemBase {
    private final CANSparkMax m_mainFlywheel = new CANSparkMax(Constants.Launcher.mainFlywheel, MotorType.kBrushless);
    private final CANSparkMax m_topRollers = new CANSparkMax(Constants.Launcher.topRollers , MotorType.kBrushless);
    private double m_flyWheelSpeed = 0;
    private double m_topRollersSpeed = 0;
    private double m_newFlyWheelSpeed;
    private double m_newTopRollersSpeed;
  /** Creates a new Launcher. */
  public Launcher() {
    m_mainFlywheel.setOpenLoopRampRate(2.5);
    m_mainFlywheel.setIdleMode(IdleMode.kCoast);

    m_topRollers.setIdleMode(IdleMode.kCoast);
    m_topRollers.setOpenLoopRampRate(2.5);

    // SmartDashboard.putNumber("Flywheel Speed", m_flyWheelSpeed); // From right at hub: 0.575/
    // SmartDashboard.putNumber("Top Rollers Speed", m_topRollersSpeed); // From right at hub: -0.23
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
   
  }
  /**
   * 
   * @param state
   * Put "SHOOT" to run the flywheel to shoot speed.
   * Put "EJECT" to run the flywheel to eject speed.
   */
  public void run (String state){
    //m_newFlyWheelSpeed = SmartDashboard.getNumber("Flywheel Speed", 0);
    //m_newTopRollersSpeed = SmartDashboard.getNumber("Top Rollers Speed", 0);
    if (state == "SHOOT"){
      m_mainFlywheel.set(0.55);
      m_topRollers.set(-0.55);
      // 81 in: main:0.55  top:-0.55
    }
    else if (state == "EJECT"){
      m_mainFlywheel.set(0.3);
      m_topRollers.set(-0.3);
    }
  }

  public void stop() {
    m_mainFlywheel.set(0);
    m_topRollers.set(0);
  }
}
