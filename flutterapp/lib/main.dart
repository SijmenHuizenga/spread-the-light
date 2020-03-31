import 'package:flutter/material.dart';
import 'package:torch/torch.dart';
import 'dart:io';
import 'package:flutterapp/main_flash.dart'
import 'package:flutter/foundation.dart';


void main() {
  runApp(TabBarDemo());
}

class TabBarDemo extends StatefulWidget {
  @override
  State<TabBarDemo> createState() => _SpreadtheLightState();
}

class _SpreadtheLightState extends State<TabBarDemo> {

  bool isOn = false;
	bool hasTorch = false;
  bool flashcomplete = true;
  double frequency = 1.0; // Hz

  void myFlash(Duration duration) {
    flashcomplete = false;
    Torch.turnOn();
    setState(() { isOn = true; });
    sleep(duration);
    Torch.turnOff();    
    setState(() { isOn = false; });  
    
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: DefaultTabController(
        length: 3,
        child: Scaffold(
          appBar: AppBar(
            bottom: TabBar(
              tabs: [
                Tab(icon: Icon(Icons.directions_car)),
                Tab(icon: Icon(Icons.directions_transit)),
                Tab(icon: Icon(Icons.directions_bike)),
              ],
            ),
            title: Text('Tabs Demo'),
          ),
          body: TabBarView(
            children: <Widget>[
              Icon(Icons.directions_car),
              Icon(Icons.directions_transit),
              Icon(Icons.directions_bike),
            ],
          ),
        ),
      ),
    );
  }
}