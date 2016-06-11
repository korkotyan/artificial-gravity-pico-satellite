#include <SoftwareSerial.h>

int speedPin = 10;
int in1 = 9;
int in2 = 8;

int rx = 2;
int tx = 3;
SoftwareSerial BTSerial(12, 13);

int motorSpeed = 0;
boolean decMotorSpeed = false;
int decCounter = 0;
boolean speedChanged = false;

const int MINSPEED = 100;

char rec = 0;
double gravity = 0;
double prevGravity = gravity;
int counter = 0;
int negative = 0;
int units = 0;
int tenths = 0;
int hundredths = 0;
int checksum = 0;

unsigned long time = 0;

int incCounter = 0;
boolean inc = false;
boolean dec = false;
boolean IncDec = false;
int incDecCounter = 0;
boolean singleInc = false;
boolean singleDec = false;
boolean singleIncDec = false;
int SingleIncDecCounter = 0;
int inARowCounter = 0;

void setup() {
  Serial.begin(9600);
  pinMode(speedPin, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);

  BTSerial.begin(38400);
}

void startMotor()
{
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  Serial.print("Motor turning on...\n");

  motorSpeed = MINSPEED;  //MINSPEED is the minimun voltage possible to run the mototr at the minimum speed
  analogWrite(speedPin, motorSpeed);

  delay(500);

  Serial.print("Motor turned on.");

  /*
  for (int i = 0; i < 8; i++)
  {
  delay(2000);

  Serial.print(i);
  Serial.print(" - Dec motor speed by 5\n");

  motorSpeed = motorSpeed - 5;
  analogWrite(speedPin, motorSpeed);
  }
  */
  
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

void speedController()
{
  if (gravity <= 1.95)    //0.95
  {
    if (IncDec == false && motorSpeed <= 250)
    {
      motorSpeed = motorSpeed + 5;
      speedChanged = true;
      Serial.print("Speed inc = ");
      Serial.print(motorSpeed);
      //incCounter++;

      inc = true;
      if (dec == true)
      {
        incDecCounter++;
        dec = false;
      }
    }
    else
    {
      if (singleIncDec == false && motorSpeed < 255)
      {
        motorSpeed++;
        decCounter--;
        speedChanged = true;

        if (singleInc == true)
        {
          inARowCounter++;
        }
        else
        {
          inARowCounter = 0;
        }

        singleInc = true;
        if (singleDec == true)
        {
          singleIncDecCounter++;
          singleDec = false;
        }
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
    if (gravity >= 2.05)    //1.05
    {
      if (IncDec == false && motorSpeed >= 5)
      {
        motorSpeed = motorSpeed - 5;
        //decMotorSpeed = true;
        speedChanged = true;
        Serial.print("Speed dec = ");
        Serial.print(motorSpeed);
        //incCounter--;

        dec = true;
        if (inc == true)
        {
          incDecCounter++;
          inc = false;
        }
      }
      else
      {
        if (singleIncDec == false && motorSpeed > 0)
        {
          motorSpeed--;
          decCounter++;
          speedChanged = true;
    
          /*
          if (decCounter > 4) //NEED to test 4 OR 5 OR 6
          {
            decMotorSpeed = false;
            decCounter = 0;
          }
          */
    
          if (singleDec == true)
          {
            inARowCounter++;
          }
          else
          {
            inARowCounter = 0;
          }
          
          singleDec = true;
          if (singleInc == true)
          {
            singleIncDecCounter++;
            singleInc = false;
          }
        }
      }
    }
    else
    {
      speedChanged = false;
    }
  }

  if (speedChanged == true)
  {
    analogWrite(speedPin, motorSpeed);

    delay(300);
  }

  if (incDecCounter > 5)
  {
    IncDec = true;
    incDecCounter = 0;
  }

  if (inARowCounter > 5)
  {
    incDec = false;
    inARowCounter = 0;
  }

  if (singleIncDecCounter > 9)
  {
    singleIncDec = true;
    singleIncDecCounter = 0;
  }

  Serial.print(" -- time: ");
  Serial.println((millis() - time));
}


void checkGravityInput()
{
  if (gravity < 0 || gravity > 5)
  {
    Serial.print("Bad Gravity, set to ");
    gravity = 2.0;
    //gravity = prevGravity;
  }
}

void waitForInput()
{
  //Serial.print("waiting...");
  while (BTSerial.available() < 1)
  {
    
  }

  if (time == 0)
  {
    time = millis();
  }
  //Serial.println("reveived");
}

void readBT()
{
  while (rec != '!')
  {
    //Serial.println("wiating for char arival from bluetooth");
    
    waitForInput();
    
    if (BTSerial.available())
    {
      rec = BTSerial.read();
      //Serial.println("read char from bluethooth");
      switch (counter)
      {
        case 0:
          negative = (int)(rec - '0');
          if (negative == 1)
          {
            Serial.print("-");
          }
          break;
        case 1:
          units = (int)rec - '0';
          Serial.print(rec);
          break;
        case 2:
          tenths = (int)rec - '0';
          Serial.print(".");
          Serial.print(rec);
          break;
        case 3:
          hundredths = (int)rec - '0';
          Serial.print(rec);
          break;
        case 4:
          checksum = (negative + units + tenths + hundredths) % 10;
          if ((int)(rec - '0') == checksum)
          {
            prevGravity = gravity;
            gravity = ((double)((double)(hundredths)/100.0)) + ((double)((double)(tenths)/10.0)) + ((double)(units));
            if (negative == 1)
            {
              gravity = gravity * (-1.0);
            }
            Serial.print(" OK - ");
            checkGravityInput();
            Serial.println(gravity);
          }
          else
          {
            Serial.println(" FAILED");
          }
      }
      counter++;
    }
  }
  if (counter < 5)
  {
    Serial.println("less than 6 bytes received");
    while(rec != '!')
    {
      if (BTSerial.available())
      {
        rec = BTSerial.read();
      }
    }
  }
  rec = 0;
  counter = 0;
  //delay(10);
}

void loop() {
  startMotor();
  Serial.print(" Please turn on the motor battery supply\n");

  while (true)
  {
    readBT();
    speedController();
  }

  stopMotor();
}
