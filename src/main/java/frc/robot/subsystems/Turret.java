// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase {

  private final CANSparkMax m_turret = new CANSparkMax(Constants.Turret.turret, MotorType.kBrushless);
  private final RelativeEncoder m_encoder;
  private final SparkMaxPIDController m_PIDController;
  /** Creates a new Turret. */
  public Turret() {
    m_encoder = m_turret.getEncoder();

    m_encoder.setPositionConversionFactor(Constants.Turret.positionConversionFactor);
    m_encoder.setVelocityConversionFactor(Constants.Turret.positionConversionFactor / 60);

    m_PIDController = m_turret.getPIDController();

    m_PIDController.setP(Constants.Turret.P);
    m_PIDController.setI(Constants.Turret.I);
    m_PIDController.setD(Constants.Turret.D);
    m_PIDController.setSmartMotionAllowedClosedLoopError(1, 0);

    m_turret.setSoftLimit(SoftLimitDirection.kForward, Constants.Turret.softLimitFor);
    m_turret.setSoftLimit(SoftLimitDirection.kReverse, Constants.Turret.softLimitRev);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void turn(double angle) {
    m_PIDController.setReference(angle, ControlType.kPosition);
  }

  public double returnEncoderPosition() {
    return m_encoder.getPosition();
  }
}
