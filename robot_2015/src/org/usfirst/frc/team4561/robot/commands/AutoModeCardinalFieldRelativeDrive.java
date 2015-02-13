package org.usfirst.frc.team4561.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.usfirst.frc.team4561.robot.Robot;

/**
 * 
 */
public class AutoModeCardinalFieldRelativeDrive extends PIDCommand {
	
// We need this, because otherwise, the motors will run at full power unless the setpoint is below 1 inch away.
	private static final int INCHES_FOR_FULL_POWER = 36;
	
	int direction;
	double inches;
	/**
	 * Drive in a certain direction a certain length
	 * To be used only in the AutoMode command
	 * @param direction
	 * 1 = north
	 * 2 = east
	 * 3 = south
	 * 4 = west
	 * @param inches
	 */
    public AutoModeCardinalFieldRelativeDrive(int direction, double inches) {
    	super(0.3/INCHES_FOR_FULL_POWER, 0, 0);
        requires(Robot.driveTrain);
        this.direction = direction;
        this.inches = inches;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setSetpoint(inches);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(getPIDController().onTarget() == true) {
    		        return true;
    	}
    	else{
    		return false;
    	}

    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

	@Override
	protected double returnPIDInput() {
		return Robot.driveTrain.getAbsAverageEncoderInches();
	}

	@Override
	protected void usePIDOutput(double output) {
		double motorPower = output;
		//North
		if(direction == 1) {
			Robot.driveTrain.driveFieldRelative(0, motorPower, 0);
		}
		//East
		if(direction == 2) {
			Robot.driveTrain.driveFieldRelative(-motorPower, 0, 0);
		}
		//South
		if(direction == 3) {
			Robot.driveTrain.driveFieldRelative(0, -motorPower, 0);
		}
		//West
		if(direction == 4) {
			Robot.driveTrain.driveFieldRelative(motorPower, 0, 0);
		}
	}
}