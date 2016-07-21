#include <SoftwareSerial.h>

String incoming;

int pin13 = 13;

char tmp;



String inputString = "";         // a string to hold incoming data (this is general code you can reuse)
boolean stringComplete = false;  // whether the string is complete (this is general code you can reuse)


void setup()
{
  Serial.begin(9600);
  Serial.println("Begin");

  pinMode(pin13, OUTPUT);
  digitalWrite(pin13, LOW);
}

void loop()
{
  /*
  while (Serial.available() && !stringComplete) {
     // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    Serial.println(inChar);
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    }
  }
  

   if (stringComplete) {
      digitalWrite(pin13, HIGH);
      Serial.println("OK");
      char tmp = inputString.charAt(0);
      if(tmp >= 0 && tmp <= 255) { // OK is a message I know (this is general code you can reuse)
        
      }
    }

  */

  while (Serial.available() > 0)
  {
    incoming = Serial.readString();
    tmp = inputString.charAt(0);
    //incoming = incoming.substring(0,2);

    if (tmp == 0)
    {
      digitalWrite(pin13, HIGH);
    }

/*
    if (incoming.startsWith("alp://"))
    {
      digitalWrite(pin13, HIGH);

      Serial.write("OK");
      Serial.write(255);
      Serial.flush();
    }
    else
    {
      //digitalWrite(pin13, HIGH);
      Serial.write("Not OK");
      Serial.write(255);
      Serial.flush();
    }
    */
  }

  
}
