int z = A0;

int zRes = 0;
int maxR = 0;
int minR = 1000;
long startT;

void setup()
{
  Serial.begin(9600);
  Serial.println("Starting maxR in 5 sec");
  delay(4000);
  Serial.println("Starting maxR in 1 sec");
  delay(1000);
  Serial.println("Starting maxR for 8 sec");
  
  startT = millis();
}

void loop()
{
  while (millis() - startT < 8000)
  {
    zRes = analogRead(z);
    if (zRes > maxR)
    {
      maxR = zRes;
    }
  }

  Serial.print("Finished maxR, maxR = ");
  Serial.println(maxR);
  Serial.println("Starting minR in 3 sec");
  delay(3000);

  Serial.println("Starting minR for 8 sec");

  startT = millis();

  while (millis() - startT < 8000)
  {
    zRes = analogRead(z);
    if (zRes < minR)
    {
      minR = zRes;
    }
  }

  Serial.print("Finished minR, minR = ");
  Serial.println(minR);
  Serial.print("\n Rsults: maxR = ");
  Serial.println(maxR);
  Serial.print("      minR = ");
  Serial.print(minR);
  delay(10000);
}
