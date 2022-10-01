// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.commands.RunFlywheel;
import frc.robot.commands.Auto.SimpleAuto;
import frc.robot.commands.Feeder.FeedFeeder;
import frc.robot.commands.Feeder.HoldFeeder;
import frc.robot.commands.Intake.ExtendAndRunIntake;
import frc.robot.commands.Intake.ExtendIntake;
import frc.robot.commands.Intake.RunIntake;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.PrettyLights;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final PowerDistribution powerHub = new PowerDistribution(2, ModuleType.kRev);
  private final Launcher m_launcher = new Launcher();
  private final DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
  private final Intake m_intake = new Intake();
  private final Pneumatics m_pneumatics = new Pneumatics();
  private final Feeder m_feeder = new Feeder();
  private final PrettyLights m_lights = new PrettyLights(powerHub);
  private final Hood m_hood = new Hood();

  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);
  
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Set up the default command for the drivetrain.
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation
    m_drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
            m_drivetrainSubsystem,
            () -> -modifyAxis(driverController.getLeftY()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(driverController.getLeftX()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(driverController.getRightX()) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
    ));
    // Configure the button bindings
    configureButtonBindings();

    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Back button zeros the gyroscope
    new Button(driverController::getBackButton)
            // No requirements because we don't need to interrupt anything
            .whenPressed(m_drivetrainSubsystem::zeroGyroscope);

    new JoystickButton(driverController, XboxController.Button.kLeftBumper.value).
            whileHeld(new RunIntake(m_intake, m_feeder));
    
    buttonFromDouble(() -> driverController.getLeftTriggerAxis()).
            whileHeld(new ExtendIntake(m_intake));
    
    buttonFromDouble(() -> driverController.getRightTriggerAxis()).
            whileHeld(new ExtendAndRunIntake(m_intake, m_feeder));

    new JoystickButton(manipulatorController, XboxController.Button.kB.value).
            whileHeld(new HoldFeeder(m_feeder));

    new JoystickButton(manipulatorController, XboxController.Button.kLeftBumper.value).
            whileHeld(new RunFlywheel(m_launcher, "EJECT"));

    buttonFromDouble(() -> manipulatorController.getLeftTriggerAxis()).
            whileHeld(new RunFlywheel(m_launcher, "SHOOT"));

    buttonFromDouble(() -> manipulatorController.getRightTriggerAxis()).
            whileHeld(new FeedFeeder(m_feeder));
  }

  private Button buttonFromDouble(DoubleSupplier value) {
    return new Button() {
      @Override
      public boolean get() {
        return Math.abs(value.getAsDouble()) > 0.1;
      }
    };
  }
  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new SimpleAuto(m_drivetrainSubsystem, m_launcher, m_feeder);
  }

  private static double deadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  private static double modifyAxis(double value) {
    // Deadband
    value = deadband(value, 0.05);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }

  public void updateMatchTime() {
    double matchTime = Math.floor(DriverStation.getMatchTime());
    if (matchTime != -1) {
      SmartDashboard.putNumber("Match Time", matchTime);
    }
  }
}
