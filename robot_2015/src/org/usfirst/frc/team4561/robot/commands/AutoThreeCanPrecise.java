package org.usfirst.frc.team4561.robot.commands;

/** 
 * Command to be run during autonomous mode
 * 
 * START: Claw facing eastern guardrail, Extender out 80"(6'8"), center of robot positioned at the 
 * intersection of the horizontal center of the furthest scoring platform of the alliance wall, and the
 * line formed by the second group of two vertical totes in the landfill zone from the eastern guardrail.
 * 
 * END: Claw facing eastern guardrail, Extender out 80"(6'8"), robot positioned in middle
 * of the autozone.
 */
public class AutoThreeCanPrecise extends Abstract4561AutomodeGroup {
    
    public  AutoThreeCanPrecise() {
    	//Get first can
    	addSequential(new CommonAutoOneCan());
    	//Get second can
    	addSequential(new CommonAutoTwoCan());
    	//Drive Forward until hit totes
    	AutoCardinalFieldRelativeDriveWithTape forward = new AutoCardinalFieldRelativeDriveWithTape(1, 22, 180);
    	addSequential(forward);
    	//Back up a bit
    	addSequential(new AutoConditionalBackoff(1, 3, 180, forward));
    	//Precisely strafe between landfill and scoring platform to "hook" third can
    	addSequential(new AutoCardinalFieldRelativeDrive(4, 161, 180));
    	//Pull third can back
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 22, 180));
    	//Drive to autozone
    	addSequential(new AutoCardinalFieldRelativeDrive(3, 48.5, 180));
    	//Rotate to fit in autozone
    	addSequential(new RotateTo(-90.0));
    }

	@Override
	public double getStartAngle() {
		// TODO Auto-generated method stub
		return 90;
	}
}
