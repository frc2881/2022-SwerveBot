// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.commands.RunFlywheel;
import frc.robot.commands.Feeder.FeedFeeder;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SimpleAuto extends SequentialCommandGroup {
  /** Creates a new SimpleAuto. */
  public SimpleAuto(DrivetrainSubsystem drive, Launcher launcher, Feeder feeder) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new InstantCommand(() -> drive.zeroGyroscope(), drive));
      /*
    new DefaultDriveCommand(drive, () -> 0.0, () -> 0.75, () -> 0.0).withTimeout(2.5),
                new InstantCommand(() -> launcher.run("SHOOT"), launcher),
                new WaitCommand(1.5),
                new InstantCommand(() -> feeder.run("FEED"), feeder),
                new WaitCommand(0.5),
                new InstantCommand(() -> launcher.stop(), launcher),
                new InstantCommand(() -> feeder.stop(), feeder)); */
  }
}
