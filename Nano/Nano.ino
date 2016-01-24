#include <SoftwareSerial.h>

int rx = 2;
int tx = 3;

SoftwareSerial BTserial(rx, tx);

int ZAxixsAPin = 28;  //analog pin for Z axixs
int AVRZ = 0; //analog value, read from accelerometer, range 0-1023
float gravity = 0;
float zeroG = 512.0;
float scale = 102.3;

char GD = 0;  //gravity digit, to send via HC-05

void setup() {
  BTserial.begin(9600);
}

void loop()
{
  AVRZ = analogRead(ZAxixsAPin);  //read analog value from accelerometer, range 0-1023

  gravity = (((float)AVRZ - zeroG) / scale);  //Convert AVOZ value to m/s^2 (gravity) ONLY on practice NEEDS calibration;

  //int tens = (int)(AVOZ / 10.0);
  //int units = (int)((AVOZ % 10.0) * 10.0);
  //int tenths = (int)((AVOZ % 1.0) * 10.0);
  //int hundredths = (int)(((AVOZ % 1.0) * 100.0) % 10.0);
  //int thousandths = (int)(((AVOZ % 1.0) * 1000.0) % 10.0);
  //---------OR-----------------
  //PUT flot value in a char array;
  
  BTserial.write(gravity);
  //---------OR-----------------
  //BTserial.write(tens);
  //delay(10);
  //BTserial.write(units);
  //delay(10);
  //BTserial.write(tenths);
  //delay(10);
  //BTserial.write(hundredths);
  //delay(10);
  //BTserial.write(thousandths);
}





