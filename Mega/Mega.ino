#include <SoftwareSerial.h>

int speedPin = 10;
int in1 = 9;
int in2 = 8;

int rx = 2;
int tx = 3;
SoftwareSerial BTserial(rx, tx);

int motorSpeed = 0;
boolean decMotorSpeed = false;
int decCounter = 0;

const int MINSPEED = 40;

int gravity = 0;

void setup() {
  Serial.begin(9600);
  pinMode(speedPin, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);

  BTserial.begin(9600);
}

void startMotor()
{
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  Serial.print("Motor turning on...\n");

  motorSpeed = MINSPEED;  //MINSPEED is the minimun voltage possible to run the mototr at the minimum speed
  analogWrite(speedPin, motorSpeed);

  delay(2000);

  Serial.print("Motor turned on.");
}

void stopMotor()
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);

  motorSpeed = 0;
  analogWrite(speedPin, motorSpeed);

  Serial.print("Motor turning off...\n");
  
  delay(2000);

  Serial.print("Motor turned off.\n");
}

void speedController(int gravity)
{
  if (gravity <= 9.7)
  {
    if (decMotorSpeed == false && motorSpeed <= 250)
    {
      motorSpeed = motorSpeed + 5;
    }
    else
    {
      if (motorSpeed <= 254)
      {
        motorSpeed++;
        decCounter--;
      }
      else
      {
        Serial.print("MAX speed reached, Shuting down the motor\n");
        stopMotor();
      }
    }
  }
  else
  {
    if (gravity >= 9.9)
    {
      if (decMotorSpeed == false)
      {
        motorSpeed = motorSpeed - 5;
        decMotorSpeed = true;
      }
      else
      {
        motorSpeed--;
        decCounter++;
        
        if (decCounter > 4) //NEED to test 4 OR 5 OR 6
        {
          decMotorSpeed = false;
          decCounter = 0;
        }
      }
    }
  }

  analogWrite(speedPin, motorSpeed);

  delay(300);
}

void loop() {
  startMotor();

  while (true)
  {
    if (BTserial.available())
    {
      gravity = BTserial.read();
    }
    speedController(gravity);
  }

  stopMotor();
}
