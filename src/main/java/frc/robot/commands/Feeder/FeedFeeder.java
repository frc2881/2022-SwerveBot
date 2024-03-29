
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;

public class FeedFeeder extends CommandBase {
  private Feeder m_feeder;

  
  private boolean firstBallOut;
  public FeedFeeder(Feeder feeder) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_feeder = feeder;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    firstBallOut = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  
    if(m_feeder.isBall() == false){
      m_feeder.run("HOLD");
    }
    else{
      m_feeder.run("FEED");
    }
    
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_feeder.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    /*
    if(firstBallOut == true){
      
    }
    else{
      return false;
    }
  }
  */
  /*
  if(m_feeder.isBall() == false){
    return true;
  }
  else{
    return false;
  }
  */
  return false;
}
}
