package org.usfirst.frc.team4561.robot.commands;

import org.usfirst.frc.team4561.robot.Robot; 

import edu.wpi.first.wpilibj.command.Command;

/**
 *  This is a command that uses the Hat on the Rotation stick to do precise rotations in robot relative mode.
 *  The only directions that work on the Hats are left and right, the rest don't effect the code.  (Needs verification)
 */



public class RotatingPOV extends Command {

	private static final int ROTATE_LEFT = 2;
	private static final int ROTATE_RIGHT = 6;
	private int currentMovement = ROTATE_LEFT;
	
    public RotatingPOV() {
    	requires(Robot.driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	currentMovement = Robot.oi.getRotatePOV();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (currentMovement == ROTATE_LEFT){
    		Robot.driveTrain.driveRotationRelative(5.0);
    	}
    	else if(currentMovement == ROTATE_RIGHT){
    		Robot.driveTrain.driveRotationRelative(-5.0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
