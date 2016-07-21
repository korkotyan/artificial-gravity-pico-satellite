int speedPin = 10;
int in1 = 9;
int in2 = 8;


int motorSpeed = 0;
boolean decMotorSpeed = false;
int decCounter = 0;

const int MINSPEED = 120;

String inputCommand;
int speedValue = MINSPEED;
int flag = 0;

void setup() {
  Serial.begin(9600);
  Serial.print("Artificial Gravity Project\n\n");
  pinMode(speedPin, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
}

void startMotor()
{
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  Serial.print("Motor turning on...\n");

  motorSpeed = MINSPEED;  //MINSPEED is the minimun voltage possible to run the mototr at the minimum speed
  analogWrite(speedPin, motorSpeed);

  delay(500);

  Serial.print("Motor turned on.\n\n");
}

void changeSpeed(int newSpeed)
{
  Serial.print("Changing analog speed value to ");
  Serial.print(newSpeed);
  Serial.print("...\n");

  analogWrite(speedPin, newSpeed);

  delay(500);

  Serial.print("Updated speed.\n\n");
}

void stopMotor()
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);

  motorSpeed = 0;
  analogWrite(speedPin, motorSpeed);

  Serial.print("Motor turning off...\n");
  
  delay(2000);

  Serial.print("Motor turned off.\n\n");
}


void loop()
{
  inputCommand = Serial.readString();

  if (inputCommand.equals("start"))
  {
    flag = 1;
  }
  else
  {
    if (inputCommand.equals("stop"))
    {
      flag = 2;
    }
    else
    {
      if (inputCommand.substring(0, 9).equals("new speed"))
      {
        speedValue = inputCommand.substring(10,inputCommand.length()).toInt();

        if (speedValue > MINSPEED && speedValue < 255 && flag == 3)
        {
          flag = 4;
        }
        else
        {
          if (flag != 3)
          {
            Serial.print("\nYou have to start the motor before you can update the speed\n");
          }
          else
          {
            Serial.print("\nAnalog speed value is between ");
            Serial.print(MINSPEED+1);
            Serial.print(" and 254\n");
          }
        }
      }
    }
  }
  
  if (flag == 1)
  {
    startMotor();
    flag = 3;
  }
  else
  {
    if (flag == 2)
    {
      stopMotor();
      flag = 0;
    }
    else
    {
      if (flag == 4)
      {
        changeSpeed(speedValue);
        flag = 3;
      }
    }
  }
}
