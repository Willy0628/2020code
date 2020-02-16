/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants.Drivetrainconstants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {  
  WPI_TalonSRX Leftmaster    = new WPI_TalonSRX(Constants.Drivetrainconstants.LeftmasterID);
  WPI_TalonSRX Rightmaster   = new WPI_TalonSRX(Constants.Drivetrainconstants.RightmasterID);
  WPI_VictorSPX Leftfollower  = new WPI_VictorSPX(Constants.Drivetrainconstants.LeftfollowerID);
  WPI_VictorSPX Rightfollower = new WPI_VictorSPX(Constants.Drivetrainconstants.RightfollowerID);
  Joystick joystick = new Joystick(0);
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  
  double m_quickStopAccumulator=0;
  double leftout,rightoutput,x,y,area;
  
  
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    
    Leftmaster.configFactoryDefault();
    Rightmaster.configFactoryDefault();
    Leftfollower.configFactoryDefault();
    Rightfollower.configFactoryDefault();
    
    Leftmaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,Constants.Drivetrainconstants.timeoutMs );
    Rightmaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.Drivetrainconstants.timeoutMs);

    Leftmaster.setInverted(false);
    Rightmaster.setInverted(true);
    Leftfollower.setInverted(InvertType.FollowMaster);
    Rightfollower.setInverted(InvertType.FollowMaster);

    Leftfollower.follow(Leftmaster);
    Rightfollower.follow(Rightmaster);

    Leftmaster.configMotionCruiseVelocity(Drivetrainconstants.MaxSpeed,10);
    Rightmaster.configMotionCruiseVelocity(Drivetrainconstants.MaxSpeed,10);
    Leftmaster.configMotionAcceleration(Drivetrainconstants.MaxAcc, 10);
    Rightmaster.configMotionAcceleration(Drivetrainconstants.MaxAcc,10);

    Rightmaster.enableCurrentLimit(true);
    Leftmaster.enableCurrentLimit(true);
    Rightmaster.configContinuousCurrentLimit(Constants.Drivetrainconstants.MaxAmp);
    
    Rightmaster.enableVoltageCompensation(true);
    Leftmaster.enableVoltageCompensation(true);

    Rightmaster.configVoltageCompSaturation(11.3);
    Leftmaster.configVoltageCompSaturation(11.3);

    Leftmaster.configAllowableClosedloopError(0, 10, 10);
    Rightmaster.configAllowableClosedloopError(0, 10, 10);
    
    Leftmaster.config_kP(Drivetrainconstants.pidsolt, Drivetrainconstants.kPdrive,Drivetrainconstants.timeoutMs);
    Rightmaster.config_kP(Drivetrainconstants.pidsolt, Drivetrainconstants.kPdrive,Drivetrainconstants.timeoutMs);
    Leftmaster.config_kF(Drivetrainconstants.pidsolt, Drivetrainconstants.kFdrive,Drivetrainconstants.timeoutMs);
    Rightmaster.config_kF(Drivetrainconstants.pidsolt, Drivetrainconstants.kFdrive,Drivetrainconstants.timeoutMs);
    Leftmaster.config_kD(Drivetrainconstants.pidsolt, Drivetrainconstants.kDdrive,Drivetrainconstants.timeoutMs);
    Rightmaster.config_kD(Drivetrainconstants.pidsolt, Drivetrainconstants.kDdrive,Drivetrainconstants.timeoutMs);
    Leftmaster.config_kI(Drivetrainconstants.pidsolt, Drivetrainconstants.kIdrive,Drivetrainconstants.timeoutMs);
    Rightmaster.config_kI(Drivetrainconstants.pidsolt, Drivetrainconstants.kIdrive,Drivetrainconstants.timeoutMs);

    Rightmaster.configNeutralDeadband(Drivetrainconstants.deadband);
    Leftmaster.configNeutralDeadband(Drivetrainconstants.deadband);

    Leftmaster.setNeutralMode(NeutralMode.Brake);
    Rightmaster.setNeutralMode(NeutralMode.Brake);

  }@Override
  public void robotPeriodic() {
   
   
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);

  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
   
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    
    Leftmaster.set(ControlMode.Velocity,2000*leftout,DemandType.ArbitraryFeedForward,-0.03*x);
    Rightmaster.set(ControlMode.Velocity,-2000*leftout,DemandType.ArbitraryFeedForward,0.03*x);

    //SmartDashboard.putNumber("joyraw", joystick.getRawAxis(1));
    //SmartDashboard.putNumber("joysch", 0.3*joystick.getRawAxis(1)+0.7*Math.pow(joystick.getRawAxis(1),5.0));
    
    SmartDashboard.putNumber("LeftVel", Leftmaster.getSelectedSensorVelocity());
    SmartDashboard.putNumber("RightVel", Rightmaster.getSelectedSensorVelocity());

    SmartDashboard.putNumber("Leftmaster", Leftmaster.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Rightmaster", Rightmaster.getSelectedSensorPosition(0));
 
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }



  /**
   * Curvature drive method for differential drive platform.
   *
   * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
   * heading change. This makes the robot more controllable at high speeds. Also handles the
   * robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
   * turn-in-place maneuvers.
   *
   * @param xSpeed      The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation   The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *                    positive.
   * @param isQuickTurn If set, overrides constant-curvature turning for
   *                    turn-in-place maneuvers.
   */
  @SuppressWarnings({"ParameterName", "PMD.CyclomaticComplexity"})
  public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {

    xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);

    zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);

    double angularPower;
    boolean overPower;
    double m_quickStopAlpha =0.1;
    if (isQuickTurn) {
      if (Math.abs(xSpeed) < 0.1) {
        m_quickStopAccumulator = (1 - 0.1) * m_quickStopAccumulator
            + m_quickStopAlpha * MathUtil.clamp(zRotation, -1.0, 1.0) * 2;
      }
      overPower = true;
      angularPower = zRotation;
    } else {
      overPower = false;
      angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

      if (m_quickStopAccumulator > 1) {
        m_quickStopAccumulator -= 1;
      } else if (m_quickStopAccumulator < -1) {
        m_quickStopAccumulator += 1;
      } else {
        m_quickStopAccumulator = 0.0;
      }
    }

    double leftMotorOutput = xSpeed + angularPower;
    double rightMotorOutput = xSpeed - angularPower;

    // If rotation is overpowered, reduce both outputs to within acceptable range
    if (overPower) {
      if (leftMotorOutput > 1.0) {
        rightMotorOutput -= leftMotorOutput - 1.0;
        leftMotorOutput = 1.0;
      } else if (rightMotorOutput > 1.0) {
        leftMotorOutput -= rightMotorOutput - 1.0;
        rightMotorOutput = 1.0;
      } else if (leftMotorOutput < -1.0) {
        rightMotorOutput -= leftMotorOutput + 1.0;
        leftMotorOutput = -1.0;
      } else if (rightMotorOutput < -1.0) {
        leftMotorOutput -= rightMotorOutput + 1.0;
        rightMotorOutput = -1.0;
      }
    }

    // Normalize the wheel speeds
    double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
    if (maxMagnitude > 1.0) {
      leftMotorOutput /= maxMagnitude;
      rightMotorOutput /= maxMagnitude;
    }

    leftout=leftMotorOutput;
    rightoutput = rightMotorOutput;
    /*
    m_leftMotor.set(leftMotorOutput * m_maxOutput);
    m_rightMotor.set(rightMotorOutput * m_maxOutput * m_rightSideInvertMultiplier);
    */

  }
}
