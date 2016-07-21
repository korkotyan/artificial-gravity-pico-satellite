#include <SoftwareSerial.h>

SoftwareSerial BTSerial(10, 11);
char rec;
void setup()
{
  Serial.begin(9600);
  BTSerial.begin(38400);

  Serial.write("Starting\n");
}

void loop()
{
  if (BTSerial.available())
  {
    rec = BTSerial.read();
    Serial.println(rec);
  }
  Serial.println("loop");
}

