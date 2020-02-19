/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.IMUProtocol.GyroUpdate;

import frc.robot.Setmotor;
import frc.robot.Constants.PowCon;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Powercell extends SubsystemBase {
  SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 40, 50, 1);
  TalonFXConfiguration talonFXConfiguration = new TalonFXConfiguration();
  Setmotor setmotor = new Setmotor();
  WPI_TalonFX flywheel = new WPI_TalonFX(PowCon.flywheelID);
  WPI_VictorSPX conveyor = new WPI_VictorSPX(PowCon.conveyorID);
  WPI_VictorSPX intake = new WPI_VictorSPX(PowCon.intakeID);
  WPI_VictorSPX armmas = new WPI_VictorSPX(PowCon.intakearmfolID);
  WPI_VictorSPX armfol = new WPI_VictorSPX(PowCon.intakearmfolID);
  WPI_VictorSPX wide = new WPI_VictorSPX(PowCon.wideID);
  /**
   * Creates a new Powercell.
   */
  public Powercell() {
    setmotor.setmotor(flywheel, supplyCurrentLimitConfiguration,PowCon.kP ,PowCon.kF,InvertType.None, 0, 1, 10);
    setmotor.setmotor(armmas,InvertType.None,10);
    setmotor.setmotor(armfol,InvertType.None,10);
    setmotor.setmotor(intake,InvertType.None,10);
    setmotor.setmotor(wide,InvertType.None,10);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }









  









}
