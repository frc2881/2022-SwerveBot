// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase {
  private final int m_softLimitFor = 40;
  private final int m_softLimitRev = -40;

  private final double m_P = 0.015;
  private final double m_I = 0;
  private final double m_D = 0.5;

  private final double m_positionConversionFactor = 28;

  private final CANSparkMax m_turret = new CANSparkMax(Constants.Launcher.turret, MotorType.kBrushless);
  private final RelativeEncoder m_encoder;
  private final SparkMaxPIDController m_PIDController;
  /** Creates a new Turret. */
  public Turret() {
    m_encoder = m_turret.getEncoder();

    m_encoder.setPositionConversionFactor(m_positionConversionFactor);
    m_encoder.setVelocityConversionFactor(m_positionConversionFactor / 60);

    m_PIDController = m_turret.getPIDController();

    m_PIDController.setP(m_P);
    m_PIDController.setI(m_I);
    m_PIDController.setD(m_D);
    m_PIDController.setSmartMotionAllowedClosedLoopError(1, 0);

    m_turret.setSoftLimit(SoftLimitDirection.kForward, m_softLimitFor);
    m_turret.setSoftLimit(SoftLimitDirection.kReverse, m_softLimitRev);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void turn(double angle) {
    m_PIDController.setReference(angle, ControlType.kPosition);
  }
}
