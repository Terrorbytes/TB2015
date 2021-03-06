package org.usfirst.frc.team4561.robot.commands;

import org.usfirst.frc.team4561.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class MoveElevatorNonPIDTimed extends Command {

	private static final int FORWARD = 0;
	private static final int FORWARD_RIGHT = 45;
	//private static final int RIGHT = 90;
	private static final int BACKWARD_RIGHT = 135;
	private static final int BACKWARD = 180;
	private static final int BACKWARD_LEFT = 225;
	//private static final int LEFT = 270;
	private static final int FORWARD_LEFT = 315;
	
	private int currentDirection = FORWARD;
	
	private boolean up;
	
    public MoveElevatorNonPIDTimed(double timeout, boolean up) {
        requires((Subsystem)Robot.commonElevator);
        this.up = up;
    	setTimeout(timeout);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(up) {
    		currentDirection = FORWARD;
    	}
    	else{
    		currentDirection = BACKWARD;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(currentDirection == FORWARD || currentDirection == FORWARD_LEFT || currentDirection == FORWARD_RIGHT) {
    		Robot.commonElevator.moveUp();
    	}
    	if(currentDirection == BACKWARD || currentDirection == BACKWARD_LEFT || currentDirection == BACKWARD_RIGHT) {
    		Robot.commonElevator.moveDown();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.commonElevator.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
