int counter = 0;

void setup()
{
  Serial.begin(9600);
  attachInterrupt(0, magnetDetected, RISING);
}


void magnetDetected()
{
  counter++;
  Serial.print("Magnet Detected, count: ");
  Serial.println(counter);
}


void loop()
{
  //Serial.println("doing nothing");
}
