// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.VisionTracking;
import frc.robot.subsystems.Turret;

public class FollowTarget extends CommandBase {
  private final VisionTracking m_vision;
  private final Turret m_turret;


  /** Creates a new followTarget. */
  public FollowTarget(VisionTracking vision, Turret turret) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_vision = vision;
    m_turret = turret;
    

    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double yaw = m_vision.getYaw();
    double position = m_turret.returnEncoderPosition();

    double change = position - yaw;

    m_turret.turn(change);


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
