// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Intake;

public class ExtendAndRunIntake extends CommandBase {
  private Intake m_intake;
  private Feeder m_feeder;
  /** Creates a new ExtendAndRunIntake. */
  public ExtendAndRunIntake(Intake intake, Feeder feeder) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intake = intake;
    m_feeder = feeder;
    addRequirements(m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intake.extend();
    m_intake.run(1.0);
    
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_feeder.isBall() == false){
      m_feeder.run("HOLD");
    }
    else{
      m_feeder.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.retract();
    m_intake.run(0.0);
    m_feeder.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
