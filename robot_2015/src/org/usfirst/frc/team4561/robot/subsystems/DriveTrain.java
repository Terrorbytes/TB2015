package org.usfirst.frc.team4561.robot.subsystems;

import org.usfirst.frc.team4561.robot.GyroReadThread;
import org.usfirst.frc.team4561.robot.RobotMap;
import org.usfirst.frc.team4561.robot.commands.MecanumDrive;
import org.usfirst.frc.team4561.robot.Robot;


import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
//import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

/**
 * The main subsystem used for driving. This drive train is configured 
 * to use mecanum wheels and rotate to a heading based on the given 
 * angle from the rotation joystick.
 */
public class DriveTrain extends PIDSubsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	//Talons
	//Uncomment to use PWMTalons
//	private Talon leftFront = new Talon(RobotMap.FRONT_LEFT_MOTOR);
//	private Talon leftRear = new Talon(RobotMap.REAR_LEFT_MOTOR);
//	private Talon rightFront = new Talon(RobotMap.FRONT_RIGHT_MOTOR);
//	private Talon rightRear = new Talon(RobotMap.REAR_RIGHT_MOTOR);
	
	//CANTalonSRXs
	//Uncomment to use CANTalonSRXs
	 public CANTalon leftFront = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN);
	 public CANTalon leftRear = new CANTalon(RobotMap.REAR_LEFT_MOTOR_CAN);
	 public CANTalon rightFront = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN);
	 public CANTalon rightRear = new CANTalon(RobotMap.REAR_RIGHT_MOTOR_CAN);
	 
	 private RobotDrive robotDrive = new RobotDrive(leftFront, leftRear,
			rightFront, rightRear);
	 
	 private DigitalInput whiteTapeDetector = new DigitalInput(RobotMap.TAPE_SENSOR_PORT);
	 
	 private double currentX = 0.0;
	 private double currentY = 0.0;
	 private double lastRotation = 0.0;
	 private boolean fieldRelative = true;
	 private boolean deltaRotating = false;
	 //private double lastGyroAngle = 0.0;
	 //private boolean needToSaveGyroBias = true;
	 //private double gyroBias = 0.0;
	 private double startAngle = 0.0;
	 //private long gyroMissCount = 0;
	 //private boolean pingPending = false;
	 public boolean hasSeenTape = false;
	 private double robotRelRotX = 0.0;
	 
	 //Gyroscope
	 //private SerialPort gyro = new SerialPort(38400, Port.kMXP);
	 
		/*
		 * Reverse Drive Train encoder direction i.e. REVERSE_DIRECTION = True, then forward =
		 * "+", backward = "-"
		 */
		public static final boolean REVERSE_DIRECTION = true;
		/*
		 * Encoder count multiplier ENCODING_TYPE = EncodingType.k1X, count is
		 * normal ENCODING_TYPE = EncodingType.k2X, count is multiplied by 2
		 * ENCODING_TYPE = EncodingType.k4X, count is multiplied by 4
		 */
		public static final EncodingType ENCODING_TYPE = EncodingType.k1X;
	 
	 //Encoders
	 public Encoder frontLeftEncoder = new Encoder(RobotMap.FRONT_LEFT_ENCODER_A_CHANNEL,
			 										RobotMap.FRONT_LEFT_ENCODER_B_CHANNEL, REVERSE_DIRECTION);
