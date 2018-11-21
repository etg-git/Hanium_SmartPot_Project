/* arduino web client - GET request for index.html or index.php */

#include <SPI.h>
#include <Phpoc.h>

char server_name[] = "210.110.180.24";
PhpocClient client;
unsigned long ptime, ctime;
unsigned long ptime2, ctime2;

String data;
boolean auto_;
int pumptime;
int limited;
int now_relay;

// set all moisture sensors PIN ID
int moisture1 = A0;
int moisture2 = A1;
int moisture3 = A2;
int moisture4 = A3;

// declare moisture values
int moisture1_value = 0;
int moisture2_value = 0;
int moisture3_value = 0;
int moisture4_value = 0;

// set water relays
int relay1 = 3;
int relay2 = 4;
int relay3 = 5;
int relay4 = 6;

// set water pump
int pump = 2;

void setup() {
  // declare relay as output
  pinMode(relay1, OUTPUT);
  pinMode(relay2, OUTPUT);
  pinMode(relay3, OUTPUT);
  pinMode(relay4, OUTPUT);
  // declare pump as output
  pinMode(pump, OUTPUT);
  // declare the ledPin as an OUTPUT:
  Serial.begin(9600);
  Phpoc.begin(PF_LOG_SPI | PF_LOG_NET);

//  if(Serial)
//  while(!Serial)
//    ;
   data="";
   ptime = millis();
   ptime2 = millis();

   pumptime = 3000;
}

void loop() {
  ctime = millis();
  if(ctime - ptime >= 5000){ //5초 간격 sensor값 DB update
      while(!sensor_Update());
      ptime = millis();
  }

  ctime2 = millis();
  if(ctime2 - ptime2 >= 2000){
    if(pump_control()&&!auto_){ //status값이 1,2,3,4 중 하나고 -> 자동이 아닐때,
      if(now_relay==1)digitalWrite(relay1, HIGH);
      if(now_relay==2)digitalWrite(relay2, HIGH);
      if(now_relay==3)digitalWrite(relay3, HIGH);
      if(now_relay==4)digitalWrite(relay4, HIGH);
      
      digitalWrite(pump, HIGH);
      delay(pumptime);
      digitalWrite(pump, LOW);
      digitalWrite(relay1, LOW);
      digitalWrite(relay2, LOW);
      digitalWrite(relay3, LOW);
      digitalWrite(relay4, LOW);
      pump_off(); //status값을 0으로 변경
    }

    if(auto_){ //자동일때
      if(limited >= moisture1_value || limited >= moisture2_value || limited >= moisture3_value || limited >= moisture4_value){
        digitalWrite(pump, HIGH);
        if(limited >= moisture1_value)digitalWrite(relay1, HIGH);
        if(limited >= moisture2_value)digitalWrite(relay2, HIGH);
        if(limited >= moisture3_value)digitalWrite(relay3, HIGH);
        if(limited >= moisture4_value)digitalWrite(relay4, HIGH);
        delay(pumptime);
        digitalWrite(pump, LOW);
        digitalWrite(relay1, LOW);
        digitalWrite(relay2, LOW);
        digitalWrite(relay3, LOW);
        digitalWrite(relay4, LOW);
        pump_off();
      }
  }
    
    ptime2 = ctime2;
  }


}

boolean sensor_Update(){
    moisture1_value = analogRead(moisture1);
    moisture2_value = analogRead(moisture2);
    moisture3_value = analogRead(moisture3);
    moisture4_value = analogRead(moisture4);

    data = "sensor1="+ (String)moisture1_value + "&&sensor2=" + (String)moisture2_value + "&&sensor3=" + (String)moisture3_value + "&&sensor4=" + (String)moisture4_value;

    if(client.connect(server_name, 80)) {
      client.println("POST /arduino/UpdateSensors.php HTTP/1.1");
      client.println("Host: 210.110.180.24");
      client.println("Content-Type: application/x-www-form-urlencoded");
      client.print("Content-Length: ");
      client.println(data.length());
      client.println();
      client.print(data);
    }
    else{
      Serial.println("실패");
      return false;
    }
      
    if(client.connected()) {
      client.stop();
      return true;
    }
}

boolean pump_control(){

    if(client.connect(server_name, 80)) 
    {
      Serial.println("Connected to server");
      client.println("GET /arduino/GetStatus.php HTTP/1.0");
      client.println("Host: 210.110.180.24");
      client.println("Connection: close");
      client.println();
    }
    else{
      Serial.println("connection failed");
      return false;
    }

    while(true){      
      if(client.available())
      {
        char c = client.read();
        if(c=='?'){
          c = client.read();
          boolean flag = c=='1'||c=='2'||c=='3'||c=='4'; //status
          now_relay = c-48;
          c = client.read();
          auto_ = c=='1'; //auto
          String temp1=""; //limited
          String temp2=""; //pumptime
          for(int i=0;i<3;i++)temp1.concat(client.read()-48);
          for(int i=0;i<4;i++)temp2.concat(client.read()-48);

          limited = temp1.toInt();
          pumptime = temp2.toInt();

          Serial.println("auto:" + String(auto_)+ "limited:" + temp1 + " pumptime:" + temp2);
          client.stop();
          return flag;
        }
    }

    if(!client.connected())
    {
      Serial.println("disconnected");
      client.stop();
      return false;
    }
  }
}

void pump_off(){

    if(client.connect(server_name, 80)) 
    {
      Serial.println("Connected to server");
      client.println("POST /arduino/PumpOff.php HTTP/1.0");
      client.println("Host: 210.110.180.24");
      client.println("Connection: close");
      client.println();
    }
    else{
      Serial.println("connection failed");
      return false;
    }

    if(client.connected()) {
      client.stop();
    }
}
