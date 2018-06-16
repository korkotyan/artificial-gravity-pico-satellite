##Synopsis -
In this project there will be created artificial gravity on the border of a centrifuge inside a pico satellite, by creating centrifugal force through rotation.
Inside the pico satellite will be a DC motor that will rotate the centrifuge at a  rate of about 150 RPM for a 0.04m (4cm) centrifuge.
The centrifuge will contain a microcontroller (Arduino NANO), Accelerometer module and a Bluetooth module.
The RPM of the motor will be controlled via a main microcontroller (I am using an Arduino MEGA), by sending Analog signal from the main controller to a speed controller.
Both microcontrollers, the one inside the centrifuge and the main microcontroller, will have Bluetooth communication.
At motion the microcontroller inside the centrifuge will read the z-axis of the accelerometer module, and will send flags or the acceleration value to the main microcontroller via Bluetooth. The main microcontroller will then increase or decrease the RPM of the motor.
The artificial gravity on the border of the centrifuge should be a constant ~1G.

##Motivation -
I am a Software Engineering student, this is my final year. In the final year the students have to work on a project of a span of a year.
I was always fascinated by Astrophysics, so here I am working on creating gravity where there is none.

##Components

This project obviously requires some hardware.
- 2 microcontrollers (I am using an Arduino MEGA and NANO)
- Speed controller (I am using the L298N module)
- 2 Bluetooth modules (I am using HC-05)
- 12V DC motor brushed/brushless (I am using a 300rpm brushed motor)
- Accelerometer module (I am using a ADXL335)
- 12V Battery (I am using a 3000mAh 12V Lithium-ion battery)
- A centrifuge with a radius of 0.04 m (I 3D printed it), the accelerometer is located on the perimeter of the centrifuge (4 cm radius), the Arduino NANO and the Bluetooth module are placed on the centrifuge in a way that they do not interfere with the rotation of the cetrifuge.
- A 4 wire (or more, usually sold with 6 wires) 300 rpm slip ring (was not aware of its existence when started, may not need the Bluetooth modules and the Arduino NANO, but the slip ring is needed anyway).
- Some form of a small container (I built it from wood, with 10x10x10 cm^3 dimensions, the height could be changed), wihtoud 4 walls (the remaining 2 walls should be parallel and play as the floor and ceiling of the container), also the container shoud rest on 4 short legs (~3.5 cm of space between the floor of the container and the surface it is palced on).