//	 public Encoder frontRightEncoder = new Encoder(RobotMap.FRONT_RIGHT_ENCODER_A_CHANNEL,
//													RobotMap.FRONT_RIGHT_ENCODER_B_CHANNEL, REVERSE_DIRECTION);
//	 public Encoder rearLeftEncoder = new Encoder(RobotMap.REAR_LEFT_ENCODER_A_CHANNEL,
//			 										RobotMap.REAR_LEFT_ENCODER_B_CHANNEL, REVERSE_DIRECTION);
//	 public Encoder rearRightEncoder = new Encoder(RobotMap.REAR_RIGHT_ENCODER_A_CHANNEL,
//			 										RobotMap.REAR_RIGHT_ENCODER_B_CHANNEL, REVERSE_DIRECTION);
	 
	 
	private static final double INCHES_PER_REVOLUTION = Math.PI * 2 * 4;
	private static final double PULSES_PER_REVOLUTION = 2048;
	private static final double INCHES_PER_PULSE = INCHES_PER_REVOLUTION/PULSES_PER_REVOLUTION;
	
	private GyroReadThread gyroThread = new GyroReadThread();
	

	public DriveTrain() {
		//Give PID to the controller
		super(1.6/180.0, 0/*0.02/180.0*/, 0); //TODO add "i" constant
		//Set input and output restraints
		setInputRange(-180.0, 180.0);
		setOutputRange(-0.5, 0.5);
		getPIDController().setContinuous(true);
		setAbsoluteTolerance(2.0);
		robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
		robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
		//Enable brake mode(as opposed to coast)
		leftFront.enableBrakeMode(true);
		leftRear.enableBrakeMode(true);
		rightFront.enableBrakeMode(true);
		rightRear.enableBrakeMode(true);
		frontLeftEncoder.setDistancePerPulse(INCHES_PER_PULSE);
//		frontRightEncoder.setDistancePerPulse(INCHES_PER_PULSE);
//		rearLeftEncoder.setDistancePerPulse(INCHES_PER_PULSE);
//		rearRightEncoder.setDistancePerPulse(INCHES_PER_PULSE);
		
		gyroThread.start();
	 }
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new MecanumDrive());
	}

	public void driveRobotRelative(double x_v, double y_v) {
		currentX = x_v;
		currentY = y_v;	
		robotRelRotX = Robot.oi.getRotX();
		fieldRelative = false;
		setSetpoint(getNormalizedGyroAngle());
	}
	
	public void driveRobotRelativeJog(double x_v, double y_v) {
		currentX = x_v;
		currentY = y_v;	
		robotRelRotX = 0;
		fieldRelative = false;
		setSetpoint(getNormalizedGyroAngle());
	}
	
	public void rotateRobotRelativeJog(double rot) {
		currentX = 0;
		currentY = 0;	
		robotRelRotX = rot;
		fieldRelative = false;
		setSetpoint(getNormalizedGyroAngle());
	}

	public void driveFieldRelative(double x_v, double y_v, double rotationDegrees) {
		currentX = x_v;
		currentY = y_v;
		fieldRelative = true;
		setSetpoint(rotationDegrees);
	}
	
	public void rotateTo(double angle) {
		setSetpoint(angle);
	}
	
	public void setP(double p) {
		super.getPIDController().setPID(p, 0, 0);
	}
	
	/**
	 * Change the rotation target by the deltaRotation. A negative value moves
	 * the target left (counter clockwise) and a positive value moves the target
	 * right (clockwise).
	 * 
	 * @param deltaRotation
	 */
	public void driveRotationRelative(double deltaRotation) {
		deltaRotating = true;
		double newSetpoint = getSetpoint() + deltaRotation;
		setSetpoint(normalizeAngle(newSetpoint));
	}

	public void stop() {
//		currentX = 0.0;
//		currentY = 0.0;
	}
	
	private double getAngle() {
		return /*lastGyroAngle*/ gyroThread.getLastGoodRotation() - gyroThread.getBias() + startAngle;
		//gyro.writeString("A");
		/*gyro.writeString("#f");
		String yawPitchRoll = gyro.readString();
		if(yawPitchRoll == null || yawPitchRoll.isEmpty()) {
			gyroMissCount++;
			return lastGyroAngle;
		}
		else {
			double doubleYaw = 0.0;
			try {
				yawPitchRoll = yawPitchRoll.substring(yawPitchRoll.indexOf('=') + 1);
				String stringYaw = yawPitchRoll.substring(0, yawPitchRoll.indexOf(','));
				doubleYaw = Double.parseDouble(stringYaw);
				if (needToSaveGyroBias) {
					gyroBias = doubleYaw;
					needToSaveGyroBias = false;
				}
			}
			catch(NumberFormatException nfe) {
				gyroMissCount++;
				return lastGyroAngle;
			}
			catch(StringIndexOutOfBoundsException sioobe) {
				gyroMissCount++;
				return lastGyroAngle;
			}

			// System.out.println(doubleYaw);
			lastGyroAngle = doubleYaw + startAngle - gyroBias;
			return lastGyroAngle;
		}*/

	}
	
	public double getNormalizedGyroAngle() {
		return normalizeAngle(getAngle());
	}
	
	/**
	 * Converts an angle to the [-180, 180] range.
	 * @param angle
	 * @return
	 */
	private double normalizeAngle(double angle) {
		double norm = angle % 360.0;
		if (Math.abs(norm) > 180.0) {
			norm = (norm < 0) ? norm + 360.0 : norm - 360.0;
		}
		return norm;
	}
	
	public void testSingleMotor(int motorID) {
		if(motorID == RobotMap.FRONT_LEFT_MOTOR_CAN) {
			leftFront.enableBrakeMode(true);
			leftFront.set(0.5);
		}
		else if(motorID == RobotMap.REAR_LEFT_MOTOR_CAN) {
			leftRear.enableBrakeMode(true);
			leftRear.set(0.5);
		}
		else if(motorID == RobotMap.FRONT_RIGHT_MOTOR_CAN) {
			rightFront.enableBrakeMode(true);
			rightFront.set(-0.5);
		}
		else if(motorID == RobotMap.REAR_RIGHT_MOTOR_CAN) {
			rightRear.enableBrakeMode(true);
			rightRear.set(-0.5);
		}
	}
	/**
	 * Gets the value in inches of a single encoder.
	 * @param id
	 * @return
	 */
	public double getSingleEncoderInches(int id) {
		double[] encoderInches;
		encoderInches = new double[4];
		encoderInches[0] = frontLeftEncoder.getDistance();
//		encoderInches[1]= frontRightEncoder.getDistance();
//		encoderInches[2]= rearLeftEncoder.getDistance();
//		encoderInches[3]= rearRightEncoder.getDistance();
		return encoderInches[id];
	}
	/**
	 * Gets the value in ticks of a single encoder.
	 * @param id
	 * @return
	 */
	public double getSingleEncoderTicks(int id) {
		double[] encoderInches;
		encoderInches = new double[4];
		encoderInches[0] = frontLeftEncoder.get();
//		encoderInches[1]= frontRightEncoder.get();
//		encoderInches[2]= rearLeftEncoder.get();
//		encoderInches[3]= rearRightEncoder.get();
		return encoderInches[id];
	}
	 /**
	  * @return An array of doubles of the inches each encoder has moved in the format 
	  * [frontLeft, frontRight, rearLeft, rearRight]
	  */
	public double[] getEncoderInches() {
		double[] encoderInches;
		encoderInches = new double[4];
		encoderInches[0] = frontLeftEncoder.getDistance();
//		encoderInches[1]= frontRightEncoder.getDistance();
//		encoderInches[2]= rearLeftEncoder.getDistance();
//		encoderInches[3]= rearRightEncoder.getDistance();
		return encoderInches;
	}
	 /**
	  * @return An array of doubles of the ticks each encoder has moved in the format 
	  * [frontLeft, frontRight, rearLeft, rearRight]
	  */
	public double[] getEncoderTicks() {
		double[] encoderInches;
		encoderInches = new double[4];
		encoderInches[0] = frontLeftEncoder.get();
//		encoderInches[1]= frontRightEncoder.get();
//		encoderInches[2]= rearLeftEncoder.get();
//		encoderInches[3]= rearRightEncoder.get();
		return encoderInches;
	}
	
	
	public double getAbsAverageEncoderInches() {
		
		double frontLeftEncoderInches = Math.abs(frontLeftEncoder.getDistance());
		//double frontRightEncoderInches = Math.abs(frontLeftEncoder.getDistance());
		//double rearLeftEncoderInches = Math.abs(frontLeftEncoder.getDistance());
		//double rearRightEncoderInches = Math.abs(frontLeftEncoder.getDistance());
		
		double encoderSumInches = frontLeftEncoderInches; //+ frontRightEncoderInches 
//									+ rearLeftEncoderInches + rearRightEncoderInches; TODO right encoders fried
		double encoderAverageInches = encoderSumInches /1; // / 4;
		
		return encoderAverageInches;
	}
	

	
	public void fullEncoderReset() {
		frontLeftEncoder.reset();
//		frontRightEncoder.reset();
//		rearLeftEncoder.reset();
//		rearRightEncoder.reset();
	}
	
	public boolean hasSeenTape() {
		if(whiteTapeDetector.get() == false) {
			hasSeenTape = true;
		}
		return hasSeenTape;
	}
	
	public void resetSeenTape() {
		hasSeenTape = false;
	}

	@Override
	protected double returnPIDInput() {
		hasSeenTape();
		//readGyro();
		return getNormalizedGyroAngle();
	}
	
	public void resetGyro() {
		gyroThread.reset();
	}
	int i = 0;
	@Override
	protected void usePIDOutput(double output) {
		double rot = 0.0;
		if (!getPIDController().onTarget()) {
			if (fieldRelative || deltaRotating) {
				if(Robot.oi.isTouringMode()) {
					rot = output * Robot.oi.getTouringPower();
				}
				else {
					rot = output;
				}

			}
		} 
		else {
			deltaRotating = false;
		}
		if(!fieldRelative) {
			if(Robot.oi.isTouringMode()) {
				rot = robotRelRotX * 0.7 * Robot.oi.getTouringPower();
			}
			else {
				rot = robotRelRotX * 0.7;
			}
		}
		i++;
		if(i%10 == 0){
//			System.out.println("Field Rel: " + fieldRelative);
//			System.out.println("Thread is running: " + gyroThread.isAlive());
//			System.out.println("Set point: " + getSetpoint());
			//System.out.println("Gyro miss count: " + gyroMissCount);
//			System.out.println("Last gyro raw data: " + gyroThread.getLastRawData());
//			System.out.println("Last read gyro angle: " + /*lastGyroAngle*/ gyroThread.getLastGoodRotation());
//			System.out.println("Gyro bias: " + /*gyroBias*/ gyroThread.getBias());
			//System.out.println("Raw gyro value: " + doubleYaw);
//			System.out.println("Start angle: " + startAngle);
//			System.out.println("Calculated non-norm angle: " + getAngle());
//			System.out.println("NormalizedGyroAngle: " +  getNormalizedGyroAngle()); // print new gyro angle);
//			System.out.println("Rot Stick Degrees: " + Robot.oi.getRotationDegrees()); //rot stick degrees
//			System.out.println("Rot Power: " + rot); // motor power
			
//			System.out.println("Rear Left Encoder Stopped: " + rearLeftEncoder.getStopped());
//			System.out.println("Rear Left Encoder Inches: " + rearLeftEncoder.getDistance());
//			System.out.println("Rear Left Encoder Ticks: " + rearLeftEncoder.get());
//			System.out.println("Rear Right Encoder Stopped: " + rearRightEncoder.getStopped());
//			System.out.println("Rear Right Encoder Inches: " + rearRightEncoder.getDistance());
//			System.out.println("Rear Right Encoder Ticks: " + rearRightEncoder.get());
//			System.out.println("Front Right Encoder Stopped: " + frontRightEncoder.getStopped());
//			System.out.println("Front Right Encoder Inches: " + frontRightEncoder.getDistance());
//			System.out.println("Front Right Encoder Ticks: " + frontRightEncoder.get());
			System.out.println("Front Left Encoder Stopped: " + frontLeftEncoder.getStopped());
			System.out.println("Front Left Encoder Inches: " + frontLeftEncoder.getDistance());
//			System.out.println("Front Left Encoder Ticks: " + frontLeftEncoder.get());
		}
		lastRotation = rot;
		robotDrive.mecanumDrive_Cartesian(currentX, currentY, rot, (fieldRelative) ? getNormalizedGyroAngle() : 0);
		//pingGyro();
	}

	public double getCurrentX() {
		return currentX;
	}

	public double getCurrentY() {
		return currentY;
	}
	
	public double getLastRotation() {
		return lastRotation;
	}

	public boolean isFieldRelative() {
		return fieldRelative;
	}

	public double getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
		//this.needToSaveGyroBias = true;
		gyroThread.reset();
	}
	
	/*private synchronized void pingGyro() {
		if (!pingPending) {
			gyro.writeString("#f");
			pingPending = true;
		}
	}
	
	double doubleYaw = 0.0;
	private synchronized void readGyro() {
		//if (pingPending) {
			pingPending = false;
			String yawPitchRoll = gyro.readString();
			if(yawPitchRoll == null || yawPitchRoll.isEmpty()) {
				gyroMissCount++;
			}
			else {
				//double doubleYaw = 0.0;
				try {
					yawPitchRoll = yawPitchRoll.substring(yawPitchRoll.indexOf('=') + 1);
					String stringYaw = yawPitchRoll.substring(0, yawPitchRoll.indexOf(','));
					doubleYaw = Double.parseDouble(stringYaw);
					if (needToSaveGyroBias) {
						gyroBias = doubleYaw;
						needToSaveGyroBias = false;
					}
				}
				catch(Throwable t) {
					gyroMissCount++;
					return;
				}

				// System.out.println(doubleYaw);
				lastGyroAngle = doubleYaw + startAngle - gyroBias;
			}
		//}
	}*/
}
