#include <SoftwareSerial.h>

SoftwareSerial BTSerial(10, 11);
char rec;
int counter = 0;

char negative, units, tenths, hundredths, checksum;
void setup()
{
  Serial.begin(9600);
  BTSerial.begin(38400);

  Serial.write("Starting\n");
  rec = 0;
}

void waitForInput()
{
  while (BTSerial.available() < 1)
  {
    
  }
}

void loop()
{
  while (rec != '!')
  {
    waitForInput();
    if (BTSerial.available())
    {
      rec = BTSerial.read();
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
          units = (int)rec;
          Serial.print(rec);
          break;
        case 2:
          tenths = (int)rec;
          Serial.print(".");
          Serial.print(rec);
          break;
        case 3:
          hundredths = (int)rec;
          Serial.print(rec);
          break;
        case 4:
          checksum = (negative + units + tenths + hundredths - (48 * 3)) % 10;
          if ((int)(rec - '0') == checksum)
          {
            Serial.println(" OK");
          }
          else
          {
            Serial.println(" FAILED");
          }
      }
      counter++;
    }
  }
  if (counter != 6)
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
  delay(10);
  //Serial.println("loop");
}


