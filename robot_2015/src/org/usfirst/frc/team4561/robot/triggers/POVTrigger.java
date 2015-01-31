package org.usfirst.frc.team4561.robot.triggers;

import org.usfirst.frc.team4561.robot.Robot;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class POVTrigger extends Trigger {
    
    public boolean get() {
        return Robot.oi.getDrivePOV() != -1 && Robot.oi.isRobotRelative();
    }
}