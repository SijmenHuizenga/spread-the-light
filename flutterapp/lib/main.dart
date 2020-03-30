import 'package:flutter/material.dart';
import 'package:torch/torch.dart';
// import 'dart:io';
import 'package:flutter/foundation.dart';

void main() => runApp(MyApp());


class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
	bool isOn = false;
	bool hasTorch = false;
  bool flashcomplete = true;


  void myFlash(Duration duration) {
    flashcomplete = false;
    int milliseconds = duration.inMilliseconds;

    if (!isOn) {
      Torch.turnOn();
      isOn = true;
      Future.delayed(Duration(milliseconds: milliseconds),
        ).then((value) {
         Torch.turnOff();
         isOn = false;
         flashcomplete = true;
        });
      
    }
    
  }

  @override
  void initState() {
    myFlash(Duration(milliseconds: 1000));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
		Torch.hasTorch.then((_hasTorch) {
			setState(() {
				hasTorch = _hasTorch;			
			});
		});

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Torch Plugin example app'),
        ),
        body: Center(
          child: Column(
						mainAxisAlignment: MainAxisAlignment.center,
						crossAxisAlignment: CrossAxisAlignment.center,
						children: <Widget>[
							Text('Does it have torch: $hasTorch'),
              Text('IsOn: $isOn'),
							RaisedButton(
								child: Text('TOGGLE TORCH !!'),
								onPressed: hasTorch ? () {
									if(isOn) {
										Future.delayed(Duration(milliseconds: 0), () => Torch.turnOff());
									} else {
										Torch.turnOn();
									}
									isOn = !isOn;
								} : null,
							),
							RaisedButton(
								child: Text('FLASH'),
								onPressed: hasTorch && flashcomplete ?  () {
                  int i = 0;
                  while (i < 10) {
                    if (flashcomplete) {
                      i++;
                      myFlash(Duration(milliseconds: 1000)); 
                    }
                    debugPrint('count: $i');
                    
                  }
									// myFlash(Duration(milliseconds: 1000));
								} : null,
							),
						],
					),
        )
      ),
    );
  }
}