#include <SoftwareSerial.h>

SoftwareSerial BTSerial(10, 11);
int z = A1;
int y = A2;
int x = A3;

double zRes;
int negative;
int units;
int tenths;
int hundredths;
int checksum;

int g0 = 353;
int g1 = 424 - g0;

unsigned long startT;
unsigned long sec10= (unsigned long ) 10000;

int max = 0;
int min = 1000;
int currentZ;

void setup()
{
  Serial.begin(9600);
  Serial.println("Prepere...");
  BTSerial.begin(38400);
  delay(3000);
  Serial.println("Started max");
  //startT = millis();
}

void loop()
{
  zRes = analogRead(z);
  zRes = (double)(zRes - g0)/(double)g1;
  Serial.print("z: ");
  Serial.println(zRes);
  if (zRes >= 10 || zRes <= -10)
  {
    zRes = 0;
  }

  if (zRes < 0)
  {
    negative = 1;
  }
  else
  {
    negative = 0;
  }

  units = (int) zRes;

  tenths = (int) ((zRes - (double)units)*10.0);
  hundredths = (int) ((zRes - (double)units)*100.0 - ((double) tenths * 10.0));

  units = abs(units);
  tenths = abs(tenths);
  hundredths = abs(hundredths);

  checksum = (negative + units + tenths + hundredths) % 10;

  /*
  if (negative == 1)
  {
    Serial.print("-");
  }

  Serial.print(units);
  Serial.print(".");
  Serial.print(tenths);
  Serial.println(hundredths);
  Serial.println();
  */

  Serial.print("sending\n");
  delay(100);
  
  BTSerial.write((negative + 48));
  delay(100);
  BTSerial.write((units + 48));
  delay(100);
  BTSerial.write((tenths + 48));
  delay(100);
  BTSerial.write((hundredths + 48));
  delay(100);
  BTSerial.write((checksum + 48));
  delay(100);
  BTSerial.write(33);

  Serial.print("sent\n");
  
  /*
  BTSerial.write(49);
  delay(50);
  BTSerial.write(49);
  delay(50);
  BTSerial.write(48);
  delay(50);
  BTSerial.write(56);
  delay(50);
  BTSerial.write(48);
  delay(50);
  BTSerial.write(33);
  */
  
  /*
  while (millis() - startT < sec10)
  {
    //Serial.print("Z: ");
    //Serial.print(analogRead(z));
    currentZ = analogRead(z);
    if (max < currentZ)
    {
      max = currentZ;
    }
  /*
  Serial.print(" X: ");
  Serial.print(analogRead(x));

  Serial.print(" Y: ");
  Serial.println(analogRead(y));
  
  }

  Serial.print("max val: ");
  Serial.println(max);
  delay(5000);
  Serial.println("Started min");

  startT = millis();
  while (millis() - startT < sec10)
  {
    //Serial.print("Z: ");
    //Serial.print(analogRead(z));
    currentZ = analogRead(z);
    if (min > currentZ)
    {
      min = currentZ;
    }
  }

  Serial.print("min val: ");
  Serial.println(min);
  delay(10000);

  */
}
