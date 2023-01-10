// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = Units.inchesToMeters(20.25);
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = Units.inchesToMeters(21.25); 

    public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
      new Translation2d(DRIVETRAIN_WHEELBASE_METERS / 2.0, DRIVETRAIN_TRACKWIDTH_METERS / 2.0),
      new Translation2d(DRIVETRAIN_WHEELBASE_METERS / 2.0, -DRIVETRAIN_TRACKWIDTH_METERS / 2.0),
      new Translation2d(-DRIVETRAIN_WHEELBASE_METERS / 2.0, DRIVETRAIN_TRACKWIDTH_METERS / 2.0),
      new Translation2d(-DRIVETRAIN_WHEELBASE_METERS / 2.0, -DRIVETRAIN_TRACKWIDTH_METERS / 2.0));

/* Swerve Profiling Values */
public static final double maxSpeed = 4.8;// 4.5; //meters per second

    //public static final int DRIVETRAIN_PIGEON_ID = 0; // FIXME Set Pigeon ID

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 1; // FIXME Set front left module drive motor ID
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 2; // FIXME Set front left module steer motor ID
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 2; // FIXME Set front left steer encoder ID
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(72.7); // FIXME Measure and set front left steer offset :: 73.9

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 5; // FIXME Set front right drive motor ID 5
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 6; // FIXME Set front right steer motor ID 6
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 6; // FIXME Set front right steer encoder ID 6
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(210.4); // FIXME Measure and set front right steer offset :: 210.7

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 3; // FIXME Set back left drive motor ID
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 4; // FIXME Set back left steer motor ID
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 4; // FIXME Set back left steer encoder ID
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(281.2); // FIXME Measure and set back left steer offset :: 281.1

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 7; // FIXME Set back right drive motor ID 7
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 8; // FIXME Set back right steer motor ID 8
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 8; // FIXME Set back right steer encoder ID 8
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(160.0); // FIXME Measure and set back right steer offset :: 160.3

  public static final class Feeder {
    public static final int frontCargoFeeder = 10;
    public static final int rearCargoFeeder = 9;
    public static final double defaultSpeed = 0.5;
  }
  
  public static final class Launcher {
    public static final int adjustableHood = 12;

    public static final int topRollers = 13;

    public static final int mainFlywheel = 14;


  }

  public static final class Turret {
    public static final int turret = 15;
    
    public static final int softLimitFor = 40;
    public static final int softLimitRev = -40;

    public static final double P = 0.015;
    public static final double I = 0;
    public static final double D = 0.5;

    public static final double positionConversionFactor = 28;
  }

      /**
   * Configuration of the intake subsystem.
   */
  public static final class Intake {
    /**
     * The CAN ID of the intake motor.
     */
    public static final int intakeRollers = 11;

    /**
     * The pneumatic hub channel ID of the intake solenoid.
     */
    public static final int kSolenoid = 0;

    /**
     * The maximum current to send to the intake motor.
     */
    public static final int kCurrentLimit = 30;

    /**
     * The maximum speed that the intake motor runs.
     */
    public static final double kMaxSpeed = 0.85;
  }

  public static final class Trajectory {
    
    public static final double xP = 0.125;

    public static final double xI = 0;

    public static final double xD = 0;



    public static final double yP = 0.1875;

    public static final double yI = 0;

    public static final double yD = 0;



    public static final double tP = 0;

    public static final double tI = 0;

    public static final double tD = 0;

  }

    /**
     * Controls the logging of detailed information about the robot.
     */
    public static final boolean kEnableDetailedLogging = true;
}
