float A=-0.00075;
float B=0.000623;
float C=0.00000173;
int lightPin = 3;  //define a pin for Photo resistor
int ledPin=13;     //define a pin for LED
const int ledPin2 = 12;      // the pin that the LED is attached to
byte serialA;
byte Cond;
float Temper;

void setup() 
{ 
 Serial1.begin(9600); 
 pinMode(ledPin, OUTPUT );
 pinMode(ledPin2, OUTPUT);
 }
void loop() 
{

 float a0 = analogRead(0); 
 Serial.print("analogread = "); 
 Serial.print(a0); 
 Serial.print("\n"); 
 
 float voltage = a0 / 1024 * 5.0; 
 Serial.print("voltage = "); 
 Serial.print(voltage); 
 Serial.print("\n"); 

float resistance = (10000 * voltage) / (5.0 - voltage);
 Serial.print("resistance = ");
 Serial.print(resistance); 
 Serial.print("\n"); 
 float logc = log(resistance); 
 float logc3 = logc * logc * logc; 
 float kelvin = 1.0 / (A + B* log(resistance) - C* (logc3)); 
//A, B and C needs to be measured by measuring the resistance and temperature of the 
//thermistor at 3 different points. This is given by the Steinhart-Hart approximation: 
//T = 1 / (A + B log(R) + C log(R) ^ 3) 
 
 float f = (kelvin - 273.15) * 9.0/5.0 + 32.0; 
 Serial.print("temp = "); 
 Serial.print(f); 
 Serial.print("\n"); 
 Temper=f;
 
 
 
 Serial.println(analogRead(lightPin)); //Write the value of the photoresistor to the serial monitor.
 float photo= analogRead(lightPin);
  Serial.println(photo);
 analogWrite(ledPin, photo);  //send the value to the ledPin. Depending on value of resistor 
                                                //you have  to divide the value. for example, 
                                                //with a 10k resistor divide the value by 2, for 100k resistor divide by 4.
if (photo > 180)
{
analogWrite(ledPin, 255);
Serial.println("ON");
Cond=1;

}
else
{
analogWrite(ledPin, 0);  
Serial.println("OFF");
Cond=2;
}
    
 if (Serial1.available() > 0) { 
   
   serialA = Serial1.read();
Serial.println(serialA);




   
      switch (serialA) {
    case 49:
      analogWrite(ledPin2, 255);
      Serial1.write("ON");
      Serial.println("LED is ON");
      break;
    case 50:
      analogWrite(ledPin2, 0);
      Serial.println("LED is Off");
      break;
    case 51:
      analogWrite(ledPin2, 255);
      delay(100);
      analogWrite(ledPin2, 0);
      Serial.println("LED is Blinking");
      delay(100);
      break;
     default:

      break;
  }
 }
Serial1.print(Cond);
byte a,b;

a = floor(Temper); 
Serial.println(a);
b = (Temper-a) * pow(10,2);
Serial.println(b);
Serial1.print(a);
Serial1.println(b);


 delay(1000); 
} 

