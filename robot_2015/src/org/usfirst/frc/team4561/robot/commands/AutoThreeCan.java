package org.usfirst.frc.team4561.robot.commands;

/**
 *
 */
public class AutoThreeCan extends Abstract4561AutomodeGroup {

    public  AutoThreeCan() {
    	
    	//Rotate: extender faces step
    	addSequential(new RotateTo(180.0));
    	//Drive Forward until hit totes, back up a bit if didn't see tape
    	addSequential(new AutoCardinalFieldRelativeDrive(1, 28, 180, 8));
    	//Strafe to "hook" easternmost can.
    	addSequential(new AutoCardinalFieldRelativeDrive(2, 25, 180));
    	//Pull first can back.
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 22, 180));
    	//Strafe to gap between first and second bins
    	addSequential(new AutoCardinalFieldRelativeDrive(4, 25, 180));
    	//Drive Forward until hit totes, back up a bit if didn't see tape
    	addSequential(new AutoCardinalFieldRelativeDrive(1, 22, 180, 8));
    	//Strafe to second can.
    	addSequential(new AutoCardinalFieldRelativeDrive(4, 25, 180));
    	//Pull second can back
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 22, 180));
    	//Drive Forward until hit totes, back up a bit if didn't see tape
    	addSequential(new AutoCardinalFieldRelativeDrive(1, 22, 180,0.5));
    	//Strafe into gap
    	addSequential(new AutoCardinalFieldRelativeDrive(4, 79.8, 180));
    	//Drive into gap
    	addSequential(new AutoCardinalFieldRelativeDrive(1, 12, 180));
    	//Rotate to "hook" third can
    	addSequential(new RotateTo(135));
    	//Drive back to get can
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 34, 135));
    	//Drive to autozone
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 48.5, 135));
    	//Rotate to fit in autozone
    	addSequential(new RotateTo(90.0));
    }
    @Override
	public double getStartAngle() {
		// Starts with the front of the robot facing the right wall.
		return 90.0;
	}
}