// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.BACK_LEFT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.BACK_RIGHT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.DRIVETRAIN_TRACKWIDTH_METERS;
import static frc.robot.Constants.DRIVETRAIN_WHEELBASE_METERS;
import static frc.robot.Constants.FRONT_LEFT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_OFFSET;

import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import com.swervedrivespecialties.swervelib.Mk4iSwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.swervedrivespecialties.swervelib.SwerveModule;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivetrainSubsystem extends SubsystemBase {

        /**
         * The maximum voltage that will be delivered to the drive motors.
         * <p>
         * This can be reduced to cap the robot's maximum speed. Typically, this is
         * useful during initial testing of the robot.
         */
        public static final double MAX_VOLTAGE = 12.0; // 12.0;
        // FIXME Measure the drivetrain's maximum velocity or calculate the theoretical.
        // The formula for calculating the theoretical maximum velocity is:
        // <Motor free speed RPM> / 60 * <Drive reduction> * <Wheel diameter meters> *
        // pi
        // By default this value is setup for a Mk3 standard module using Falcon500s to
        // drive.
        // An example of this constant for a Mk4 L2 module with NEOs to drive is:
        // 5880.0 / 60.0 / SdsModuleConfigurations.MK4_L2.getDriveReduction() *
        // SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI
        /**
         * The maximum velocity of the robot in meters per second.
         * <p>
         * This is a measure of how fast the robot should be able to drive in a straight
         * line.
         */
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 5880.0 / 60.0 *
                        SdsModuleConfigurations.MK4I_L2.getDriveReduction() *
                        SdsModuleConfigurations.MK4I_L2.getWheelDiameter() * Math.PI;
        /**
         * The maximum angular velocity of the robot in radians per second.
         * <p>
         * This is a measure of how fast the robot can rotate in place.
         */
        // Here we calculate the theoretical maximum angular velocity. You can also
        // replace this with a measured amount.
        public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
                        Math.hypot(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0);

        private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
                        // Front left
                        new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
                        // Front right
                        new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0),
                        // Back left
                        new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
                        // Back right
                        new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0));

        // By default we use a Pigeon for our gyroscope. But if you use another
        // gyroscope, like a NavX, you can change this.
        // The important thing about how you configure your gyroscope is that rotating
        // the robot counter-clockwise should
        // cause the angle reading to increase until it wraps back over to zero.
        // FIXME Remove if you are using a Pigeon
        // private final PigeonIMU m_pigeon = new PigeonIMU(DRIVETRAIN_PIGEON_ID);
        // FIXME Uncomment if you are using a NavX
        private final AHRS m_navx = new AHRS(SPI.Port.kMXP, (byte) 200); // NavX connected over MXP

        // These are our modules. We initialize them in the constructor.
        public SwerveModule[] m_SwerveMods;
        private final SwerveModule m_frontLeftModule;
        private final SwerveModule m_frontRightModule;
        private final SwerveModule m_backLeftModule;
        private final SwerveModule m_backRightModule;
        private final SwerveDriveOdometry m_Odometry;

        private final Field2d m_field = new Field2d();

        private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

        private boolean robotCentic = false;

        public DrivetrainSubsystem() {
                ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

                SmartDashboard.putData("Field", m_field);

                // There are 4 methods you can call to create your swerve modules.
                // The method you use depends on what motors you are using.
                //
                // Mk3SwerveModuleHelper.createFalcon500(...)
                // Your module has two Falcon 500s on it. One for steering and one for driving.
                //
                // Mk3SwerveModuleHelper.createNeo(...)
                // Your module has two NEOs on it. One for steering and one for driving.
                //
                // Mk3SwerveModuleHelper.createFalcon500Neo(...)
                // Your module has a Falcon 500 and a NEO on it. The Falcon 500 is for driving
                // and the NEO is for steering.
                //
                // Mk3SwerveModuleHelper.createNeoFalcon500(...)
                // Your module has a NEO and a Falcon 500 on it. The NEO is for driving and the
                // Falcon 500 is for steering.
                //
                // Similar helpers also exist for Mk4 modules using the Mk4SwerveModuleHelper
                // class.

                // By default we will use Falcon 500s in standard configuration. But if you use
                // a different configuration or motors
                // you MUST change it. If you do not, your code will crash on startup.
                // FIXME Setup motor configuration
                m_frontLeftModule = Mk4iSwerveModuleHelper.createNeo(
                                // This parameter is optional, but will allow you to see the current state of
                                // the module on the dashboard.
                                tab.getLayout("Front Left Module", BuiltInLayouts.kList)
                                                .withSize(2, 4)
                                                .withPosition(0, 0),
                                // This can either be STANDARD or FAST depending on your gear configuration
                                Mk4iSwerveModuleHelper.GearRatio.L2,
                                // This is the ID of the drive motor
                                FRONT_LEFT_MODULE_DRIVE_MOTOR,
                                // This is the ID of the steer motor
                                FRONT_LEFT_MODULE_STEER_MOTOR,
                                // This is the ID of the steer encoder
                                FRONT_LEFT_MODULE_STEER_ENCODER,
                                // This is how much the steer encoder is offset from true zero (In our case,
                                // zero is facing straight forward)
                                FRONT_LEFT_MODULE_STEER_OFFSET);

                // We will do the same for the other modules
                m_frontRightModule = Mk4iSwerveModuleHelper.createNeo(
                                tab.getLayout("Front Right Module", BuiltInLayouts.kList)
                                                .withSize(2, 4)
                                                .withPosition(2, 0),
                                Mk4iSwerveModuleHelper.GearRatio.L2,
                                FRONT_RIGHT_MODULE_DRIVE_MOTOR,
                                FRONT_RIGHT_MODULE_STEER_MOTOR,
                                FRONT_RIGHT_MODULE_STEER_ENCODER,
                                FRONT_RIGHT_MODULE_STEER_OFFSET);

                m_backLeftModule = Mk4iSwerveModuleHelper.createNeo(
                                tab.getLayout("Back Left Module", BuiltInLayouts.kList)
                                                .withSize(2, 4)
                                                .withPosition(4, 0),
                                Mk4iSwerveModuleHelper.GearRatio.L2,
                                BACK_LEFT_MODULE_DRIVE_MOTOR,
                                BACK_LEFT_MODULE_STEER_MOTOR,
                                BACK_LEFT_MODULE_STEER_ENCODER,
                                BACK_LEFT_MODULE_STEER_OFFSET);

                m_backRightModule = Mk4iSwerveModuleHelper.createNeo(
                                tab.getLayout("Back Right Module", BuiltInLayouts.kList)
                                                .withSize(2, 4)
                                                .withPosition(6, 0),
                                Mk4iSwerveModuleHelper.GearRatio.L2,
                                BACK_RIGHT_MODULE_DRIVE_MOTOR,
                                BACK_RIGHT_MODULE_STEER_MOTOR,
                                BACK_RIGHT_MODULE_STEER_ENCODER,
                                BACK_RIGHT_MODULE_STEER_OFFSET);

                m_Odometry = new SwerveDriveOdometry(
                                m_kinematics, getGyroscopeRotation());

        }

        /**
         * Sets the gyroscope angle to zero. This can be used to set the direction the
         * robot is currently facing to the
         * 'forwards' direction.
         */
        public void zeroGyroscope() {
                // FIXME Remove if you are using a Pigeon
                // m_pigeon.setFusedHeading(0.0);

                // FIXME Uncomment if you are using a NavX
                m_navx.zeroYaw();
        }

        public void makeRobotCentric() {
                if (robotCentic == false) {
                        // robotCentic = true;
                }

        }

        public void makeFieldCentric() {
                if (robotCentic == true) {
                        robotCentic = false;
                }

        }

        public Rotation2d getGyroscopeRotation() {
                // FIXME Remove if you are using a Pigeon
                // return Rotation2d.fromDegrees(m_pigeon.getFusedHeading());
                if (robotCentic == false) {
                        // FIXME Uncomment if you are using a NavX
                        if (m_navx.isMagnetometerCalibrated()) {
                                // We will only get valid fused headings if the magnetometer is calibrated
                                return Rotation2d.fromDegrees(m_navx.getFusedHeading());
                        }

                        // We have to invert the angle of the NavX so that rotating the robot
                        // counter-clockwise makes the angle increase.
                        return Rotation2d.fromDegrees(360.0 - m_navx.getYaw());
                } else {
                        return new Rotation2d(0);
                }
        }

        public void resetOdometry(Pose2d pose) {
                m_Odometry.resetPosition(pose, new Rotation2d(0));
                // swerveOdometry.resetPosition(pose, getGyroscopeRotation());
        }

        /* Used by SwerveFollowCommand in Auto */
        public void setModuleStates(SwerveModuleState[] desiredStates) {
                // DataLogManager.log("setModuleState Begin");
                SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.maxSpeed);

                m_frontLeftModule.set(
                                desiredStates[0].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE,
                                desiredStates[0].angle.getRadians());
                m_frontRightModule.set(
                                desiredStates[1].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE,
                                desiredStates[1].angle.getRadians());
                m_backLeftModule.set(
                                desiredStates[2].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE,
                                desiredStates[2].angle.getRadians());
                m_backRightModule.set(
                                desiredStates[3].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE,
                                desiredStates[3].angle.getRadians());

                // DataLogManager.log("setModuleState End");
        }

        public Pose2d getPose() {
                return m_Odometry.getPoseMeters();
        }

        public void drive(ChassisSpeeds chassisSpeeds) {
                m_chassisSpeeds = chassisSpeeds;
        }

        // Assuming this method is part of a drivetrain subsystem that provides the
        // necessary methods
        public Command followTrajectoryCommand(PathPlannerTrajectory traj, boolean isFirstPath) {

                return new SequentialCommandGroup(
                                new InstantCommand(() -> {
                                        // Reset odometry for the first path you run during auto
                                        if (isFirstPath) {
                                                this.resetOdometry(this.getPose());
                                        }
                                }),

                                new PPSwerveControllerCommand(
                                                this,
                                                traj,
                                                this::getPose, // Pose supplier
                                                Constants.swerveKinematics, // SwerveDriveKinematics
                                                new PIDController(Constants.Trajectory.xP, Constants.Trajectory.xI,
                                                                Constants.Trajectory.xD), // X controller. Tune these
                                                                                          // values for your robot.
                                                                                          // Leaving them 0 will only
                                                                                          // use feedforwards.
                                                new PIDController(Constants.Trajectory.yP, Constants.Trajectory.yI,
                                                                Constants.Trajectory.yD), // Y controller (usually the
                                                                                          // same values as X
                                                                                          // controller)
                                                new ProfiledPIDController(Constants.Trajectory.tP,
                                                                Constants.Trajectory.tI, Constants.Trajectory.tD,
                                                                new Constraints(1, 1)), // Rotation controller. Tune
                                                                                        // these values for your robot.
                                                                                        // Leaving them 0 will only use
                                                                                        // feedforwards.
                                                this::setModuleStates, // Module states consumer
                                                true, // reset odom
                                                this // Requires this drive subsystem
                                ));
        }

        @Override
        public void periodic() {

                SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
                SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_VELOCITY_METERS_PER_SECOND);
                m_Odometry.update(getGyroscopeRotation(), states);
                m_field.setRobotPose(m_Odometry.getPoseMeters());

                if (DriverStation.isAutonomous() != true) {
                        if ((states[0].speedMetersPerSecond < 0.1) && (states[1].speedMetersPerSecond < 0.1) &&
                                        (states[2].speedMetersPerSecond < 0.1) && (states[3].speedMetersPerSecond < 0.1)
                                        &&
                                        !DriverStation.isAutonomous()) {
                                m_frontLeftModule.set(0, Math.toRadians(45));
                                m_frontRightModule.set(0, Math.toRadians(-45));
                                m_backLeftModule.set(0, Math.toRadians(-45));
                                m_backRightModule.set(0, Math.toRadians(45));
                        } else {
                                m_frontLeftModule.set(
                                                states[0].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND
                                                                * MAX_VOLTAGE,
                                                states[0].angle.getRadians());
                                m_frontRightModule.set(
                                                states[1].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND
                                                                * MAX_VOLTAGE,
                                                states[1].angle.getRadians());
                                m_backLeftModule.set(
                                                states[2].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND
                                                                * MAX_VOLTAGE,
                                                states[2].angle.getRadians());
                                m_backRightModule.set(
                                                states[3].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND
                                                                * MAX_VOLTAGE,
                                                states[3].angle.getRadians());
                        }
                }
        }
}
