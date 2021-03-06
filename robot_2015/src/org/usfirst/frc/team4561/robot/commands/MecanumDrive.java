package org.usfirst.frc.team4561.robot.commands;

import org.usfirst.frc.team4561.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class MecanumDrive extends Command {
	/**
	 * Gives the drive train the current joystick values.
	 */
	public MecanumDrive() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveTrain.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.oi.isRobotRelative()) {
			if(Robot.oi.isTouringMode())
			Robot.driveTrain.driveRobotRelative(Robot.oi.getDriveX() * Robot.oi.getTouringPower(),
					Robot.oi.getDriveY() * Robot.oi.getTouringPower());
			else {
				Robot.driveTrain.driveRobotRelative(Robot.oi.getDriveX(), Robot.oi.getDriveY());
			}
		} 
		else {
			if(Robot.oi.isTouringMode()) {
				Robot.driveTrain.driveFieldRelative(Robot.oi.getDriveX() * Robot.oi.getTouringPower(),
						Robot.oi.getDriveY() * Robot.oi.getTouringPower(), 
						Robot.oi.getRotationDegrees());
			}
			else {
				Robot.driveTrain.driveFieldRelative(Robot.oi.getDriveX(),
						Robot.oi.getDriveY(), Robot.oi.getRotationDegrees());
			}

		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
