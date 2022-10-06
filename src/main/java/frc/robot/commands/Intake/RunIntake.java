// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Intake;

public class RunIntake extends CommandBase {
  private Intake m_intake;
  private Feeder m_feeder;
  private XboxController m_controller;
  private boolean m_first;
  private int m_count;

  public RunIntake(Intake intake, Feeder feeder, XboxController controller) {
    m_intake = intake;
    m_feeder = feeder;
    m_controller = controller;

    addRequirements(m_intake);
  }

  // called when the command is initially scheduled
  @Override
  public void initialize() {
    m_intake.run(1.0);
    if (m_feeder.isBall() == false) {
      m_first = true;
    } else {
      m_first = false;
    }
    m_count = 0;
  }

  // called every time the scheduler runs while the command is scheduled
  @Override
  public void execute() {
    if(m_feeder.isBall() == false){
      m_feeder.run("HOLD");
    }
    else{
      m_feeder.stop();
      if (m_first) {
        m_controller.setRumble(RumbleType.kLeftRumble, 1.0);
        m_controller.setRumble(RumbleType.kRightRumble, 1.0);
        m_first = false;
        m_count = 10;
      }
    }

    if (m_count != 0) {
      m_count--;
      if (m_count == 0) {
        m_controller.setRumble(RumbleType.kLeftRumble, 0.0);
        m_controller.setRumble(RumbleType.kRightRumble, 0.0);
      }
    }
  }

  // called once the command ends or is interrupted
  @Override
  public void end(boolean interrupted) {
    m_intake.run(0.0);
    m_feeder.stop();
    m_controller.setRumble(RumbleType.kLeftRumble, 0.0);
    m_controller.setRumble(RumbleType.kRightRumble, 0.0);
  }

  // returns false when the command should end
  @Override
  public boolean isFinished() {
    return false;
  }
}
