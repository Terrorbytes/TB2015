package org.usfirst.frc.team4561.robot.commands;

/**
 * Uses the AutoModeCardinalFieldRelativeDrive command in junction with the
 * RotateTo command to achieve movement.
 */
public class AutoOneCan extends Abstract4561AutomodeGroup {

	
	/** 
	 * Command to be run during autonomous mode
	 * 
	 * START: Claw facing eastern guardrail, Extender out 80"(7'8"), center of robot positioned at the 
	 * intersection of the horizontal center of the furthest scoring platform of the alliance wall, and the
	 * line formed by the second group of two vertical totes in the landfill zone from the eastern guardrail.
	 * 
	 * END: Claw facing eastern guardrail, Extender out 80"(7'8"), robot positioned in eastern half
	 * of the autozone.
	 */
    public  AutoOneCan() {
    	//Rotate: extender faces step
    	addSequential(new RotateTo(180.0));
    	//Drive Forward until hit totes
    	addSequential(new AutoCardinalFieldRelativeDrive(1, 28, 180));
    	//Back up a bit
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 8, 180));
    	//Strafe to "hook" easternmost can.
    	addSequential(new AutoCardinalFieldRelativeDrive(2, 25, 180));
    	//Pull can back.
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 22, 180));
    	//Drive to autozone
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 48.5, 180));
    	//Rotate to fit in autozone
    	addSequential(new RotateTo(90));
    }

	@Override
	public double getStartAngle() {
		// Starts with the front of the robot facing the right wall.
		return 90.0;
	}
}
