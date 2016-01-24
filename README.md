##Synopsis
In this project there will be created artificial gravity on the border of a centrifuge inside a pico satellite, by creating centrifugal force through rotation.
Inside the pico satellite will be a DC motor that will rotate the centrifuge at a  rate of about 150 RPM for a 0.04m (4cm) centrifuge.
The centrifuge will contain a microcontroller (Arduino NANO), Accelerometer module and a Bluetooth module.
The RPM of the motor will be controlled via a main microcontroller (I am using an Arduino MEGA), by sending Analog signal from the main controller to a speed controller.
Both microcontrollers, the one inside the centrifuge and the main microcontroller, will have Bluetooth communication.
At motion the microcontroller inside the centrifuge will read the z-axis of the accelerometer module, and will send flags or the acceleration value to the main microcontroller via Bluetooth. The main microcontroller will then increase or decrease the RPM of the motor.
The artificial gravity on the border of the centrifuge should be a constant ~1G.

##Motivation
I am a Software Engineering student, this is my final year. In the final year the students have to work on a project of a span of a year.
I was always fascinated by Astrophysics, so here I am working on creating gravity where there is non.

##Tests
This project obviously requires some hardware.
- 2 microcontrollers (I am using an Arduino MEGA and NANO)
- Speed controller (I am using the L298N module)
- Bluetooth modules (I am using HC-05)
- DC motor brushed/brushless (I am using a 300rpm brushed motor)
- Accelerometer module (I am using a ADXL335)
- 12V Battery (I am using a 3000mAh 12V Lithium-ion battery)

